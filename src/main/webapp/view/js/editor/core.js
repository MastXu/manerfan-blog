/*globals Markdown, requirejs */
define([
    "jquery",
    "underscore",
    "crel",
    "js/editor/editor",
    "js/editor/layout",
    "js/editor/constants",
    "js/editor/utils",
    "js/editor/storage",
    "js/editor/settings",
    "js/editor/eventMgr",
    "js/editor/storage",
    'pagedown'
], function ($, _, crel, editor, layout, constants, utils, storage, settings, eventMgr) {

    var core = {};

    // Used for periodic tasks
    var intervalId;

    // Used to detect user activity
    var isUserReal = false;
    var userActive = false; // 用户是否激活  false 用户已超时或在同一个浏览器中打开多于一个编辑窗口
    var windowUnique = true; // 同一个浏览器中，是否只打开一个编辑窗口
    var userLastActivity = 0; // 用户最近一次操作时间

    function setUserActive() {
        isUserReal = true;
        userActive = true;
        var currentTime = utils.currentTime;
        if (currentTime > userLastActivity + 1000) {
            userLastActivity = currentTime;
            eventMgr.onUserActive();
        }
    }

    /**
     * 判断用户是否激活
     * 若已超时或在同一个浏览器中打开多于一个编辑窗口，则返回false
     * @returns {boolean}
     */
    function isUserActive() {
        if (utils.currentTime - userLastActivity > constants.USER_IDLE_THRESHOLD) {
            userActive = false;
        }
        return userActive && windowUnique;
    }

    // Used to only have 1 window of the application in the same browser
    var windowId;

    /**
     * 检测是否在同一个浏览器中只打开一个编辑窗口
     */
    function checkWindowUnique() {
        if (isUserReal === false || windowUnique === false) {
            return;
        }
        if (windowId === undefined) {
            // 若windowId不存在，则随机生成一个
            windowId = utils.id(); // 生成随机字符串
            storage.frontWindowId = windowId;
        }
        var frontWindowId = storage.frontWindowId;
        if (frontWindowId != windowId) {
            // 若windowId与浏览器缓存Id不一致，则说明在同一个浏览器中打开多于一个编辑窗口
            windowUnique = false;
            if (intervalId !== undefined) {
                // 关闭周期任务
                clearInterval(intervalId);
            }
            // 打开提示
            $(".modal").modal("hide");
            $('.modal-non-unique').modal("show");
            // Attempt to close the window
            // 尝试管理当前窗口
            window.close();
        }
    }

    // Offline management
    var isOffline = false;
    var offlineTime = utils.currentTime;
    core.setOffline = function () {
        offlineTime = utils.currentTime;
        if (isOffline === false) {
            isOffline = true;
            eventMgr.onOfflineChanged(true);
        }
    };
    function setOnline() {
        if (isOffline === true) {
            isOffline = false;
            eventMgr.onOfflineChanged(false);
        }
    }

    /**
     * 检测是否离线
     */
    function checkOnline() {
        // Try to reconnect if we are offline but we have some network
        if (isOffline === true && navigator.onLine === true && offlineTime + constants.CHECK_ONLINE_PERIOD < utils.currentTime) {
            offlineTime = utils.currentTime;
            // Try to download anything to test the connection
            // 随便下载一个啥，若成功了则说明网络是好的
            $.ajax({
                url: "//code.jquery.com/jquery-1.11.0.min.js",
                timeout: constants.AJAX_TIMEOUT,
                dataType: "script"
            }).done(function () {
                setOnline();
            });
        }
    }

    // Load settings in settings dialog
    var $themeInputElt;

    // Create the PageDown editor
    var pagedownEditor;
    var fileDesc;

    // 加载文章，初始化各种插件
    core.initEditor = function (fileDescParam) {
        if (fileDesc !== undefined) {
            eventMgr.onFileClosed(fileDesc);
        }
        fileDesc = fileDescParam;

        if (pagedownEditor !== undefined) {
            // If the editor is already created
            editor.undoMgr.init();
            return pagedownEditor.uiManager.setUndoRedoButtonStates();
        }

        // Create the converter and the editor
        var converter = new Markdown.Converter();
        var options = {
            _DoItalicsAndBold: function (text) {
                // Restore original markdown implementation
                text = text.replace(/(\*\*|__)(?=\S)(.+?[*_]*)(?=\S)\1/g,
                    "<strong>$2</strong>");
                text = text.replace(/(\*|_)(?=\S)(.+?)(?=\S)\1/g,
                    "<em>$2</em>");
                return text;
            }
        };
        converter.setOptions(options);
        pagedownEditor = new Markdown.Editor(converter, undefined, {
            undoManager: editor.undoMgr
        });

        // Custom insert link dialog
        pagedownEditor.hooks.set("insertLinkDialog", function (callback) {
            core.insertLinkCallback = callback;
            utils.resetModalInputs();
            $(".modal-insert-link").modal();
            return true;
        });
        // Custom insert image dialog
        pagedownEditor.hooks.set("insertImageDialog", function (callback) {
            core.insertLinkCallback = callback;
            if (core.catchModal) {
                return true;
            }
            utils.resetModalInputs();
            $(".modal-insert-image").modal();
            return true;
        });

        eventMgr.onPagedownConfigure(pagedownEditor);
        pagedownEditor.hooks.chain("onPreviewRefresh", eventMgr.onAsyncPreview);
        pagedownEditor.run();
        editor.undoMgr.init();

        // Hide default buttons
        $(".wmd-button-row li").addClass("btn btn-success").css("left", 0).find("span").hide();

        // Add customized buttons
        var $btnGroupElt = $('.wmd-button-group1');
        $("#wmd-bold-button").append($('<i class="icon-bold">')).appendTo($btnGroupElt);
        $("#wmd-italic-button").append($('<i class="icon-italic">')).appendTo($btnGroupElt);
        $btnGroupElt = $('.wmd-button-group2');
        $("#wmd-link-button").append($('<i class="icon-globe">')).appendTo($btnGroupElt);
        $("#wmd-quote-button").append($('<i class="icon-indent-right">')).appendTo($btnGroupElt);
        $("#wmd-code-button").append($('<i class="icon-code">')).appendTo($btnGroupElt);
        $("#wmd-image-button").append($('<i class="icon-picture">')).appendTo($btnGroupElt);
        $btnGroupElt = $('.wmd-button-group3');
        $("#wmd-olist-button").append($('<i class="icon-list-numbered">')).appendTo($btnGroupElt);
        $("#wmd-ulist-button").append($('<i class="icon-list-bullet">')).appendTo($btnGroupElt);
        $("#wmd-heading-button").append($('<i class="icon-text-height">')).appendTo($btnGroupElt);
        $("#wmd-hr-button").append($('<i class="icon-ellipsis">')).appendTo($btnGroupElt);
        $btnGroupElt = $('.wmd-button-group5');
        $("#wmd-undo-button").append($('<i class="icon-reply">')).appendTo($btnGroupElt);
        $("#wmd-redo-button").append($('<i class="icon-forward">')).appendTo($btnGroupElt);
    };

    // 总入口
    // Initialize multiple things and then fire eventMgr.onReady
    core.onReady = function () {
        // Add RTL class
        document.body.className += ' ' + settings.editMode;

        /*if (window.viewerMode === true) {
         $("body").prepend(bodyViewerHTML);
         }
         else {
         $("body").prepend(bodyEditorHTML);
         }*/

        // Initialize utils library
        utils.init();

        // listen to online/offline events
        // 浏览器离线触发
        $(window).on('offline', core.setOffline);
        // 浏览器上线触发
        $(window).on('online', setOnline);
        if (navigator.onLine === false) {
            core.setOffline();
        }

        // Detect user activity
        // 有鼠标或者键盘操作，均更新用户active状态
        $(document).mousemove(setUserActive).keypress(setUserActive);

        layout.init();
        editor.init();

        // Do periodic tasks
        intervalId = window.setInterval(function () {
            utils.updateCurrentTime();
            checkWindowUnique();
            if (isUserActive() === true || window.viewerMode === true) {
                eventMgr.onPeriodicRun();
                checkOnline();
            }
        }, 1000);

        eventMgr.onReady();
    };

    // Other initialization that are not prioritary
    eventMgr.addListener("onReady", function () {

        $(document.body).on('shown.bs.modal', '.modal', function () {
            // 当modal弹出时，查找第一个button或input，并focus
            var $elt = $(this);
            setTimeout(function () {
                // When modal opens focus on the first button
                $elt.find('.btn:first').focus();
                // Or on the first link if any
                $elt.find('button:first').focus();
                // Or on the first input if any
                $elt.find("input:enabled:visible:first").focus();
            }, 50);
        }).on('hidden.bs.modal', '.modal', function () {
            // 当modal关闭时，editor focus，并应用主题
            // Focus on the editor when modal is gone
            editor.focus();
            // Revert to current theme when settings modal is closed
            applyTheme(window.theme);
        }).on('keypress', '.modal', function (e) {
            // 在modal中的textarea中点击回车时，触发页脚最后一个a标签的click
            // Handle enter key in modals
            if (e.which == 13 && !$(e.target).is("textarea")) {
                $(this).find(".modal-footer a:last").click();
            }
        });

        // Click events on "insert link" and "insert image" dialog buttons
        $(".action-insert-link").click(function (e) {
            // 插入链接
            var value = utils.getInputTextValue($("#input-insert-link"), e);
            if (value !== undefined) {
                // 见pagedownEditor初始化
                core.insertLinkCallback(value);
                core.insertLinkCallback = undefined;
            }
        });
        $(".action-insert-image").click(function (e) {
            // 插入图片
            var value = utils.getInputTextValue($("#input-insert-image"), e);
            if (value !== undefined) {
                // 见pagedownEditor初始化
                core.insertLinkCallback(value);
                core.insertLinkCallback = undefined;
            }
        });

        // Hide events on "insert link" and "insert image" dialogs
        $(".modal-insert-link, .modal-insert-image").on('hidden.bs.modal', function () {
            if (core.insertLinkCallback !== undefined) {
                // 见pagedownEditor初始化
                core.insertLinkCallback(null);
                core.insertLinkCallback = undefined;
            }
        });

        // Hot theme switcher in the settings
        var currentTheme = window.theme;

        function applyTheme(theme) {
            theme = theme || 'default';
            if (currentTheme != theme) {
                var themeModule = "css!style/themes/" + theme;
                // Undefine the module in RequireJS
                requirejs.undef(themeModule);
                // Then reload the style
                require([
                    themeModule
                ]);
                currentTheme = theme;
            }
        }

        $(".action-app-reset").click(function () {
            storage.clear();
            window.location.reload();
        });

        // Avoid dropdown panels to close on click
        $("div.dropdown-menu").click(function (e) {
            e.stopPropagation();
        });

        // Non unique window dialog
        $('.modal-non-unique').modal({
            backdrop: "static",
            keyboard: false,
            show: false
        });

        // Load images
        /*_.each(document.querySelectorAll('img'), function (imgElt) {
         var $imgElt = $(imgElt);
         var src = $imgElt.data('stackeditSrc');
         if (src) {
         $imgElt.attr('src', window.baseDir + '/img/' + src);
         }
         });*/

    });

    return core;
});
