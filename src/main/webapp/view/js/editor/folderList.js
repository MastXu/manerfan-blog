define([
    "underscore",
    "js/editor/utils",
    "js/editor/storage",
    "js/editor/classes/FolderDescriptor",
    "js/editor/storage",
], function(_, utils, storage, FolderDescriptor) {
    var folderList = {};

    // Retrieve folder descriptors from localStorage
    utils.retrieveIndexArray("folder.list").forEach(function(folderIndex) {
        folderList[folderIndex] = new FolderDescriptor(folderIndex);
    });

    // Clean fields from deleted folders in local storage
    Object.keys(storage).forEach(function(key) {
        var match = key.match(/(folder\.\S+?)\.\S+/);
        if(match && !folderList.hasOwnProperty(match[1])) {
            storage.removeItem(key);
        }
    });

    return folderList;
});
