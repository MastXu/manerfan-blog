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
 * Created by ManerFan on 2016/4/22 0022.
 */

/**
 * 图片管理
 */
require([
    "jquery",
    'underscore',
    "jBoxUtil",
    'bootbox'
], function ($, _, jBoxUtil, bootbox) {
    var dirTemplate = "<article class='list-group-item dir-item'>" +
        "<a href='#' data-path='<%= imagePath %>' class='glyphicon glyphicon-folder-close'></a>" +
        "<span><%= pathName %></span>" +
        "</article>";

    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='image-manager']").click(function () {
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='image-manager']").show();

        findImageList();
    });

    /**
     * 点击文件夹
     */
    $(document).on("click", ".list-group-item.dir-item a, .image-breadcrumb a", function (e) {
        e.stopPropagation();
        e.preventDefault();
        findImageList($(this).data("path"));
    });

    /**
     * 在相对路径relative中查找
     * @param relative
     */
    function findImageList(relative) {
        var path = !!relative ? relative : "";
        $("._loading").show();
        $.ajax({
            url: "/article/image/list/" + path,
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
                            imagePath: !!relative ? relative + "/" + dir : dir,
                            pathName: dir
                        }));
                    });
                }
                if (!!data.imgs) {
                    /* 图片 */

                }

                $(".image-manager-list").empty().append(html.join(""));

                /* 修正路径导航 */
                html = [];
                $(".image-breadcrumb").empty();
                var paths = path.toString().split("/");
                var cp = ""; // 记录每个路径节点的路径值
                html.push('<li><a href="#" data-path="">image</a></li>'); // 加一个根路径
                _.each(paths, function (p) {
                    if ("" == p) {
                        return true;
                    }
                    cp += p + "/";
                    html.push('<li><a href="#" data-path="' + cp + '">' + p + '</a></li>');
                });
                $(".image-breadcrumb").empty().append(html.join(""));
                var lastcrumb = $(".image-breadcrumb a:last").text();
                $(".image-breadcrumb li:last").addClass("active").empty().text(lastcrumb);
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    $(".list-group-item[data-action='image-manager']").css("visibility", "visible");
});