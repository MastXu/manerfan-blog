/**
 * Created by ManerFan on 2016/3/31 0031.
 */

define([
    "jquery",
    "js/editor/extensions/echartsParser",
    "js/editor/extensions/mathJax",
    "js/article/providers/duoshuoProvider"
], function ($, echartsParser, mathJax, duoshuo) {
    var articleContent = {};

    articleContent.initArticleContent = function () {
        try {
            duoshuo.initDuoshuo();
        } catch (e) {
            console.error("Init Duoshuo Error!");
            console.error(e);
        }

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