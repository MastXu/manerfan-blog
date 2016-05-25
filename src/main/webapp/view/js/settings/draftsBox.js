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
 * 草稿箱
 * Created by ManerFan on 2016/4/8.
 */
require([
    "jquery",
    'underscore',
    "commonutils",
    "jBoxUtil",
    'bootbox',
    'text!pages/article/html/draftList.html',
    'pagination'
], function ($, _, commonutils, jBoxUtil, bootbox, draftListHTML) {
    var pageSize = 5;
    var currentPage = 0;
    var totalPages = 0;

    $('.pagination.drafts-box-pagination').jqPagination({
        page_string: '第{current_page}页, 共{max_page}页',
        current_page: 1,
        max_page: 1,
        paged: function (page) {
            if (page == currentPage + 1) {
                return;
            }

            findDraftList(page - 1, pageSize);
        }
    });

    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='drafts-box']").click(function () {
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='drafts-box']").show();

        if ($(this).data("hasOpend")) {
            if ($('.pagination.drafts-box-pagination').jqPagination("option", "current_page") > 1) {
                $('.pagination.drafts-box-pagination').jqPagination("option", "current_page", 1);
            } else {
                findDraftList(0, pageSize);
            }
        } else {
            $(this).data("hasOpend", true);
            findDraftList(0, pageSize);
        }
    });

    /**
     * 将文章移动到回收站
     */
    $(document).on("click", ".btn-drafts-article-recycle", function () {
        var uid = $(this).data("uid");
        bootbox.confirm("此操作将该草稿放入回收站", function (result) {
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
            findDraftList(currentPage, pageSize);
        } else { // 已经没有
            $("article[data-uid='" + uid + "']").remove();

            $('.pagination.drafts-box-pagination').jqPagination('option', 'max_page', 1);
            $('.pagination.drafts-box-pagination').jqPagination('option', 'current_page', 1);
        }
    }

    function findDraftList(page, size) {
        $("._loading").show();
        $(".drafts-box-list").empty();
        $.ajax({
            url: "/article/query",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {state: 'DRAFT', page: page, size: size},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                var list = [];
                _.each(data.articles, function (article) {
                    list.push(_.template(draftListHTML)({
                        articleCreateTime: commonutils.dateFormate(new Date(article.createTime)),
                        articleLastModTime: commonutils.dateFormate(new Date(article.lastModTime)),
                        articleTitle: article.title,
                        articleSummary: article.summary,
                        articleUid: article.uid
                    }));
                });

                $(".drafts-box-list").append(list.join(""));

                currentPage = page;
                totalPages = data.totalPages;
                /*$('.pagination.drafts-box-pagination').jqPagination("option", "current_page", currentPage + 1);*/
                $('.pagination.drafts-box-pagination').jqPagination("option", "max_page", totalPages < 1 ? 1 : totalPages);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    $(".list-group-item[data-action='drafts-box']").css("visibility", "visible");
});