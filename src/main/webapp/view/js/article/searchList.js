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
 * 文章检索
 * Created by ManerFan on 2016/4/26 0026.
 */
require([
    "jquery",
    'underscore',
    'nprogress',
    "jBoxUtil",
    "commonutils",
    "js/article/articleWidget",
    'pjax',
    "bootstrap"
], function ($, _, NProgress, jBoxUtil, commonutils, articleWidget) {
    var articleListHTML = '<article class="list-group-item">' +
        '<h4><a class="text-info" target="_blank" href="/article/<%= articleUid %>"><%= articleTitle %></a></h4>' +
        '<section title="<%= articleSummary %>"><%= articleSummary %></section>' +
        '<small>' +
        '<i class="glyphicon glyphicon-calendar"></i> <%= articleCreateTime %>&nbsp;' +
        '<i class="glyphicon glyphicon-heart-empty"></i> 相关度 <%= articleScore %>' +
        '</small>' +
        '</article>';

    var afterDocs = [];
    afterDocs[0] = null;

    var pageSize = 2;
    var currentPage = 0;
    var totalPages = 0;

    $(".next-page").click(function () {
        if (currentPage >= totalPages) {
            return;
        }

        querySearch(currentPage + 1);
    });

    $(".prev-page").click(function () {
        if (currentPage <= 1) {
            return;
        }

        querySearch(currentPage - 1);
    });

    function querySearch(page) {
        NProgress.start();
        $.ajax({
            url: "/article/search/query",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: $.extend({
                kw: $("#keywords").val(),
                numHits: pageSize
            }, afterDocs[page - 1]),
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                if (data.total < 1) {
                    $(".pjax-content").empty().append("<h1 style='color: lightgray;margin: 40px 0 20px;'>→_→ 无搜索结果</h1><h4 style='color: lightgray;margin: 20px 0 40px;'>请尝试其他关键词</h4>");
                    $(".prev-page").addClass("disabled");
                    $(".next-page").addClass("disabled");

                    $(".searchtotal span").text(0);
                    $("input[name='kw']").select().focus();
                } else {
                    afterDocs[page] = data.after;
                    currentPage = page;
                    totalPages = Math.ceil(data.total / pageSize);

                    $(".searchtotal span").text(totalPages);
                    reShowArticleList(data.articles);
                }
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                NProgress.done();
            }
        });
    }

    function reShowArticleList(_articles) {
        var list = [];
        _.each(_articles, function (article) {
            list.push(_.template(articleListHTML)({
                articleCreateTime: commonutils.dateFormate(new Date(article.createTime)),
                articleUid: article.uid,
                articleTitle: article.title,
                articleSummary: article.summary,
                articleScore: article.score.toFixed(2)
            }));
        });

        $(".article-list").empty().append(list.join(""));

        $("ul.pager li").removeClass("disabled");
        if (currentPage <= 1) {
            $(".prev-page").addClass("disabled");
        }
        if (currentPage >= totalPages) {
            $(".next-page").addClass("disabled");
        }
    }

    querySearch(1);

    if ($.support.pjax) {
        $(document).on('pjax:start', function () {
            NProgress.start();
        });

        $(document).on('pjax:end', function () {
            NProgress.done();

            afterDocs = [];
            afterDocs[0] = null;
            currentPage = 0;
            totalPages = 0;

            $(".prev-page").addClass("disabled");
            $(".next-page").addClass("disabled");

            querySearch(1);
        });

        $(document).on('submit', 'form.article-search-pjax-submit', function (event) {
        	if ($.trim($("#keywords").val()).length > 0) {
        		// 只有在有关键词查询时，才使用pjax
	            $.pjax.submit(event, '.pjax-content');
	            event.preventDefault();
	            event.stopPropagation();
            }
        });
    }

    window.setTimeout(articleWidget.init, 1000);
});
