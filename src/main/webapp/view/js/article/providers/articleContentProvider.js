/**
 * Created by ManerFan on 2016/3/31 0031.
 */

define([
    "jquery",
    "jBoxUtil",
    'bootbox',
    "js/editor/extensions/echartsParser",
    "js/editor/extensions/mathJax",
    "js/article/providers/duoshuoProvider"
], function ($, jBoxUtil, bootbox, echartsParser, mathJax, duoshuo) {
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

    /**
     * 撤回到草稿箱
     */
    $(document).on("click", ".btn-article-withdraw", function () {
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
    $(document).on("click", ".btn-article-recycle", function () {
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

                $("a.article-list-pjax-href[href='/article']").trigger("click");
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    return articleContent;
});