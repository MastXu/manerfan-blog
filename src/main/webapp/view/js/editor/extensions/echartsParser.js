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
    'crel',
    "echarts",
    "js-base64"
], function ($, Extension, crel, echarts) {
    var echartsParser = new Extension("echartsParser", "Echarts Parser Extra", true);

    var eventMgr;
    echartsParser.onEventMgrCreated = function (eventMgrParameter) {
        eventMgr = eventMgrParameter;
    };

    /**
     * window resize
     */
    echartsParser.onLayoutResize = function () {
        // TODO some opr on window resize
    };

    /**
     * 将elt转为echarts图表
     * @param elt
     * @returns {boolean}
     */
    echartsParser.parse = function (elt) {
        if ($(elt).hasClass("language-echarts")) {
            try {
                var optioneval = $(elt).text();

                // 执行echarts语句
                eval(optioneval);
                // 初始化echarts
                var chart = echarts.init(elt);
                // 初始化图表
                chart.setOption(option);

                // 将echarts语句(编码)保存到pre，查看文章时需要重新初始化echarts
                var oc = crel('pre', {
                    class: 'echarts-option',
                    style: 'display: none'
                });
                oc.innerHTML = Base64.encode(optioneval);
                $(elt).append(oc);

                return true;
            } catch (e) {
                // 如果出错，则按普通code处理
                console.error(e);
                return false;
            }
        } else {
            return false;
        }
    };

    echartsParser.parseArticle = function (elts) {
        $.each(elts, function (index, elt) {
            var $elt = $(elt);
            var optioneval = Base64.decode($elt.find(".echarts-option").text());
            $elt.empty();
            // 执行echarts语句
            eval(optioneval);
            // 初始化echarts
            var chart = echarts.init(elt);
            // 初始化图表
            chart.setOption(option);
        });
    };

    return echartsParser;
});
