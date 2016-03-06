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
 * Created by ManerFan on 2016/3/6 0006.
 */

define([
    "jquery",
    "js/editor/classes/Extension",
    "echarts"
], function ($, Extension, echarts) {
    var echartsParser = new Extension("echartsParser", "Echarts Parser Extra", true);

    var eventMgr;
    echartsParser.onEventMgrCreated = function (eventMgrParameter) {
        eventMgr = eventMgrParameter;
    };

    /**
     * 将elt转为echarts图表
     * @param elt
     * @returns {boolean}
     */
    echartsParser.parse = function (elt) {
        if ($(elt).hasClass("language-echarts")) {
            try {
                eval($(elt).text());
                var chart = echarts.init(elt);
                chart.setOption(option);
                return true;
            } catch (e) {
                // 如果出错，则按普通code处理
                console.error(e);
                return false;
            }
        } else {
            return false;
        }
    }

    return echartsParser;
});
