define([], function () {
    var constants = {};
    constants.VERSION = "4.3.14";
    constants.MAIN_URL = "http://www.manerfan.com/";
    constants.DEFAULT_FILE_TITLE = "文章标题";
    constants.DEFAULT_FOLDER_NAME = "New folder";
    constants.EDITOR_DEFAULT_PADDING = 35;
    constants.CHECK_ONLINE_PERIOD = 120000;
    constants.AJAX_TIMEOUT = 30000;
    constants.ASYNC_TASK_DEFAULT_TIMEOUT = 60000;
    constants.ASYNC_TASK_LONG_TIMEOUT = 180000;
    constants.USER_IDLE_THRESHOLD = 300000;
    constants.IMPORT_FILE_MAX_CONTENT_SIZE = 100000;
    constants.IMPORT_IMG_MAX_CONTENT_SIZE = 10000000;
    constants.TEMPORARY_FILE_INDEX = "file.mblog";
    constants.FRONT_WINDOWID = "mblog.front.windowId";
    constants.WELCOME_DOCUMENT_TITLE = "欢迎使用MBLOG-markdown编辑器!";
    constants.DOWNLOAD_IMPORT_URL = "/editor/fileImport";
    constants.PICASA_IMPORT_IMG_URL = "/picasaImportImg";
    constants.PDF_EXPORT_URL = "/pdfExport";

    // Site dependent
    constants.BASE_URL = "http://localhost/";
    constants.GITHUB_CLIENT_ID = 'e47fef6055344579799d';

    return constants;
});
