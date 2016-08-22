/*
 * ManerFan(http://www.manerfan.com). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 回收站
 * Created by ManerFan on 2016/4/8.
 */
require([
    "jquery",
    'underscore',
    'nprogress',
    "commonutils",
    "jBoxUtil",
    'bootbox',
    'text!pages/article/html/recycleList.html',
    'pagination'
], function ($, _, NProgress, commonutils, jBoxUtil, bootbox, recycleListHTML) {
    var pageSize = 2;
    var currentPage = 0;
    var totalPages = 0;

    $('.pagination.recycle-bin-pagination').jqPagination({
        page_string: '第{current_page}页, 共{max_page}页',
        current_page: 1,
        max_page: 1,
        paged: function (page) {
            if (page == currentPage + 1) {
                return;
            }

            findRecycleList(page - 1, pageSize);
        }
    });

    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='recycle-bin']").click(function () {
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='recycle-bin']").show();

        if ($(this).data("hasOpend")) {
            if ($('.pagination.recycle-bin-pagination').jqPagination('option', 'current_page') > 1) {
                $('.pagination.recycle-bin-pagination').jqPagination('option', 'current_page', 1)
            } else {
                findRecycleList(0, pageSize);
            }
        } else {
            $(this).data("hasOpend", true);
            findRecycleList(0, pageSize);
        }

    });

    /**
     * 将文章彻底删除
     */
    $(document).on("click", ".btn-article-delete", function () {
        var uid = $(this).data("uid");
        bootbox.confirm("此操作将彻底删除该文章，且无法恢复", function (result) {
            if (!!result) {
                $("._loading").show();
                $.ajax({
                    url: "/article/delete/" + uid,
                    async: true,
                    type: 'post',
                    cache: false,
                    dataType: 'json',
                    success: function (data, textStatus, XMLHttpRequest) {
                        if (null != data.errmsg) {
                            // 出现错误
                            jBoxUtil.noticeError({content: data.errmsg});
                            return;
                        }

                        resetArticleList(uid);
                    },
                    error: function () {
                        jBoxUtil.noticeError({content: "未知错误"});
                    },
                    complete: function () {
                        $("._loading").hide();
                    }
                });
            }
        });
    });

    /**
     * 将文章恢复到草稿箱
     */
    $(document).on("click", ".btn-article-recovery", function () {
        var uid = $(this).data("uid");
        bootbox.confirm("此操作将该文章恢复到草稿箱", function (result) {
            if (!!result) {
                updateArticleState(uid, "DRAFT");
            }
        });
    });

    /**
     * 修改文章状态
     * @param uid
     * @param state
     */
    function updateArticleState(uid, state) {
        $("._loading").show();
        $.ajax({
            url: "/article/update/state",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {state: state, uid: uid},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                resetArticleList(uid);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    function resetArticleList(uid) {
        var remain = $(".recycle-bin-list article[data-uid]").length - 1;

        if (totalPages > 1 && totalPages == (currentPage + 1) && remain < 1) {
            // 最后一页，并且没有内容了，回退一页
            $('.pagination.recycle-bin-pagination').jqPagination('option', 'current_page', currentPage);
        } else {
            // 刷新当前页
            findRecycleList(currentPage, pageSize);
        }
    }

    function findRecycleList(page, size) {
        NProgress.start();
        $(".recycle-bin-list").empty();
        $.ajax({
            url: "/article/query",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {state: 'DELETED', page: page, size: size},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                var list = [];
                _.each(data.articles, function (article) {
                    list.push(_.template(recycleListHTML)({
                        articleCreateTime: commonutils.dateFormate(new Date(article.createTime)),
                        articleLastModTime: commonutils.dateFormate(new Date(article.lastModTime)),
                        articleTitle: article.title,
                        articleSummary: article.summary,
                        articleUid: article.uid
                    }));
                });

                $(".recycle-bin-list").append(list.join(""));

                currentPage = page;
                totalPages = data.totalPages;
                /*$('.pagination.recycle-bin-pagination').jqPagination("option", "current_page", currentPage + 1);*/
                $('.pagination.recycle-bin-pagination').jqPagination("option", "max_page", totalPages < 1 ? 1 : totalPages);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                NProgress.done();
            }
        });
    }

    $(".list-group-item[data-action='recycle-bin']").css("visibility", "visible");
});