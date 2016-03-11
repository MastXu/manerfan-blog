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
        "jcryption",
        "md5",
        "bootstrap",
        themeModule
    ],
    function ($, _) {
        // CSS 加载完之后再显示
        $(".body").show();

        var $username = $("input[name='username']");
        var $password = $("input[id='password']");

        $username.focus();

        $("button[type='submit']").click(function (event) {

            $(".has-error").removeClass("has-error");

            if (!isEmpty($username, event)) { // 无用户
                return false;
            }

            if (!isEmpty($password, event)) { // 无密码
                return false;
            }

            // md5
            var password = $.md5($password.val());
            // rsa pkcs#1 padding
            $.jCryption.crypt.setKey({e: $("#exponent").val(), n: $("#modulus").val()});
            password = $.jCryption.crypt.encrypt(password);
            $("input[name='password']").val(password);

            /*$.jCryption.getPublicKey("/login/publickey", function () {
             var password = $.md5($password.val());
             $.jCryption.encryptKey(password, function (encryptedPasswd) {
             $password.val(encryptedPasswd);
             $("form[name='loginForm']").submit();
             });
             });*/
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
