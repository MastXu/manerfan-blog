define([
	"jquery",
	"underscore",
	"js/editor/constants",
	"js/editor/utils",
	"js/editor/classes/Extension",
	"text!pages/editor/html/dialogAbout.html"
], function($, _, constants, utils, Extension, dialogAboutHTML) {

	var dialogAbout = new Extension("dialogAbout", 'Dialog "About"');

	var eventMgr;
	dialogAbout.onEventMgrCreated = function(eventMgrParameter) {
		eventMgr = eventMgrParameter;
	};

	dialogAbout.onReady = function() {
		utils.addModal('modal-about', _.template(dialogAboutHTML, {
			version: constants.VERSION
		}));
	};

	return dialogAbout;

});
