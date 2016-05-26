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
    "jBoxUtil",
    "commonutils",
    "js/article/articleWidget",
    "bootstrap"
], function ($, _, jBoxUtil, commonutils, articleWidget) {
    var articleListHTML = '<article class="list-group-item">' +
        '<h4><a class="text-info" target="_blank" href="/article/<%= articleUid %>"><%= articleTitle %></a></h4>' +
        '<section title="<%= articleSummary %>"><%= articleSummary %></section>' +
        '<small>' +
        '<i class="glyphicon glyphicon-calendar"></i> <%= articleCreateTime %>&nbsp;' +
        '<i class="glyphicon glyphicon-heart-empty"></i> 相关度 <%= articleScore %>' +
        '</small>' +
        '</article>';

    $.ajax({
        url: "/article/search/query",
        async: true,
        type: 'post',
        cache: false,
        dataType: 'json',
        data: {
            kw: $("#keywords").val(),
            numHits: 8
        },
        success: function (data, textStatus, XMLHttpRequest) {
            if (null != data.errmsg) {
                // 出现错误
                jBoxUtil.noticeError({content: data.errmsg});
                return;
            }

            reShowArticleList(data.articles, data.total);
        },
        error: function () {
            jBoxUtil.noticeError({content: "未知错误"});
        },
        complete: function () {
        }
    });

    function reShowArticleList(_articles, _total) {
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
    }

    window.setTimeout(articleWidget.init, 1000);
});
