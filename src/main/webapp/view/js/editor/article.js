/**
 * Created by ManerFan on 2016/3/29 0029.
 */

define([
    'jquery',
    'js/editor/eventMgr',
    'bootstrap'
], function ($, eventMgr) {
    /**
     * 内容变化时触发
     */
    eventMgr.addListener('onContentChanged', function () {
        $(".btn-blog-save").addClass("changed");
    });
});
