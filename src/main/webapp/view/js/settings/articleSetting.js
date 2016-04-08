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
 * 文章设置
 * Created by ManerFan on 2016/4/8.
 */
require([
    "jquery",
    'bootcss-paginator'
], function ($) {
    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='article-settings']").click(function () {
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='article-settings']").show();
    });


});