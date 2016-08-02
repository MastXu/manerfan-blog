define([
    "jquery",
    "js/editor/classes/Extension",
    "text!pages/editor/html/buttonViewer.html",
], function($, Extension, buttonViewerHTML) {

    var buttonViewer = new Extension("buttonViewer", 'Button "Viewer"', true, true);
    buttonViewer.settingsBlock = '<p>Adds a "Viewer" button over the preview.</p>';

    buttonViewer.onCreatePreviewButton = function() {
        return buttonViewerHTML;
    };

    return buttonViewer;

});