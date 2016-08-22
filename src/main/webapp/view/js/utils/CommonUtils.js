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
 * Created by ManerFan on 2016/1/28.
 */

define([
        "jquery",
        "underscore",
    ], function ($, _) {
        var commonutils = {};

        /**
         * 克隆
         * @param obj
         * @returns {*}
         */
        commonutils.clone = function (obj) {
            var o;
            if (typeof obj == "object") { // 非基本类型
                if (obj === null) {
                    o = null;
                } else {
                    if (obj instanceof Array) { // 序列，需要递归克隆
                        o = [];
                        for (var i = 0, len = obj.length; i < len; i++) {
                            o.push(clone(obj[i]));
                        }
                    } else {
                        o = {};
                        for (var j in obj) { // 对象中的每一个参数，都需要递归克隆
                            o[j] = clone(obj[j]);
                        }
                    }
                }
            } else {  // 基本类型
                o = obj;
            }
            return o;
        };

        /**
         * 将jQuery.Date 格式化为 yyyy-MM-dd HH:mm:ss
         * @param date Date
         * @returns {string}
         */
        commonutils.dateFormate = function (date) {
            if (typeof(date) == "undefined" || null == date) {
                return "";
            }

            var year = date.getFullYear();
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
            var minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        };

        /**
         * 将 yyyy-MM-dd HH:mm:ss 格式字符串转化成jQuery.Date
         * @param dateString
         * @returns {Date}
         */
        commonutils.dateParse = function (dateString) {
            if (typeof(dateString) == "undefined"
                || null == dateString) {
                return null;
            }

            var sub = dateString.split(" ");
            if (sub.length < 2) { // 格式不正确
                return null;
            }

            var subY = sub[0].split("-");
            if (subY.length < 3) { // 格式不正确
                return null;
            }

            var subD = sub[1].split(":");
            if (subD.length < 3) { // 格式不正确
                return null;
            }

            return new Date(subY[0], subY[1] - 1, subY[2], subD[0], subD[1], subD[2]);
        };


        commonutils.isPhoneNum = function (_phone) {
            if (!_.isString(_phone)) {
                return false;
            }

            if (!_phone.match(RegExp("^1\\d{10}$"))) {
                return false;
            }

            return true;
        };

        commonutils.isHost = function (_host) {
            if (!_.isString(_host)) {
                return false;
            }

            if (!_host.match(RegExp("((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?"))) {
                return false;
            }

            return true;
        };

        /** 校验邮箱格式 */
        commonutils.isEmail = function (_email) {
            if (!_.isString(_email)) {
                return false;
            }

            if (!_email.match(RegExp("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$"))) {
                return false;
            }

            return true;
        };

        commonutils.hasText = function (_text) {
            if (!_.isString(_text)) {
                return false;
            }

            if ($.trim(_text).length < 1) {
                return false;
            }

            return true;
        };

        return commonutils;
    }
)
;
