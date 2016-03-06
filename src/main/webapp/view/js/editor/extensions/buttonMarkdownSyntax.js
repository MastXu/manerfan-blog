define([
    "jquery",
    "js/editor/classes/Extension",
    "text!pages/editor/html/buttonMarkdownSyntax.html",
], function($, Extension, buttonMarkdownSyntaxHTML) {

    var buttonMarkdownSyntax = new Extension("buttonMarkdownSyntax", 'Button "Markdown syntax', true, true);
    buttonMarkdownSyntax.settingsBlock = '<p>Adds a "Markdown syntax" button over the preview.</p>';

    buttonMarkdownSyntax.onCreatePreviewButton = function() {
        return buttonMarkdownSyntaxHTML;
    };

    return buttonMarkdownSyntax;

});