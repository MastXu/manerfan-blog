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
 * Created by ManerFan on 2016/7/21 0022.
 */

/**
 * 系统备份
 */
require([
    "jquery",
    'underscore',
    'nprogress',
    "jBoxUtil",
    'bootbox'
], function ($, _, NProgress, jBoxUtil, bootbox) {
    var dirTemplate = "<article class='list-group-item dir-item backup-dir'>" +
        "<a href='#' data-path='<%= dirPath %>' class='icon-folder-open dir'></a>" +
        "<span><%= pathName %></span>" +
        "<a type='button' href='/sysconfig/backup/download?name=<%= pathName %>.zip&path=<%= dirPath %>' style='width: 100% !important' class='btn btn-link btn-xs btn-block btn-backup-download' ><i class='glyphicon glyphicon-download-alt'></i>下载</a>" +
        "</article>";

    var zipTemplate = "<article class='list-group-item dir-item'>" +
        "<a href='#' data-path='<%= zipPath %>' class='icon-archive'></a>" +
        "<span><%= pathName %></span>" +
        "<span><%= size %>MB</span>" +
        "<a type='button' href='/sysconfig/backup/download?name=<%= pathName %>.zip&path=<%= zipPath %>' style='width: 100% !important' class='btn btn-link btn-xs btn-block btn-backup-download' ><i class='glyphicon glyphicon-download-alt'></i>下载</a>" +
        "</article>";

    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='system-backup']").click(function () {
        $("#panel-settings").children("div").hide();
        init(function () {
            $(".panel[data-action='system-backup']").show();
        });
    });

    function init(call) {
        initSysconf(call);
        findBackupList();
    }

    function initSysconf(call) {
        $.ajax({
            url: "/sysconfig",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {
                keys: ['backup_week', 'backup_hour', 'backup_keep']
            },
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }
                $("#backup-week").val(data.backup_week);
                $("#backup-hour").val(data.backup_hour);
                $("#backup-keep").val(data.backup_keep);
                if (typeof call == 'function') {
                    call();
                }
            },
            error: function () {
                jBoxUtil.noticeError({content: '通信错误'});
            },
            complete: function () {
            }
        });
    }

    /**
     * 点击文件夹
     */
    $(document).on("click", ".list-group-item.backup-dir a.dir, .backup-breadcrumb a", function (e) {
        e.stopPropagation();
        e.preventDefault();
        findBackupList($(this).data("path"));
    });

    /**
     * 在相对路径relative中查找
     * @param relative
     */
    function findBackupList(relative) {
        var path = !!relative ? relative : "";

        NProgress.start();
        $.ajax({
            url: "/sysconfig/backup/query/" + path,
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                var html = [];
                if (!!data.dirs) {
                    /* 目录 */
                    _.each(data.dirs, function (dir) {
                        html.push(_.template(dirTemplate)({
                            dirPath: !!relative ? relative + "/" + dir.name : dir.name,
                            pathName: dir.name
                        }));
                    });
                }
                if (!!data.zips) {
                    /* 文件 */
                    _.each(data.zips, function (zip) {
                        var name = zip.name.replace(/(\.bak)?\.zip/, "");
                        html.push(_.template(zipTemplate)({
                            zipPath: !!relative ? relative + "/" + zip.name : zip.name,
                            pathName: name,
                            size: zip.size.toFixed(2)
                        }));
                    });
                }

                $(".backup-manager-list").empty().append(html.join(""));

                /* 修正路径导航 */
                html = [];
                $(".backup-breadcrumb").empty();
                var paths = path.toString().split("/");
                var cp = ""; // 记录每个路径节点的路径值
                html.push('<li><a href="#" data-path="">backup</a></li>'); // 加一个根路径
                _.each(paths, function (p) {
                    if ("" == p) {
                        return true;
                    }
                    cp += p + "/";
                    html.push('<li><a href="#" data-path="' + cp + '">' + p + '</a></li>');
                });
                $(".backup-breadcrumb").empty().append(html.join(""));
                var lastcrumb = $(".backup-breadcrumb a:last").text();
                $(".backup-breadcrumb li:last").addClass("active").empty().text(lastcrumb);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                NProgress.done();
            }
        });
    }

    $("#backup-immediately").click(function () {
        NProgress.start();
        $("#backup-immediately").button('loading');
        $.ajax({
            url: "/sysconfig/backup/immediately",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                jBoxUtil.noticeInfo({content: "备份成功"});
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("#backup-immediately").button('reset');
                NProgress.done();
            }
        });
    });

    $("#btn-backup-config").click(function () {
        var config = makeData();
        if (!validateDate(config)) {
            return;
        }

        $("#btn-backup-config").button('loading');
        NProgress.start();
        $.ajax({
            url: "/sysconfig/backup/update",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: config,
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                jBoxUtil.noticeInfo({content: '已保存'});
            },
            error: function () {
                jBoxUtil.noticeError({content: '通信错误'});
            },
            complete: function () {
                NProgress.done();
                $("#btn-backup-config").button('reset');
            }
        });
    });

    function makeData() {
        var data = {}
        data.week = $("#backup-week").val();
        data.hour = $("#backup-hour").val();
        data.keep = $("#backup-keep").val();

        return data;
    }

    function validateDate(data) {
        if (!data || !data.week || !data.hour || !data.keep) {
            jBoxUtil.noticeWarning({content: '请填写完整参数'});
            return false;
        }

        try {
            data.keep = parseInt(data.keep);
        } catch (e) {
            jBoxUtil.noticeWarning({content: '请正确填写保留条数'});
            return false;
        }

        if (!_.isNumber(data.keep) || NaN == data.keep | data.keep < 1 || data.keep > 60) {
            jBoxUtil.noticeWarning({content: '保留条数在 [1, 60] 之间'});
            return false;
        }

        return true;
    }

    $(".list-group-item[data-action='system-backup']").css("visibility", "visible");
});