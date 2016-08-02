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
define([
    "jquery",
    'underscore',
    'nprogress',
    "jBoxUtil",
    'bootbox',
    "commonutils",
    "bootstrap",
    'pagination'
], function ($, _, NProgress, jBoxUtil, bootbox, commonutils) {
    var articleList = {};

    var pageSize = 2;
    var currentPage = 0;
    var totalPages = 0;

    var articleListHTML = '<article class="list-group-item" data-uid="<%= articleUid %>">' +
        '<h4><a class="article-content-pjax-href text-info" href="/article/<%= articleUid %>"><%= articleTitle %></a></h4>' +
        '<section title="<%= articleSummary %>"><%= articleSummary %></section>' +
        '<div>' +
        '<small>' +
        '<%= articleHits %>人阅读&nbsp;' +
        '<i class="icon-calendar"></i><%= articleCreateTime %>' +
        '</small>' +
        '<button data-uid="<%= articleUid %>" type="button" class="btn btn-danger btn-xs btn-list-article-recycle"><i class="glyphicon glyphicon-trash"></i>删除</button>' +
        '<button data-uid="<%= articleUid %>" type="button" class="btn btn-warning btn-xs btn-list-article-withdraw"><i class="glyphicon glyphicon-share"></i>撤回</button>' +
        '<a href="/editor/<%= articleUid %>" type="button" class="btn btn-primary btn-xs btn-article-edit"><i class="glyphicon glyphicon-edit"></i>编辑</a>' +
        '</div>' +
        '</article>';

    var articleListAnonymousHTML = '<article class="list-group-item" data-uid="<%= articleUid %>">' +
        '<h4><a class="article-content-pjax-href text-info" href="/article/<%= articleUid %>"><%= articleTitle %></a></h4>' +
        '<section title="<%= articleSummary %>"><%= articleSummary %></section>' +
        '<div>' +
        '<small>' +
        '<%= articleHits %>人阅读&nbsp;' +
        '<i class="icon-calendar"><%= articleCreateTime %></i>' +
        '</small>' +
        '</div>' +
        '</article>';

    var funcname = null;
    var funcparam = "";

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
    searchFuncs.none = function (page) {
        console.warn("Cannot Find Any Search Function!");
    };

    function searchArticles(url, page) {
        NProgress.start();
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
                NProgress.done();
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

        $(".article-list").empty().append(list.join(""));

        currentPage = _page;
        totalPages = _totalPages < 1 ? 1 : _totalPages;
        /*$('.pagination.article-pagination').jqPagination("option", "current_page", currentPage + 1);*/
        $('.pagination.article-pagination').jqPagination("option", "max_page", totalPages);
    }

    var searchFunc = searchFuncs.none;
    articleList.initArticleList = function () {
        currentPage = 0;
        totalPages = 0;

        funcname = $("#funcname").val();
        funcparam = $("#funcparam").val();

        var func = searchFuncs[funcname];
        searchFunc = typeof func == "function" ? func : searchFuncs.none;

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

        searchFunc(0);
    };

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
        var remain = $("article[data-uid]").length - 1;

        if (totalPages > 1 && totalPages == (currentPage + 1) && remain < 1) {
            // 最后一页，并且没有内容了，回退一页
            $('.pagination.article-pagination').jqPagination('option', 'current_page', currentPage);
        } else {
            // 刷新当前页
            searchFunc(currentPage);
        }
    }

    return articleList;
});