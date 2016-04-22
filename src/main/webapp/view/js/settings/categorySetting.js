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
 * 分类管理
 * Created by ManerFan on 2016/4/8.
 */
require([
    "jquery",
    'underscore',
    "commonutils",
    "jBoxUtil",
    'bootbox',
    'text!pages/article/html/categoryList.html',
    'pagination'
], function ($, _, commonutils, jBoxUtil, bootbox, categoryListHTML) {

    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='category-settings']").click(function () {
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='category-settings']").show();

        findCategoryListAll();
        /**
         if (!$(this).data("hasOpend")) {
             $(this).data("hasOpend", true);
             findCategoryListAll();
         }
         */
    });

    /**
     * 删除分类
     */
    $(document).on("click", ".btn-category-delete", function () {
        var $category_tag = $(this).parents(".list-category-item");
        var name = $category_tag.data("name");

        bootbox.confirm("确认删除分类 " + name + " ?", function (result) {
            if (!!result) {
                $("._loading").show();
                $.ajax({
                    url: "/category/delete/" + name,
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

                        $category_tag.fadeOut(function () {
                            $category_tag.remove();
                        });
                    },
                    error: function () {
                        jBoxUtil.noticeError({content: "未知错误"});
                    },
                    complete: function () {
                        $("._loading").hide();
                    }
                });
            }
        });
    });

    /**
     * 重命名分类
     */
    $(document).on("click", ".btn-category-rename", function () {
        var $category_tag = $(this).parents(".list-category-item");
        var oldName = $category_tag.data("name");

        bootbox.prompt("重命名分类", function (newName) {
            if (oldName == newName) {
                // 没有修改
                return;
            }

            $("._loading").show();
            $.ajax({
                url: "/category/rename/" + oldName,
                async: true,
                type: 'post',
                cache: false,
                dataType: 'json',
                data: {newName: newName},
                success: function (data, textStatus, XMLHttpRequest) {
                    if (null != data.errmsg) {
                        // 出现错误
                        jBoxUtil.noticeError({content: data.errmsg});
                        return;
                    }

                    $category_tag.data("name", newName);
                    $category_tag.find(".category-header").attr("href", "/article/category/" + newName);
                    $category_tag.find(".category-name").text(newName);
                },
                error: function () {
                    jBoxUtil.noticeError({content: "未知错误"});
                },
                complete: function () {
                    $("._loading").hide();
                }
            });
        });

        $(".bootbox-form input").val(oldName);
    });

    /**
     * 查询分类
     * @param page
     * @param size
     */
    function findCategoryListAll() {
        $("._loading").show();
        $(".category-list").empty();
        $.ajax({
            url: "/category/list/all",
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

                var list = [];
                _.each(data.categories, function (category) {
                    list.push(_.template(categoryListHTML)({
                        categoryName: category.name,
                        articleNum: category.num
                    }));
                });

                $(".category-list").append(list.join(""));
            },
            error: function () {
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    }

    $(".list-group-item[data-action='category-settings']").css("visibility", "visible");
});