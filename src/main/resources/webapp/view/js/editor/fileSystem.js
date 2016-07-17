define([
    "underscore",
    "js/editor/utils",
    "js/editor/classes/FileDescriptor",
    "js/editor/storage",
], function(_, utils, FileDescriptor, storage) {
    var fileSystem = {};

    // Retrieve file descriptors from localStorage
    utils.retrieveIndexArray("file.list").forEach(function(fileIndex) {
        fileSystem[fileIndex] = new FileDescriptor(fileIndex);
    });

    // Clean fields from deleted files in local storage
    Object.keys(storage).forEach(function(key) {
        var match = key.match(/(file\.\S+?)\.\S+/);
        if(match && !fileSystem.hasOwnProperty(match[1])) {
            storage.removeItem(key);
        }
    });

    return fileSystem;
});
