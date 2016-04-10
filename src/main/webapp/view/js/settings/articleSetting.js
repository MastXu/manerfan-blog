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

        if (!!$(".panel[data-action='article-settings']").data("hasOpened")) {
            $('.pagination.article-settings-pagination').jqPagination("option", "current_page", 1);
        } else {
            $(".panel[data-action='article-settings']").data("hasOpened", true)
            findArticleList(1, pageSize);
        }
    });

    /**
     * 撤回到草稿箱
     */
    $(document).on("click", ".btn-article-withdraw", function () {
        var uid = $(this).data("uid");
        bootbox.confirm("此操作将该文章撤回到草稿箱", function (result) {
            if (!!result) {
                alert(uid);
            }
        });
    });

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
                $('.pagination.article-settings-pagination').jqPagination("option", "max_page", totalPages);
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