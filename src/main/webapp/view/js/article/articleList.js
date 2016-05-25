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
 * 文章列表
 * Created by ManerFan on 2016/4/26 0026.
 */
require([
    "jquery",
    'underscore',
    "jBoxUtil",
    "commonutils",
    "js/article/articleWidget",
    "bootstrap",
    'pagination'
], function ($, _, jBoxUtil, commonutils, articleWidget) {
    var pageSize = 2;
    var currentPage = 0;
    var totalPages = 0;

    var articleListHTML = '<article class="list-group-item" data-uid="<%= articleUid %>">' +
        '<h4><a class="text-info" href="/article/<%= articleUid %>"><%= articleTitle %></a></h4>' +
        '<section class="text-overflow"><%= articleSummary %></section>' +
        '<div>' +
        '<span class="label label-info has-badge">阅读<span class="badge"><%= articleHits %></span></span>' +
        '<span class="label label-info"><i class="icon-calendar"><%= articleCreateTime %></i></span>' +
        '<button data-uid="<%= articleUid %>" type="button" class="btn btn-danger btn-xs btn-list-article-recycle"><i class="glyphicon glyphicon-trash"></i>删除</button>' +
        '<button data-uid="<%= articleUid %>" type="button" class="btn btn-warning btn-xs btn-list-article-withdraw"><i class="glyphicon glyphicon-share"></i>撤回</button>' +
        '<a href="/editor/<%= articleUid %>" type="button" class="btn btn-primary btn-xs btn-article-edit"><i class="glyphicon glyphicon-edit"></i>编辑</a>' +
        '</div>' +
        '</article>';

    var articleListAnonymousHTML = '<article class="list-group-item" data-uid="<%= articleUid %>">' +
        '<h4><a class="text-info" href="/article/<%= articleUid %>"><%= articleTitle %></a></h4>' +
        '<section class="text-overflow"><%= articleSummary %></section>' +
        '<div>' +
        '<span class="label label-info has-badge">阅读<span class="badge"><%= articleHits %></span></span>' +
        '<span class="label label-info"><i class="icon-calendar"><%= articleCreateTime %></i></span>' +
        '</div>' +
        '</article>';

    $('.pagination.article-pagination').jqPagination({
        page_string: '第{current_page}页, 共{max_page}页',
        current_page: 1,
        max_page: null,
        paged: function (page) {
            if (page == currentPage + 1) {
                return;
            }

            searchFunc(page - 1);
        }
    });

    var searchFuncs = {};
    searchFuncs.article = function (page) {
        searchArticles("/article/query/", page);
    };
    searchFuncs.category = function (page) {
        searchArticles("/article/category/query/", page);
    };
    searchFuncs.archive = function (page) {
        searchArticles("/article/archive/query/", page);
    };
    searchFuncs.search = function (page) {

    };

    var func = searchFuncs[funcname];
    var searchFunc = typeof func == "function" ? func : function () {
        console.warn("Cannot Find Any Search Function!");
    };

    function searchArticles(url, page) {
        $("._loading").show();
        $.ajax({
            url: url + funcparam,
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {state: 'PUBLISHED', page: page, size: pageSize},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                reShowArticleList(data.articles, page, data.totalPages);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    function reShowArticleList(_articles, _page, _totalPages) {
        var list = [];
        _.each(_articles, function (article) {
            list.push(_.template(!!logined ? articleListHTML : articleListAnonymousHTML)({
                articleCreateTime: commonutils.dateFormate(new Date(article.createTime)),
                articleTitle: article.title,
                articleSummary: article.summary,
                articleHits: article.hits,
                articleUid: article.uid
            }));
        });

        $(".article-list").append(list.join(""));

        currentPage = _page;
        totalPages = _totalPages < 1 ? 1 : _totalPages;
        /*$('.pagination.article-pagination').jqPagination("option", "current_page", currentPage + 1);*/
        $('.pagination.article-pagination').jqPagination("option", "max_page", totalPages);
    }

    searchFunc(0);

    window.setTimeout(articleWidget.init, 1000);
});
