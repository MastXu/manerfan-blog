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
 * 文章设置
 * Created by ManerFan on 2016/4/8.
 */
require([
    "jquery",
    'underscore',
    "commonutils",
    "jBoxUtil",
    'bootbox',
    'text!pages/article/html/articleList.html',
    'pagination'
], function ($, _, commonutils, jBoxUtil, bootbox, articleListHTML) {
    var pageSize = 2;
    var currentPage = 0;
    var totalPages = 0;

    $('.pagination.article-settings-pagination').jqPagination({
        page_string: '第{current_page}页, 共{max_page}页',
        current_page: 1,
        max_page: null,
        paged: function (page) {
            if (page == currentPage + 1) {
                return;
            }

            findArticleList(page - 1, pageSize);
        }
    });

    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='article-settings']").click(function () {
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='article-settings']").show();

        if ($(this).data("hasOpend")) {
            if ($('.pagination.article-settings-pagination').jqPagination('option', 'current_page') > 1) {
                $('.pagination.article-settings-pagination').jqPagination('option', 'current_page', 1);
            } else {
                findArticleList(0, pageSize);
            }
        } else {
            $(this).data("hasOpend", true);
            findArticleList(0, pageSize);
        }
    });

    /**
     * 撤回到草稿箱
     */
    $(document).on("click", ".btn-list-article-withdraw", function () {
        var uid = $(this).data("uid");
        bootbox.confirm("此操作将该文章撤回到草稿箱", function (result) {
            if (!!result) {
                updateArticleState(uid, "DRAFT");
            }
        });
    });

    /**
     * 将文章移动到回收站
     */
    $(document).on("click", ".btn-list-article-recycle", function () {
        var uid = $(this).data("uid");
        bootbox.confirm("此操作将该文章放入回收站", function (result) {
            if (!!result) {
                updateArticleState(uid, "DELETED");
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
        var currPageNum = totalPages - currentPage * pageSize; // （包括）当前页之后还有多少条
        if (currPageNum <= 1) { // 删完之后这页就没有了，需要回退一页
            currentPage--;
        }

        if (currentPage >= 0) {
            findArticleList(currentPage, pageSize);
        } else { // 已经没有
            $("article[data-uid='" + uid + "']").remove();

            $('.pagination.article-settings-pagination').jqPagination('option', 'max_page', 1);
            $('.pagination.article-settings-pagination').jqPagination('option', 'current_page', 1);
        }
    }

    /**
     * 查询已发布文章列表
     * @param page
     * @param size
     */
    function findArticleList(page, size) {
        $("._loading").show();
        $(".article-settings-list").empty();
        $.ajax({
            url: "/article/list",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {state: 'PUBLISHED', page: page, size: size},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                var list = [];
                _.each(data.articles, function (article) {
                    list.push(_.template(articleListHTML)({
                        articleCreateTime: commonutils.dateFormate(new Date(article.createTime)),
                        articleTitle: article.title,
                        articleSummary: article.summary,
                        articleHits: article.hits,
                        articleUid: article.uid
                    }));
                });

                $(".article-settings-list").append(list.join(""));

                currentPage = page;
                totalPages = data.totalPages;
                /*$('.pagination.article-settings-pagination').jqPagination("option", "current_page", currentPage + 1);*/
                $('.pagination.article-settings-pagination').jqPagination("option", "max_page", totalPages < 1 ? 1 : totalPages);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    $(".list-group-item[data-action='article-settings']").css("visibility", "visible");
});