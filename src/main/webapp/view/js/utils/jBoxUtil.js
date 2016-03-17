/*
 * ManerFan(http://www.manerfan.com). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
