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
 * 边条
 * Created by ManerFan on 2016/4/26.
 */
define([
    "jquery",
    'underscore',
    "bootstrap"
], function ($, _) {

    var articleWidget = {};

    var execFlow = [];

    /*********************
     * 文章分类
     *********************/
    var categoryListHTML = '<a href="/article/category/<%= categoryName %>" class="article-list-pjax-href has-badge"><span class="text-overflow"><i class="glyphicon glyphicon-tags"></i> <%= categoryName %></span><span class="badge"><%= articleNum %></span></a>';
    var archiveListHTML = '<a href="/article/archive/<%= archiveDate %>" class="article-list-pjax-href has-badge"><span class="text-overflow"><i class="glyphicon glyphicon-calendar"></i> <%= archiveDate %></span><span class="badge"><%= articleNum %></span></a>';
    var hitsListHTML = '<a href="/article/<%= articleUid %>" class="article-content-pjax-href has-badge" title="[<%= articleTitle %>]\n<%= articleSummary %>"><span class="text-overflow"><i class="glyphicon glyphicon-calendar"></i> <%= articleTitle %></span><span class="badge"><%= articleHits %></span></a>';

    function initCategory() {
        $.ajax({
            url: "/article/category/hots/10",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    console.error("Get Hots Category Error! [" + data.errmsg + "]");
                    return;
                }

                if (_.isArray(data.categories) && data.categories.length > 0) {
                    var categories = [];
                    _.each(data.categories, function (category) {
                        categories.push(_.template(categoryListHTML)({
                            categoryName: category.name,
                            articleNum: category.num
                        }));
                    });

                    $(".widget-category").empty().append(categories.join(''));
                }
            },
            error: function () {
                console.error("Get Hots Category Error!");
            },
            complete: function () {
                execNext();
            }
        });
    }

    execFlow.push(initCategory);

    /*********************
     * 文章存档
     *********************/
    function initArchive() {
        $.ajax({
            url: "/article/archive/hots/12",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    console.error("Get Hots Archive Error! [" + data.errmsg + "]");
                    return;
                }

                if (_.isArray(data.archives) && data.archives.length > 0) {
                    var archives = [];
                    _.each(data.archives, function (archive) {
                        archives.push(_.template(archiveListHTML)({
                            archiveDate: archive.date,
                            articleNum: archive.num
                        }));
                    });

                    $(".widget-archive").empty().append(archives.join(''));
                }
            },
            error: function () {
                console.error("Get Hots Archive Error!");
            },
            complete: function () {
                execNext();
            }
        });
    }

    execFlow.push(initArchive);

    /*********************
     * 文章阅读排行
     *********************/
    function initHits() {
        $.ajax({
            url: "/article/hit/hots/10",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    console.error("Get Hots Archive Error! [" + data.errmsg + "]");
                    return;
                }

                if (_.isArray(data.articles) && data.articles.length > 0) {
                    var articles = [];
                    _.each(data.articles, function (article) {
                    	var hits = article.hits;
                    	if (hits > 9999) {
                    		hits = (hits / 10000.0).toFixed(1) + '万';
                    	}
                        articles.push(_.template(hitsListHTML)({
                            articleUid: article.uid,
                            articleTitle: article.title,
                            articleSummary: article.summary,
                            articleHits: hits
                        }));
                    });

                    $(".widget-hits").empty().append(articles.join(''));
                }
            },
            error: function () {
                console.error("Get Hots Archive Error!");
            },
            complete: function () {
                execNext();
            }
        });
    }

    execFlow.push(initHits);

    /**
     * 执行下一个流程
     */
    function execNext() {
        var callback = execFlow.shift();
        if (typeof callback == "function") {
            callback();
        }
    }

    /**
     * 开始执行流程
     */
    articleWidget.init = function () {
        execNext();
    };

    return articleWidget;
});
