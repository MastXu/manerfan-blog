// Setup an empty localStorage or upgrade an existing one
define([
    "underscore",
    "js/editor/constants"
], function (_, constants) {

    var publishKey = constants.TEMPORARY_FILE_INDEX + ".publish";

    /**
     * 每次进入页面，都清空[constants.TEMPORARY_FILE_INDEX].*
     */
    _.each(_.keys(localStorage), function (key) {
        if (startWith(key, constants.TEMPORARY_FILE_INDEX)) {
            if (publishKey == key) {
                _.each(retrieveIndexArray(key), function (_key) {
                    /* 移除 publish.* */
                    localStorage.removeItem(_key);
                });
            }
            localStorage.removeItem(key);
        }
    });

    function retrieveIndexArray(storeIndex) {
        try {
            return _.compact(localStorage[storeIndex].split(";"));
        }
        catch (e) {
            localStorage[storeIndex] = ";";
            return [];
        }
    };

    function startWith(src, str) {
        if (str == null || str == "" || src.length == 0 || str.length > src.length)
            return false;
        if (src.substr(0, str.length) == str)
            return true;
        else
            return false;
        return true;
    }

    return localStorage;
});
