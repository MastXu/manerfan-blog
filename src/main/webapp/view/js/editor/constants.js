define([], function() {
	var constants = {};
	constants.VERSION = "4.3.14";
	constants.MAIN_URL = "https://stackedit.io/";
	constants.DEFAULT_FILE_TITLE = "Title";
	constants.DEFAULT_FOLDER_NAME = "New folder";
	constants.EDITOR_DEFAULT_PADDING = 35;
	constants.CHECK_ONLINE_PERIOD = 120000;
	constants.AJAX_TIMEOUT = 30000;
	constants.ASYNC_TASK_DEFAULT_TIMEOUT = 60000;
	constants.ASYNC_TASK_LONG_TIMEOUT = 180000;
	constants.USER_IDLE_THRESHOLD = 300000;
	constants.IMPORT_FILE_MAX_CONTENT_SIZE = 100000;
	constants.IMPORT_IMG_MAX_CONTENT_SIZE = 10000000;
	constants.TEMPORARY_FILE_INDEX = "file.tempIndex";
	constants.WELCOME_DOCUMENT_TITLE = "Hello!";
	constants.DOWNLOAD_IMPORT_URL = "/downloadImport";
	constants.PICASA_IMPORT_IMG_URL = "/picasaImportImg";
	constants.PDF_EXPORT_URL = "/pdfExport";

	// Site dependent
	constants.BASE_URL = "http://localhost/";
	constants.GITHUB_CLIENT_ID = 'e47fef6055344579799d';

	if(location.hostname.indexOf("stackedit.io") === 0) {
		constants.BASE_URL = constants.MAIN_URL;
		constants.GITHUB_CLIENT_ID = '710fc67886ab1ae8fee6';
	}
	else if(location.hostname.indexOf("benweet.github.io") === 0) {
		constants.BASE_URL = 'http://benweet.github.io/stackedit/';
		constants.GITHUB_CLIENT_ID = 'fa0d09514da8377ee32e';
	}
	else if(location.hostname.indexOf("stackedit-beta.herokuapp.com") === 0) {
		constants.BASE_URL = 'https://stackedit-beta.herokuapp.com/';
		constants.GITHUB_CLIENT_ID = 'e9034ae191c3a8a1c5ed';
	}
	else if(location.hostname.indexOf("benweet.insomnia247.nl") === 0) {
		constants.BASE_URL = "http://benweet.insomnia247.nl/stackedit/";
		constants.GITHUB_CLIENT_ID = 'd2943d6074b2d9c4a830';
	}

	return constants;
});
