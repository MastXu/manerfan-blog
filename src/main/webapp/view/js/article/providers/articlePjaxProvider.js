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

define([
    "jquery",
    'nprogress',
    'js/article/providers/articleContentProvider',
    'js/article/providers/articleListProvider',
    'pjax'
], function ($, NProgress, articleContent, articleList) {
    var articlePjax = {};

    if ($.support.pjax) {
        // pjax完成后的回调
        var callback = function () {
            // DO NOTHING ...
        };

        $(document).on('pjax:start', function () {
            NProgress.start();
        });

        $(document).on('pjax:end', function () {
            NProgress.done();

            if (typeof callback == "function") {
                callback();
            }
        });

        // 点击 文章内容 链接
        $(document).on('click', 'a.article-content-pjax-href', function (event) {
            callback = articleContent.initArticleContent;
            $.pjax.click(event, {container: '.pjax-content'});
            event.preventDefault();
            event.stopPropagation();
        });

        // 点击 文章列表 链接
        $(document).on('click', 'a.article-list-pjax-href', function (event) {
            callback = articleList.initArticleList;
            $.pjax.click(event, {container: '.pjax-content'});
            event.preventDefault();
            event.stopPropagation();
        });
    }

    return articlePjax;
});
