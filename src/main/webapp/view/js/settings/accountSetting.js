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
 * 用户账户设置
 * Created by ManerFan on 2016/4/8.
 */
require([
    "jquery",
    "js/editor/core",
    "commonutils",
    "jBoxUtil",
    "jcryption",
    "md5"
], function ($, core, commonutils, jBoxUtil) {
    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='account-settings']").click(function () {
        $("input").val("");
        $("#account-email").val($("#account-email").data("account-email"));
        $("#panel-settings").children("div").hide();
        $(".panel[data-action='account-settings']").show();
    });

    /******************
     * 修改邮箱
     ******************/

    $("#btn-account-info").click(function () {
        var email = $.trim($("#account-email").val());
        if (email.length > 0 && !commonutils.isEmial(email)) {
            jBoxUtil.noticeError({content: "请填写正确的邮箱地址"});
            $("#account-email").focus();
            return;
        }

        $("#btn-account-info").button('loading');
        $.ajax({
            url: "/user/modify/email",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {email: email},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                $("#account-email").data("account-email", data.email);
            },
            error: function () {
                core.setOffline();
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("#btn-account-info").button('reset');
            }
        });
    });

    /******************
     * 修改密码
     ******************/

    $("#btn-account-password").click(function () {
        var orgPasswd = $.trim($("#account-org-password").val());
        var newPasswd = $.trim($("#account-new-password").val());
        var cfmPasswd = $.trim($("#account-cfm-password").val());

        if (orgPasswd.length < 1) {
            jBoxUtil.noticeError({content: "请填写原密码"});
            $("#account-org-password").focus();
            return;
        }

        if (newPasswd.length < 1) {
            jBoxUtil.noticeError({content: "请填写新密码"});
            $("#account-new-password").focus();
            return;
        }

        if (cfmPasswd.length < 1) {
            jBoxUtil.noticeError({content: "请再次填写新密码"});
            $("#account-cfm-password").focus();
            return;
        }

        if (newPasswd != cfmPasswd) {
            jBoxUtil.noticeError({content: "两次密码输入不一致"});
            $("#account-cfm-password").focus();
            return;
        }

        $("#btn-account-password").button('loading');
        $.ajax({
            url: "/login/publickey",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    $("#btn-account-password").button('reset');
                    return;
                }

                $.jCryption.crypt.setKey({e: data.exponent, n: data.modulus});
                orgPasswd = $.jCryption.crypt.encrypt($.md5(orgPasswd));
                newPasswd = $.jCryption.crypt.encrypt($.md5(newPasswd));

                modifyPasswd(orgPasswd, newPasswd);
            },
            error: function () {
                core.setOffline();
                jBoxUtil.noticeError({content: "未知错误"});
                $("#btn-account-password").button('reset');
            },
            complete: function () {
            }
        });
    });

    function modifyPasswd(orgPasswd, newPasswd) {
        $.ajax({
            url: "/user/modify/passwd",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {orgPasswd: orgPasswd, newPasswd: newPasswd},
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                $("input[id$='-passwd']").val("");
                window.location = "/logout";
            },
            error: function () {
                core.setOffline();
                jBoxUtil.noticeError({content: "未知错误"});
            },
            complete: function () {
                $("#btn-account-password").button('reset');
            }
        });
    }
});