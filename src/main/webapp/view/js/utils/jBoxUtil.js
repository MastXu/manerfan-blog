/**
 * Created by ManerFan on 2016/3/17 0017.
 */

define([
    'jquery',
    'plugins/jbox/Source/jBox.min'
], function () {
    var jBoxUtil = {};

    jBoxUtil.notice = function (options) {
        var defaults = {
            autoClose: 3000,
            attributes: {
                x: "right",
                y: "bottom"
            },
            stack: true,
            animation: {
                open: "flip",
                close: "zoomIn"
            },
            theme: "NoticeBorder",
            color: "green",
            title: null,
            content: null,
            zIndex: 12e3
        };

        var opts = $.extend(defaults, options);

        if (!opts.content) {
            return;
        }

        new jBox("Notice", {
            autoClose: opts.autoClose,
            attributes: opts.attributes,
            stack: opts.stack,
            animation: opts.animation,
            audio: opts.audio,
            /* theme: opts.theme, */
            title: opts.title,
            content: opts.content,
            color: opts.color,
            zIndex: opts.zIndex,
            onInit: function () {
                this.options.color = opts.color;
            }
        });
    };
    jBoxUtil.noticeInfo = function (options) {
        this.notice($.extend({
            color: "green",
            title: "Info!"
        }, options));
    };
    jBoxUtil.noticeWarning = function (options) {
        this.notice($.extend({
            color: "yellow",
            title: "Warning!"
        }, options))
    };
    jBoxUtil.noticeError = function (options) {
        this.notice($.extend({
            color: "red",
            title: "Error!"
        }, options));
    };

    return jBoxUtil;
});
