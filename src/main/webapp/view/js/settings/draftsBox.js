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
    var pageSize = 2;
    var currentPage = 0;
    var totalPages = 0;

    $('.pagination.drafts-box-pagination').jqPagination({
        page_string: '第{current_page}页, 共{max_page}页',
        current_page: 1,
        max_page: null,
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

        if (!!$(".panel[data-action='drafts-box']").data("hasOpened")) {
            $('.pagination.drafts-box-pagination').jqPagination("option", "current_page", 1);
        } else {
            $(".panel[data-action='drafts-box']").data("hasOpened", true)
            findDraftList(1, pageSize);
        }
    });

    function findDraftList(page, size) {
        $("._loading").show();
        $(".drafts-box-list").empty();
        $.ajax({
            url: "/article/list",
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
                $('.pagination.drafts-box-pagination').jqPagination("option", "max_page", totalPages);
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