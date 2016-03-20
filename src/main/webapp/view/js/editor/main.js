// Check browser compatibility
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
window.viewerMode = false; // /(^| )viewer($| )/.test(document.body.className);

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
    "rangy-cssclassapplier"
], function ($, rangy, core, eventMgr) {

    if (window.noStart) {
        return;
    }

    $(function () {
        rangy.init();

        // Here, all the modules are loaded and the DOM is ready
        core.onReady();

        // If browser has detected a new application cache.
        if (window.applicationCache) {
            window.applicationCache.addEventListener('updateready', function () {
                if (window.applicationCache.status === window.applicationCache.UPDATEREADY) {
                    window.applicationCache.swapCache();
                    eventMgr.onMessage('New version available!\nJust refresh the page to upgrade.');
                }
            }, false);
        }
    });
});
