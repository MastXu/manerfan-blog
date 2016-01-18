// Check browser compatibility
// 测试浏览器是否支持某些特性
try {
    var test = 'seLocalStorageCheck';
    localStorage.setItem(test, test);
    localStorage.removeItem(test);
    var obj = {};
    Object.defineProperty(obj, 'prop', {
        get: function () {
        },
        set: function () {
        }
    });
}
catch (e) {
    alert('Your browser is not supported, sorry!');
    throw e;
}

// Viewer mode is deduced from the body class
// 判断当前是不是view模式 (/view)
/*window.viewerMode = /(^| )viewer($| )/.test(document.body.className);*/

// Keep the theme in a global variable
var themeModule = "less!style/editor/themes/default";
if (!!window.debug) {
    themeModule = "css!style/editor/default";
}

// RequireJS entry point. By requiring synchronizer, publisher, sharing and
// media-importer, we are actually loading all the modules
require([
    "jquery",
    "rangy",
    "js/editor/core",
    "js/editor/eventMgr",
    "js/editor/synchronizer",
    "js/editor/publisher",
    "js/editor/sharing",
    "js/editor/mediaImporter",
    "css",
    "rangy-cssclassapplier",
    themeModule
], function ($, rangy, core, eventMgr) {

    if (window.noStart) {
        return;
    }

    $(function () {
        rangy.init();

        // Here, all the modules are loaded and the DOM is ready
        core.onReady();

        // If browser has detected a new application cache.
        // 添加浏览器缓存更新的事件，当缓存更新后，提示用户
        if (window.applicationCache) {
            window.applicationCache.addEventListener('updateready', function () {
                if (window.applicationCache.status === window.applicationCache.UPDATEREADY) {
                    window.applicationCache.swapCache(); // 刷新缓存
                    eventMgr.onMessage('New version available!\nJust refresh the page to upgrade.');
                }
            }, false);
        }
    });

});
