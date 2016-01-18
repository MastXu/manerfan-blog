define([
    "jquery",
    "underscore",
    "crel",
    "mousetrap",
    "js/editor/utils",
    "js/editor/logger",
    "js/editor/classes/Extension",
    "js/editor/settings",
    "text!pages/editor/html/settingsExtensionsAccordion.html",
    "js/editor/extensions/yamlFrontMatterParser",
    "js/editor/extensions/markdownSectionParser",
    "js/editor/extensions/partialRendering",
    "js/editor/extensions/buttonMarkdownSyntax",
    "js/editor/extensions/googleAnalytics",
    "js/editor/extensions/twitter",
    "js/editor/extensions/dialogAbout",
    "js/editor/extensions/dialogManagePublication",
    "js/editor/extensions/dialogManageSynchronization",
    "js/editor/extensions/dialogManageSharing",
    "js/editor/extensions/dialogOpenHarddrive",
    "js/editor/extensions/documentTitle",
    "js/editor/extensions/documentSelector",
    "js/editor/extensions/documentPanel",
    "js/editor/extensions/documentManager",
    "js/editor/extensions/workingIndicator",
    "js/editor/extensions/notifications",
    "js/editor/extensions/umlDiagrams",
    "js/editor/extensions/markdownExtra",
    "js/editor/extensions/toc",
    "js/editor/extensions/mathJax",
    "js/editor/extensions/emailConverter",
    "js/editor/extensions/scrollSync",
    "js/editor/extensions/buttonSync",
    "js/editor/extensions/buttonPublish",
    "js/editor/extensions/buttonStat",
    "js/editor/extensions/buttonHtmlCode",
    "js/editor/extensions/buttonViewer",
    "js/editor/extensions/welcomeTour",
    "js/editor/extensions/shortcuts",
    "js/editor/extensions/userCustom",
    "js/editor/extensions/comments",
    "js/editor/extensions/findReplace",
    "js/editor/extensions/htmlSanitizer",
    "bootstrap",
    "jquery-waitforimages"
], function ($, _, crel, mousetrap, utils, logger, Extension, settings, settingsExtensionsAccordionHTML) {

    var eventMgr = {};

    // Create a list of extensions from module arguments
    // 检测此module的参数，将所有Extension类型的参数提取出来
    var extensionList = _.chain(arguments).map(function (argument) {
        return argument instanceof Extension && argument;
    }).compact().value();

    // Configure extensions
    var extensionSettings = settings.extensionSettings || {};
    _.each(extensionList, function (extension) {
        // Set the extension.config attribute from settings or default
        // configuration
        // 合并插件的配置 [浏览器缓存 > settings.extensionSettings > defaultConfig]
        extension.config = _.extend({}, extension.defaultConfig, extensionSettings[extension.extensionId]);
        if (window.viewerMode === true && extension.disableInViewer === true) {
            // Skip enabling the extension if we are in the viewer and extension
            // doesn't support it
            // 浏览模式下，插件定义了不在浏览模式下使用，则禁用
            extension.enabled = false;
        }
        else {
            // Enable the extension if it's not optional or it has not been
            // disabled by the user
            // 插件为必选或没有禁用此插件，则启用
            extension.enabled = !extension.isOptional || extension.config.enabled === undefined || extension.config.enabled === true;
        }
    });

    // Returns all listeners with the specified name that are implemented in the
    // enabled extensions
    // 在启用插件中，返回含有指定事件(名称)的监听器（事件回调）
    // 即，查找定义了指定事件回调函数的所有启用插件，并返回这些插件的该事件回调函数
    function getExtensionListenerList(eventName) {
        return _.chain(extensionList).map(function (extension) {
            return extension.enabled && extension[eventName];
        }).compact().value();
    }

    // Returns a function that calls every listeners with the specified name
    // from all enabled extensions
    // 查找所有含有指定事件的监听器（事件回调），并返回一个函数，使用此函数可批量调用所有此类监听器事件回调
    // 相当于使用观察者模式注册此事件（一个插件可以绑定多个事件）
    // 即，创建一类事件的回调执行函数，当调用此函数时，所有定义了此事件的可用插件，均会执行此事件回调
    var eventListenerListMap = {};

    function createEventHook(eventName) {
        // key: 事件名 value: 事件回调函数
        eventListenerListMap[eventName] = getExtensionListenerList(eventName);
        return function () {
            logger.log(eventName, arguments);
            var eventArguments = arguments;
            _.each(eventListenerListMap[eventName], function (listener) {
                // Use try/catch in case userCustom listener contains error
                try {
                    // 批量执行该事件回调，相当于 if (typeof listener == "function") { listener(eventArguments); }
                    listener.apply(null, eventArguments);
                }
                catch (e) {
                    console.error(_.isObject(e) ? e.stack : e);
                }
            });
        };
    }

    // Declare an event Hook in the eventMgr that we can fire using eventMgr.eventName()
    // 添加事件回调，key:事件名 value: 回调function
    function addEventHook(eventName) {
        eventMgr[eventName] = createEventHook(eventName);
    }

    // Used by external modules (not extensions) to listen to events
    // 添加监听器（事件回调）
    eventMgr.addListener = function (eventName, listener) {
        try {
            eventListenerListMap[eventName].push(listener);
        }
        catch (e) {
            console.error('No event listener called ' + eventName);
        }
    };

    // Call every onInit listeners (enabled extensions only)
    // 执行所有可用插件的onInit方法
    createEventHook("onInit")();

    // Load/Save extension config from/to settings
    // 加载配置的时候，设置插件的checkbox
    eventMgr.onLoadSettings = function () {
        logger.log("onLoadSettings");
        _.each(extensionList, function (extension) {
            var isChecked = !extension.isOptional || extension.config.enabled === undefined || extension.config.enabled === true;
            utils.setInputChecked("#input-enable-extension-" + extension.extensionId, isChecked);
            // Special case for Markdown Extra and MathJax
            if (extension.extensionId == 'markdownExtra') {
                utils.setInputChecked("#input-settings-markdown-extra", isChecked);
            }
            else if (extension.extensionId == 'mathJax') {
                utils.setInputChecked("#input-settings-mathjax", isChecked);
            }
            var onLoadSettingsListener = extension.onLoadSettings;
            onLoadSettingsListener && onLoadSettingsListener();
        });
    };
    // 保存配置的时候
    eventMgr.onSaveSettings = function (newExtensionSettings, event) {
        logger.log("onSaveSettings");
        _.each(extensionList, function (extension) {
            var newExtensionConfig = _.extend({}, extension.defaultConfig);
            newExtensionConfig.enabled = utils.getInputChecked("#input-enable-extension-" + extension.extensionId);
            var isChecked;
            // Special case for Markdown Extra and MathJax
            if (extension.extensionId == 'markdownExtra') {
                isChecked = utils.getInputChecked("#input-settings-markdown-extra");
                if (isChecked != extension.enabled) {
                    newExtensionConfig.enabled = isChecked;
                }
            }
            else if (extension.extensionId == 'mathJax') {
                isChecked = utils.getInputChecked("#input-settings-mathjax");
                if (isChecked != extension.enabled) {
                    newExtensionConfig.enabled = isChecked;
                }
            }
            var onSaveSettingsListener = extension.onSaveSettings;
            onSaveSettingsListener && onSaveSettingsListener(newExtensionConfig, event);
            newExtensionSettings[extension.extensionId] = newExtensionConfig;
        });
    };

    addEventHook("onMessage");
    addEventHook("onError");
    addEventHook("onOfflineChanged");
    addEventHook("onUserActive");
    addEventHook("onAsyncRunning");
    addEventHook("onPeriodicRun");

    // To access modules that are loaded after extensions
    addEventHook("onEditorCreated");
    addEventHook("onFileMgrCreated");
    addEventHook("onSynchronizerCreated");
    addEventHook("onPublisherCreated");
    addEventHook("onSharingCreated");
    addEventHook("onEventMgrCreated");

    // Operations on files
    addEventHook("onFileCreated");
    addEventHook("onFileDeleted");
    addEventHook("onFileSelected");
    addEventHook("onFileOpen");
    addEventHook("onFileClosed");
    addEventHook("onContentChanged");
    addEventHook("onTitleChanged");

    // Operations on folders
    addEventHook("onFoldersChanged");

    // Sync events
    addEventHook("onSyncRunning");
    addEventHook("onSyncSuccess");
    addEventHook("onSyncImportSuccess");
    addEventHook("onSyncExportSuccess");
    addEventHook("onSyncRemoved");

    // Publish events
    addEventHook("onPublishRunning");
    addEventHook("onPublishSuccess");
    addEventHook("onNewPublishSuccess");
    addEventHook("onPublishRemoved");

    // Operations on Layout
    addEventHook("onLayoutCreated");
    addEventHook("onLayoutResize");
    addEventHook("onExtensionButtonResize");

    // Operations on editor
    addEventHook("onPagedownConfigure");
    addEventHook("onSectionsCreated");
    addEventHook("onCursorCoordinates");
    addEventHook("onEditorPopover");

    // Operations on comments
    addEventHook("onDiscussionCreated");
    addEventHook("onDiscussionRemoved");
    addEventHook("onCommentsChanged");

    // Refresh twitter buttons
    addEventHook("onTweet");

    var onPreviewFinished = createEventHook("onPreviewFinished");
    var onAsyncPreviewListenerList = getExtensionListenerList("onAsyncPreview");
    var previewContentsElt;
    var $previewContentsElt;
    eventMgr.onAsyncPreview = function () {
        logger.log("onAsyncPreview");
        function recursiveCall(callbackList) {
            var callback = callbackList.length ? callbackList.shift() : function () {
                setTimeout(function () {
                    var html = "";
                    _.each(previewContentsElt.children, function (elt) {
                        if (!elt.exportableHtml) {
                            var clonedElt = elt.cloneNode(true);
                            _.each(clonedElt.querySelectorAll('.MathJax, .MathJax_Display, .MathJax_Preview'), function (elt) {
                                elt.parentNode.removeChild(elt);
                            });
                            elt.exportableHtml = clonedElt.innerHTML;
                        }
                        html += elt.exportableHtml;
                    });
                    var htmlWithComments = utils.trim(html);
                    var htmlWithoutComments = htmlWithComments.replace(/ <span class="comment label label-danger">.*?<\/span> /g, '');
                    onPreviewFinished(htmlWithComments, htmlWithoutComments);
                }, 10);
            };
            callback(function () {
                recursiveCall(callbackList);
            });
        }

        recursiveCall(onAsyncPreviewListenerList.concat([
            function (callback) {
                // We assume some images are loading asynchronously after the preview
                $previewContentsElt.waitForImages(callback);
            }
        ]));
    };


    // all the modules are loaded and the DOM is ready
    // Called in main.js
    var onReady = createEventHook("onReady");
    eventMgr.onReady = function () {
        previewContentsElt = document.getElementById('preview-contents');
        $previewContentsElt = $(previewContentsElt);

        // Create a button from an extension listener
        var createBtn = function (listener) {
            var buttonGrpElt = crel('div', {
                class: 'btn-group'
            });
            var btnElt = listener();
            if (_.isString(btnElt)) {
                buttonGrpElt.innerHTML = btnElt;
            }
            else if (_.isElement(btnElt)) {
                buttonGrpElt.appendChild(btnElt);
            }
            return buttonGrpElt;
        };

        if (window.viewerMode === false) {
            // Create accordion in settings dialog
            var accordionHtml = _.chain(extensionList).sortBy(function (extension) {
                return extension.extensionName.toLowerCase();
            }).reduce(function (html, extension) {
                return html + (extension.settingsBlock ? _.template(settingsExtensionsAccordionHTML, {
                        extensionId: extension.extensionId,
                        extensionName: extension.extensionName,
                        isOptional: extension.isOptional,
                        settingsBlock: extension.settingsBlock
                    }) : "");
            }, "").value();
            document.querySelector('.accordion-extensions').innerHTML = accordionHtml;

            // Create extension buttons
            logger.log("onCreateButton");
            var onCreateButtonListenerList = getExtensionListenerList("onCreateButton");
            var extensionButtonsFragment = document.createDocumentFragment();
            _.each(onCreateButtonListenerList, function (listener) {
                extensionButtonsFragment.appendChild(createBtn(listener));
            });
            document.querySelector('.extension-buttons').appendChild(extensionButtonsFragment);
        }

        // Create extension preview buttons
        logger.log("onCreatePreviewButton");
        var onCreatePreviewButtonListenerList = getExtensionListenerList("onCreatePreviewButton");
        var extensionPreviewButtonsFragment = document.createDocumentFragment();
        _.each(onCreatePreviewButtonListenerList, function (listener) {
            extensionPreviewButtonsFragment.appendChild(createBtn(listener));
        });
        var previewButtonsElt = document.querySelector('.extension-preview-buttons');
        previewButtonsElt.appendChild(extensionPreviewButtonsFragment);

        // Shall close every popover
        // 当按下esc时，关闭所有弹窗
        mousetrap.bind('escape', function () {
            eventMgr.onEditorPopover();
        });

        // Call onReady listeners
        onReady();
    };

    // For extensions that need to call other extensions
    eventMgr.onEventMgrCreated(eventMgr);
    return eventMgr;
});
