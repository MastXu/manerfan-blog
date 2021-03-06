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
    "jBoxUtil",
    "text!pages/editor/html/bodyEditor.html",
    "text!pages/editor/html/bodyViewer.html",
    "text!pages/editor/html/tooltipSettingsTemplate.html",
    "text!pages/editor/html/tooltipSettingsPdfOptions.html",
    "js/editor/storage",
    'pagedown',
    'jqueryfileupload',
], function ($, _, crel, editor, layout, constants, utils, storage, settings,
             eventMgr, jBoxUtil, bodyEditorHTML, bodyViewerHTML,
             settingsTemplateTooltipHTML, settingsPdfOptionsTooltipHTML) {

    var core = {};

    // Used for periodic tasks
    var intervalId;

    // Used to detect user activity
    var isUserReal = false;
    var userActive = false;
    var windowUnique = true;
    var userLastActivity = 0;

    function setUserActive() {
        isUserReal = true;
        userActive = true;
        var currentTime = utils.currentTime;
        if (currentTime > userLastActivity + 1000) {
            userLastActivity = currentTime;
            eventMgr.onUserActive();
        }
    }

    function isUserActive() {
        if (utils.currentTime - userLastActivity > constants.USER_IDLE_THRESHOLD) {
            userActive = false;
        }
        return userActive && windowUnique;
    }

    // Used to only have 1 window of the application in the same browser
    var windowId;

    function checkWindowUnique() {
        if (isUserReal === false || windowUnique === false) {
            return;
        }
        if (windowId === undefined) {
            windowId = utils.id();
            storage[constants.FRONT_WINDOWID] = windowId;
        }
        var frontWindowId = storage[constants.FRONT_WINDOWID];
        if (frontWindowId != windowId) {
            windowUnique = false;
            if (intervalId !== undefined) {
                clearInterval(intervalId);
            }
            $(".modal").modal("hide");
            $('.modal-non-unique').modal("show");
            // Attempt to close the window
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

    function checkOnline() {
        // Try to reconnect if we are offline but we have some network
        if (isOffline === true && navigator.onLine === true && offlineTime + constants.CHECK_ONLINE_PERIOD < utils.currentTime) {
            offlineTime = utils.currentTime;
            // Try to download anything to test the connection
            $.ajax({
                url: "//www.google.com/jsapi",
                timeout: constants.AJAX_TIMEOUT,
                dataType: "script"
            }).done(function () {
                setOnline();
            });
        }
    }

    function loadSettings() {

        // Layout orientation
        utils.setInputRadio("radio-layout-orientation", settings.layoutOrientation);
        // Lazy rendering
        utils.setInputChecked("#input-settings-lazy-rendering", settings.lazyRendering);
        // Editor font class
        utils.setInputRadio("radio-settings-editor-font-class", settings.editorFontClass);
        // Font size ratio
        utils.setInputValue("#input-settings-font-size", settings.fontSizeRatio);
        // Max width ratio
        utils.setInputValue("#input-settings-max-width", settings.maxWidthRatio);
        // Cursor locking ratio
        utils.setInputValue("#input-settings-cursor-focus", settings.cursorFocusRatio);
        // Default content
        utils.setInputValue("#textarea-settings-default-content", settings.defaultContent);
        // Edit mode
        utils.setInputRadio("radio-settings-edit-mode", settings.editMode);
        // Commit message
        utils.setInputValue("#input-settings-publish-commit-msg", settings.commitMsg);
        // Markdown MIME type
        utils.setInputValue("#input-settings-markdown-mime-type", settings.markdownMimeType);
        // Gdrive multi-accounts
        utils.setInputValue("#input-settings-gdrive-multiaccount", settings.gdriveMultiAccount);
        // Gdrive full access
        utils.setInputChecked("#input-settings-gdrive-full-access", settings.gdriveFullAccess);
        // Dropbox full access
        utils.setInputChecked("#input-settings-dropbox-full-access", settings.dropboxFullAccess);
        // GitHub full access
        utils.setInputChecked("#input-settings-github-full-access", settings.githubFullAccess);
        // Template
        utils.setInputValue("#textarea-settings-publish-template", settings.template);
        // PDF template
        utils.setInputValue("#textarea-settings-pdf-template", settings.pdfTemplate);
        // PDF options
        utils.setInputValue("#textarea-settings-pdf-options", settings.pdfOptions);
        // CouchDB URL
        utils.setInputValue("#input-settings-couchdb-url", settings.couchdbUrl);

        // Load extension settings
        eventMgr.onLoadSettings();
    }

    // Save settings from settings dialog
    function saveSettings(event) {
        var newSettings = {};

        // Layout orientation
        newSettings.layoutOrientation = utils.getInputRadio("radio-layout-orientation");
        // Lazy Rendering
        newSettings.lazyRendering = utils.getInputChecked("#input-settings-lazy-rendering");
        // Editor font class
        newSettings.editorFontClass = utils.getInputRadio("radio-settings-editor-font-class");
        // Font size ratio
        newSettings.fontSizeRatio = utils.getInputFloatValue("#input-settings-font-size", event, 0.1, 10);
        // Max width ratio
        newSettings.maxWidthRatio = utils.getInputFloatValue("#input-settings-max-width", event, 0.1, 10);
        // Cursor locking ratio
        newSettings.cursorFocusRatio = utils.getInputFloatValue("#input-settings-cursor-focus", event, 0, 1);
        // Default content
        newSettings.defaultContent = utils.getInputValue("#textarea-settings-default-content");
        // Edit mode
        newSettings.editMode = utils.getInputRadio("radio-settings-edit-mode");
        // Commit message
        newSettings.commitMsg = utils.getInputTextValue("#input-settings-publish-commit-msg", event);
        // Gdrive multi-accounts
        newSettings.gdriveMultiAccount = utils.getInputIntValue("#input-settings-gdrive-multiaccount");
        // Markdown MIME type
        newSettings.markdownMimeType = utils.getInputValue("#input-settings-markdown-mime-type");
        // Gdrive full access
        newSettings.gdriveFullAccess = utils.getInputChecked("#input-settings-gdrive-full-access");
        // Drobox full access
        newSettings.dropboxFullAccess = utils.getInputChecked("#input-settings-dropbox-full-access");
        // GitHub full access
        newSettings.githubFullAccess = utils.getInputChecked("#input-settings-github-full-access");
        // Template
        newSettings.template = utils.getInputTextValue("#textarea-settings-publish-template", event);
        // PDF template
        newSettings.pdfTemplate = utils.getInputTextValue("#textarea-settings-pdf-template", event);
        // PDF options
        newSettings.pdfOptions = utils.getInputJSONValue("#textarea-settings-pdf-options", event);
        // CouchDB URL
        newSettings.couchdbUrl = utils.getInputValue("#input-settings-couchdb-url", event);

        // Save extension settings
        newSettings.extensionSettings = {};
        eventMgr.onSaveSettings(newSettings.extensionSettings, event);

        if (!event.isPropagationStopped()) {
            if (settings.dropboxFullAccess !== newSettings.dropboxFullAccess) {
                storage.removeItem('dropbox.lastChangeId');
            }
            $.extend(settings, newSettings);
            storage.settings = JSON.stringify(settings);
        }
    }

    // Create the PageDown editor
    var pagedownEditor;
    var fileDesc;
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
        $("#wmd-bold-button").append($('<i class="csdn-icon-bold">')).appendTo($btnGroupElt);
        $("#wmd-italic-button").append($('<i class="csdn-icon-italic">')).appendTo($btnGroupElt);
        $btnGroupElt = $('.wmd-button-group2');
        $("#wmd-link-button").append($('<i class="csdn-icon-earth">')).appendTo($btnGroupElt);
        $("#wmd-quote-button").append($('<i class="csdn-icon-quote">')).appendTo($btnGroupElt);
        $("#wmd-code-button").append($('<i class="csdn-icon-code">')).appendTo($btnGroupElt);
        $("#wmd-image-button").append($('<i class="csdn-icon-picture">')).appendTo($btnGroupElt);
        $btnGroupElt = $('.wmd-button-group3');
        $("#wmd-olist-button").append($('<i class="csdn-icon-list-ordered">')).appendTo($btnGroupElt);
        $("#wmd-ulist-button").append($('<i class="csdn-icon-list-unordered">')).appendTo($btnGroupElt);
        $("#wmd-heading-button").append($('<i class="csdn-icon-heading">')).appendTo($btnGroupElt);
        $("#wmd-hr-button").append($('<i class="csdn-icon-line">')).appendTo($btnGroupElt);
        $btnGroupElt = $('.wmd-button-group4');
        $("#wmd-undo-button").append($('<i class="csdn-icon-undo">')).appendTo($btnGroupElt);
        $("#wmd-redo-button").append($('<i class="csdn-icon-redo">')).appendTo($btnGroupElt);
    };

    // Initialize multiple things and then fire eventMgr.onReady
    core.onReady = function () {
        // Add RTL class
        document.body.className += ' ' + settings.editMode;

        /*if (window.viewerMode === true) {
         document.body.innerHTML = bodyViewerHTML;
         }
         else {
         document.body.innerHTML = bodyEditorHTML;
         }*/

        // Initialize utils library
        utils.init();

        // listen to online/offline events
        $(window).on('offline', core.setOffline);
        $(window).on('online', setOnline);
        if (navigator.onLine === false) {
            core.setOffline();
        }

        // Detect user activity
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
            // Focus on the editor when modal is gone
            editor.focus();
        }).on('keypress', '.modal', function (e) {
            // Handle enter key in modals
            if (e.which == 13 && !$(e.target).is("textarea")) {
                $(this).find(".modal-footer a:last").click();
            }
        });

        // Click events on "insert link" and "insert image" dialog buttons
        $(".action-insert-link").click(function (e) {
            var value = utils.getInputTextValue($("#input-insert-link"), e);
            if (value !== undefined) {
                core.insertLinkCallback(value);
                core.insertLinkCallback = undefined;
            }
        });
        $(".action-insert-image").click(function (e) {
            var value = utils.getInputTextValue($("#input-insert-image"), e);
            if (value !== undefined) {
                core.insertLinkCallback(value);
                core.insertLinkCallback = undefined;
            }
        });

        // 上传图片
        $("#imageupload").fileupload({
            url: settings.imageUploadURL,
            /*type: "post",*/
            dataType: "json",
            /*dropZone: $("#imageupload"),*/
            singleFileUploads: false,
            /*autoUpload: false,*/
            /*acceptFileTypes: settings.acceptFileTypes,*/
            add: function (e, data) {
                var accept = true;
                $(data.files).each(function (index, file) {
                    if (!settings.acceptImageTypes.test(file.name)) {
                        accept = false;
                        return false;
                    }
                });

                if (!accept) {
                    return;
                }

                var reader = new FileReader();
                reader.onload = function (e) {
                    $("#preview").attr('src', e.target.result);
                }
                reader.readAsDataURL(data.files[0]);

                $("#progress").text("0%");
                $("#progress-bar").css("width", "0%");

                $(".before-add").hide();
                $(".after-add").show();

                // 点击上传
                $("#upload-image").one("click", function () {
                    $(".file-progress").show();
                    $("#upload-image").hide();

                    jqXHR = data.submit()
                        .success(function (_result, _textStatus, _jqXHR) {
                            if (null != _result.errmsg) {
                                resetImageModalLayout();
                                jBoxUtil.noticeError({content: _result.errmsg});
                            } else {
                                utils.setInputValue($("#input-insert-image"), _result.url);
                                $(".action-insert-image").trigger("click");
                            }
                        })
                        .error(function (_jqXHR, _textStatus, _errorThrown) {
                        })
                        .complete(function (_result, _textStatus, _jqXHR) {
                            jqXHR = null;
                            $("#progress").text("");
                            $("#progress-bar").css("width", "0%");
                            $("#importModal").modal("hide");
                        });
                });
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $("#progress").text(progress + "%");
                $("#progress-bar").css("width", progress + "%");

                if (progress == 100) {
                    if (typeof settings.onBefore == "function") {
                        settings.onBefore();
                    }
                }
            }
        });

        // 插入链接|图片模态框隐藏时
        // Hide events on "insert link" and "insert image" dialogs
        $(".modal-insert-link, .modal-insert-image").on('hidden.bs.modal', function () {
            if (core.insertLinkCallback !== undefined) {
                core.insertLinkCallback(null);
                core.insertLinkCallback = undefined;
            }
        });

        // 插入图片模态框显示时
        $(".modal-insert-image").on("shown.bs.modal", function () {
            resetImageModalLayout();
        });

        // 重置插入图片布局
        function resetImageModalLayout() {
            // 插入图片 显示拖拽栏，隐藏进度条
            $(".before-add").show();
            $(".after-add").hide();

            $(".file-progress").hide();
            $("#upload-image").show();
        }

        // Settings loading/saving
        $(".action-load-settings").click(function () {
            loadSettings();
        });
        $(".action-apply-settings").click(function (e) {
            saveSettings(e);
            if (!e.isPropagationStopped()) {
                window.location.reload();
            }
        });
        $('.action-add-google-drive-account').click(function () {
            if (settings.gdriveMultiAccount === 3) {
                return;
            }
            settings.gdriveMultiAccount++;
            storage.settings = JSON.stringify(settings);
            window.location.reload();
        });

        // Import docs and settings
        $(".action-import-docs-settings").click(function () {
            $("#input-file-import-docs-settings").click();
        });
        var newstorage;
        $("#input-file-import-docs-settings").change(function (evt) {
            var files = (evt.dataTransfer || evt.target).files;
            $(".modal-settings").modal("hide");
            _.each(files, function (file) {
                var reader = new FileReader();
                reader.onload = (function (importedFile) {
                    return function (e) {
                        try {
                            newstorage = JSON.parse(e.target.result);
                            // Compare storage version
                            var newVersion = parseInt(newstorage.version.match(/^v(\d+)$/)[1], 10);
                            var currentVersion = parseInt(storage.version.match(/^v(\d+)$/)[1], 10);
                            if (newVersion > currentVersion) {
                                // We manage storage upgrade, not downgrade
                                eventMgr.onError("Incompatible version. Please upgrade StackEdit.");
                            } else {
                                $('.modal-import-docs-settings').modal('show');
                            }
                        }
                        catch (exc) {
                            eventMgr.onError("Wrong format: " + importedFile.name);
                        }
                        $("#input-file-import-docs-settings").val('');
                    };
                })(file);
                reader.readAsText(file);
            });
        });
        $(".action-import-docs-settings-confirm").click(function () {
            storage.clear();
            var allowedKeys = /^file\.|^folder\.|^publish\.|^settings$|^sync\.|^google\.|^author\.|^version$/;
            _.each(newstorage, function (value, key) {
                if (allowedKeys.test(key)) {
                    storage[key] = value;
                }
            });
            window.location.reload();
        });
        // Export settings
        $(".action-export-docs-settings").click(function () {
            utils.saveAs(JSON.stringify(storage), "StackEdit local storage.json");
        });

        $(".action-default-settings").click(function () {
            storage.removeItem("settings");
            storage.removeItem("theme");
            if (!settings.dropboxFullAccess) {
                storage.removeItem('dropbox.lastChangeId');
            }
            window.location.reload();
        });

        $(".action-app-reset").click(function () {
            storage.clear();
            window.location.reload();
        });

        // Reset inputs
        $(".action-reset-input").click(function () {
            utils.resetModalInputs();
        });

        utils.createTooltip(".tooltip-lazy-rendering", 'Disable preview rendering while typing in order to offload CPU. Refresh preview after 500 ms of inactivity.');
        utils.createTooltip(".tooltip-default-content", [
            'Thanks for supporting StackEdit by adding a backlink in your documents!<br/><br/>',
            '<b class="text-danger">NOTE: Backlinks in Stack Exchange Q/A are not welcome.</b>'
        ].join(''));
        utils.createTooltip(".tooltip-template", settingsTemplateTooltipHTML);
        utils.createTooltip(".tooltip-pdf-options", settingsPdfOptionsTooltipHTML);

        // Avoid dropdown panels to close on click
        $("div.dropdown-menu").click(function (e) {
            // e.stopPropagation();
        });

        // Non unique window dialog
        $('.modal-non-unique').modal({
            backdrop: "static",
            keyboard: false,
            show: false
        });

        // Load images
        _.each(document.querySelectorAll('img'), function (imgElt) {
            var $imgElt = $(imgElt);
            var src = $imgElt.data('stackeditSrc');
            if (src) {
                $imgElt.attr('src', window.baseDir + '/img/' + src);
            }
        });
    });

    return core;
});
