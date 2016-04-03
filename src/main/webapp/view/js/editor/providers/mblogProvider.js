/**
 * Created by ManerFan on 2016/3/31 0031.
 */

define([
    "js/editor/utils",
    "js/editor/classes/Provider",
    "js/editor/settings",
    "js/editor/eventMgr",
    "js/editor/helpers/mblogHelper",
    "js/editor/fileMgr",
    "jBoxUtil",
    "tagsinput"
], function (utils, Provider, settings, eventMgr, mblogHelper, fileMgr, jBoxUtil) {
    var mblogProvider = new Provider("mblog", "ManerFanBlog");

    var catalogLimit = 5;
    var currCatalogNum = 0;
    $(".tagsinput").tagsInput({
        onAddTag: function (key) {
            currCatalogNum++;
            if (currCatalogNum >= catalogLimit) {
                // 最多允许添加5个分类
                $(".tagsinput-add-container input").hide();
            }
            if (currCatalogNum > catalogLimit) {
                $(".tagsinput").removeTag(key);
            }
        },
        onRemoveTag: function () {
            currCatalogNum--;
            if (currCatalogNum < catalogLimit) {
                $(".tagsinput-add-container input").show();
            }
        }
    });

    $(".tagsinput-add-container input").attr("maxlength", 10); // 分类最多10个字符

    var currentFileDesc;
    eventMgr.addListener("onFileSelected", function (fileDesc) {
        currentFileDesc = fileDesc;
    });

    var currentContent;
    eventMgr.addListener("onPreviewFinished", function (htmlWithComments, htmlWithoutComments) {
        currentContent = {
            withComments: htmlWithComments,
            withoutComments: htmlWithoutComments,
            text: $("#preview-contents").text()
        };
    });

    /**
     * 点击文章设置
     */
    $(".btn-blog-setting").click(function () {
        utils.resetModalInputs();

        var categories = currentFileDesc.categories; // 分类
        $(".tagsinput").importTags(categories.join(","));

        var summary = currentFileDesc.summary; // 摘要
        if ($.trim(summary).length < 1) {
            // 没有设置摘要，则自动生成摘要
            summary = $.trim(currentContent.text).replace(/[\r\n]+/gm, "\n")/*删除空行*/.slice(0, 210);
        }
        $(".blog-summary").val(summary);

        $(".modal-blog-setting").modal();
    });

    /**
     * 添加常用分类
     */
    $(".catalog").click(function () {
        $(".tagsinput").addTag($(this).text(), {unique: true});
    });

    /**
     * 点击设置
     */
    $(".action-blog-setting").click(function () {
        var summary = $(".blog-summary").val();
        var categories = $(".tagsinput").val().split(",");
        if (summary.length < 1) {
            jBoxUtil.noticeError({content: "摘要不能为空"});
            return;
        }

        if (categories.length < 1 || (categories.length == 1 && categories[0].length < 1)) {
            jBoxUtil.noticeError({content: "分类不能为空"});
            return;
        }

        currentFileDesc.summary = summary;
        currentFileDesc.categories = categories;

        $(".modal-blog-setting").modal("hide");
    });

    $(".modal-blog-setting").on("show.bs.modal", function () {
        currCatalogNum = 0;
        $(".tagsinput-add-container input").show();
        $(".blog-summary").focus();
    });

    var blogSettingCallback = null;
    $(".modal-blog-setting").on("hidden.bs.modal", function () {
        // 回调
        if (_.isFunction(blogSettingCallback)) {
            var callback = blogSettingCallback;
            blogSettingCallback = null;
            callback();
        }
    });

    /**
     * 内容变化时触发
     */
    eventMgr.addListener('onContentChanged', function () {
        $(".btn-blog-save").addClass("changed");
    });

    /**
     * 保存文章到草稿
     */
    $(".btn-blog-save").click(function () {

    });

    /**
     * 发布文章
     */
    $(".btn-blog-publish").click(function () {

    });

    return mblogProvider;
});