// Setup an empty localStorage or upgrade an existing one
define([
    "underscore"
], function (_) {

    /**
     * 每次进入页面，都清空localstorage
     */
    /* 不需要清理的值 */
    var unclear = ["welcomeTour"];
    _.each(_.keys(localStorage), function (key) {
        if (!_.contains(unclear, key)) {
            localStorage.removeItem(key);
        }
    });

    return localStorage;
});
