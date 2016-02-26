/**
 * 记录常量
 */

define([], function () {
    var constants = {};
    constants.VERSION = "4.3.12"; // 版本号
    constants.MAIN_URL = "http://www.manerfan.com/"; // 主页

    constants.DEFAULT_FOLDER_NAME = "New folder"; // 默认文件夹名
    constants.DEFAULT_FILE_TITLE = "Title"; // 默认标题名
    constants.EDITOR_DEFAULT_PADDING = 35; // 编辑区padding
    constants.CHECK_ONLINE_PERIOD = 120000;
    constants.AJAX_TIMEOUT = 30000; // ajax请求超时时间
    constants.ASYNC_TASK_DEFAULT_TIMEOUT = 60000;
    constants.ASYNC_TASK_LONG_TIMEOUT = 180000;
    constants.USER_IDLE_THRESHOLD = 300000; // 用户操作 超时时间|空闲阈值
    constants.IMPORT_FILE_MAX_CONTENT_SIZE = 100000; // 文件导入大小限制
    constants.IMPORT_IMG_MAX_CONTENT_SIZE = 10000000; // 图片导入大小限制
    constants.TEMPORARY_FILE_INDEX = "file.tempIndex"; // 临时文件索引
    constants.WELCOME_DOCUMENT_TITLE = "Hello!"; // 默认文章标题

    constants.DOWNLOAD_IMPORT_URL = "/downloadImport";
    constants.PICASA_IMPORT_IMG_URL = "/picasaImportImg";
    constants.SSH_PUBLISH_URL = '/sshPublish';
    constants.PDF_EXPORT_URL = "/pdfExport";

    constants.THEME_LIST = {
        "default": "Default"
        /*
         "blue": "Blue",
         "gray": "Gray",
         "night": "Night",
         "school": "School",
         "solarized-light": "Solarized Light",
         "solarized-dark": "Solarized Dark"
         */
    };

    /***  ***/
    constants.GOOGLE_ANALYTICS_ACCOUNT_ID = "UA-39556145-1";
    constants.GOOGLE_API_KEY = "AIzaSyAeCU8CGcSkn0z9js6iocHuPBX4f_mMWkw";
    constants.GOOGLE_DRIVE_APP_ID = "241271498917";
    constants.DROPBOX_APP_KEY = "lq6mwopab8wskas";
    constants.DROPBOX_APP_SECRET = "851fgnucpezy84t";
    constants.DROPBOX_RESTRICTED_APP_KEY = "sw0hlixhr8q1xk0";
    constants.DROPBOX_RESTRICTED_APP_SECRET = "1r808p2xygs6lbg";
    constants.BITLY_ACCESS_TOKEN = "317e033bfd48cf31155a68a536b1860013b09c4c";
    constants.GDRIVE_DEFAULT_FILE_TITLE = "New Markdown document";
    constants.COUCHDB_URL = 'https://stackedit.smileupps.com/documents';
    constants.COUCHDB_PAGE_SIZE = 25;

    // Site dependent
    constants.BASE_URL = "http://localhost/";
    constants.GOOGLE_CLIENT_ID = '241271498917-lev37kef013q85avc91am1gccg5g8lrb.apps.googleusercontent.com';
    constants.GITHUB_CLIENT_ID = 'e47fef6055344579799d';
    constants.GATEKEEPER_URL = "https://stackedit-gatekeeper-localhost.herokuapp.com/";
    constants.TUMBLR_PROXY_URL = "https://stackedit-tumblr-proxy-local.herokuapp.com/";
    constants.WORDPRESS_CLIENT_ID = '23361';
    constants.WORDPRESS_PROXY_URL = "https://stackedit-io-wordpress-proxy.herokuapp.com/";

    if (location.hostname.indexOf("stackedit.io") === 0) {
        constants.BASE_URL = constants.MAIN_URL;
        constants.GOOGLE_CLIENT_ID = '241271498917-t4t7d07qis7oc0ahaskbif3ft6tk63cd.apps.googleusercontent.com';
        constants.GITHUB_CLIENT_ID = '710fc67886ab1ae8fee6';
        constants.GATEKEEPER_URL = "https://stackedit-io-gatekeeper.herokuapp.com/";
        constants.TUMBLR_PROXY_URL = "https://stackedit-io-tumblr-proxy.herokuapp.com/";
    }
    else if (location.hostname.indexOf("benweet.github.io") === 0) {
        constants.BASE_URL = 'http://benweet.github.io/stackedit/';
        constants.GOOGLE_CLIENT_ID = '241271498917-jpto9lls9fqnem1e4h6ppds9uob8rpvu.apps.googleusercontent.com';
        constants.GITHUB_CLIENT_ID = 'fa0d09514da8377ee32e';
        constants.GATEKEEPER_URL = "https://stackedit-gatekeeper.herokuapp.com/";
        constants.TUMBLR_PROXY_URL = "https://stackedit-tumblr-proxy.herokuapp.com/";
        constants.WORDPRESS_CLIENT_ID = '3185';
        constants.WORDPRESS_PROXY_URL = "https://stackedit-wordpress-proxy.herokuapp.com/";
    }
    else if (location.hostname.indexOf("stackedit-beta.herokuapp.com") === 0) {
        constants.BASE_URL = 'https://stackedit-beta.herokuapp.com/';
        constants.GOOGLE_CLIENT_ID = '241271498917-9bbplknkt0ljv5gaudhoiogp13hd18be.apps.googleusercontent.com';
        constants.GITHUB_CLIENT_ID = 'e9034ae191c3a8a1c5ed';
        constants.GATEKEEPER_URL = "https://stackedit-beta-gatekeeper.herokuapp.com/";
        constants.TUMBLR_PROXY_URL = "https://stackedit-beta-tumblr-proxy.herokuapp.com/";
        constants.WORDPRESS_CLIENT_ID = '34786';
        constants.WORDPRESS_PROXY_URL = "https://stackedit-beta-wordpress-proxy.herokuapp.com/";
    }
    else if (location.hostname.indexOf("benweet.insomnia247.nl") === 0) {
        constants.BASE_URL = "http://benweet.insomnia247.nl/stackedit/";
        constants.GOOGLE_CLIENT_ID = '241271498917-52hae7a08hv7ltenv7km8h7lghno9sk3.apps.googleusercontent.com';
        constants.GITHUB_CLIENT_ID = 'd2943d6074b2d9c4a830';
        constants.GATEKEEPER_URL = "https://stackedit-gatekeeper-insomnia.herokuapp.com/";
        constants.TUMBLR_PROXY_URL = "https://stackedit-tumblr-proxy-beta.herokuapp.com/";
    }
    /*** ***/

    return constants;
});
