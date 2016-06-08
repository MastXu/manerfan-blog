/**
 * Created by ManerFan on 2016/3/31 0031.
 */

define([
    "jquery",
    "js/editor/extensions/echartsParser",
    "js/editor/extensions/mathJax"
], function ($, echartsParser, mathJax) {
    var articleContent = {};

    articleContent.initArticleContent = function () {
        mathJax.onAsyncPreview(function () {
            try {
                echartsParser.parseArticle($("code.language-echarts"));
            } catch (e) {
                console.error("Pares Echarts Error!");
                console.error(e);
            }
        });
    };

    return articleContent;
});