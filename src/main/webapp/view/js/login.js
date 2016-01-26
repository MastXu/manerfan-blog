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

var themeModule = "css!style/themes/default";
if (!!window.debug) {
    themeModule = "less!style/themes/default";
}

/**
 * 登陆
 * Created by ManerFan on 2016/1/26.
 */
require([
        "jquery",
        "underscore",
        "md5",
        themeModule
    ],
    function ($, _) {
        $(".bg img").load(function () {
            $(this).fadeIn("fast");
        });
        $(".bg img").attr("src",
            "http://imglf1.nosdn.127.net/img/Z09yMllJWHpmK2NBcnB4M2lKOFRHK2d1azFNSmFveVE1OG8vTmVJNXhZYnhycGFzOXhqckZ3PT0.jpg?imageView&thumbnail=2000y1235&type=jpg&quality=96&stripmeta=0&type=jpg");

        // CSS 加载完之后再显示
        $(".body").show();

        var $username = $("input[name='username']");
        var $password = $("input[name='password']");

        $username.focus();

        $("button[type='submit']").click(function (event) {

            $(".has-error").removeClass("has-error");

            if (!isEmpty($username, event)) { // 无用户
                return false;
            }

            if (!isEmpty($password, event)) { // 无密码
                return false;
            }

            $password.val($.md5($password.val()));
        });

        function isEmpty(_input, _event) {
            if (_.isEmpty(_input.val())) {
                _input.parent().addClass("has-error");
                _input.focus();
                _event.preventDefault();
                _event.stopPropagation();
                return false;
            } else {
                return true;
            }
        }
    }
);
