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
 * 文章浏览
 * Created by ManerFan on 2016/4/26 0026.
 */
require([
    "jquery",
    "js/editor/extensions/echartsParser",
    "js/article/articleWidget"
], function ($, echartsParser, articleWidget) {
    try {
        echartsParser.parseArticle($("code.language-echarts"));
    } catch (e) {
        console.error("Pares Echarts Error!");
        console.error(e);
    }

    window.setTimeout(articleWidget.init, 1000);
});
