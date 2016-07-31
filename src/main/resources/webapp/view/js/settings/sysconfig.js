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
 * Created by ManerFan on 2016/7/22 0022.
 */

/**
 * 系统设置
 */
require([
    "jquery",
    'underscore',
    'nprogress',
    "jBoxUtil",
    'bootbox',
    'commonutils',
    'jcryption'
], function ($, _, NProgress, jBoxUtil, bootbox, commonutils) {
    /**
     * 恢复数据
     */
    $(".list-group-item[data-action='sysconfig']").click(function () {
        $("#panel-settings").children("div").hide();
        init(function () {
            $(".panel[data-action='sysconfig']").show();
        });
    });

    function init(call) {
        NProgress.start();
        $.ajax({
            url: "/sysconfig",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: {
                keys: ['email_stmp_host', 'email_stmp_port',
                    'email_ssl_enable', 'email_username',
                    'duoshuo_key', 'duoshuo_url']
            },
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                configInput($("#email-hostname"), data.email_stmp_host);
                configInput($("#email-port"), data.email_stmp_port);
                configInput($("#email-name"), data.email_username);
                configCheckbox($("#email-sslenable"), data.duoshuo_key);

                configInput($("#duoshuo-key"), data.duoshuo_key);
                configInput($("#duoshuo-url"), data.duoshuo_url);

                if (typeof call == 'function') {
                    call();
                }
            },
            error: function () {
                jBoxUtil.noticeError({content: '通信错误'});
            },
            complete: function () {
                NProgress.done();
            }
        });
    }

    function configInput($input, value) {
        if (!$input || !value) {
            return;
        }

        $input.val(value);
    }

    function configCheckbox($cb, value) {
        if (!$cb || !value) {
            return;
        }

        $cb.prop("checked", "TRUE" == value);
    }

    /***************************
     * 邮箱
     ***************************/
    function makeEmailData() {
        var data = {}
    }

    /***************************
     * 多说
     ***************************/
    $("#btn-duoshuo-clear").click(function () {
        $("._loading").show();
        $.ajax({
            url: "/sysconfig/duoshuo/clean",
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

                $(".duoshuo-config input").val("");
                jBoxUtil.noticeInfo({content: '已保存'});
            },
            error: function () {
                jBoxUtil.noticeError({content: '通信错误'});
            },
            complete: function () {
                $("._loading").hide();
            }
        });
    });

    $("#btn-duoshuo-config").click(function () {
        var config = makeDuoshuoData();
        if (!validateDuoshuo(config)) {
            jBoxUtil.noticeWarning({content: '请填写完整参数'});
            return;
        }

        $("#btn-duoshuo-config").button('loading');
        NProgress.start();
        $.ajax({
            url: "/sysconfig/duoshuo/update",
            async: true,
            type: 'post',
            cache: false,
            dataType: 'json',
            data: config,
            success: function (data, textStatus, XMLHttpRequest) {
                if (null != data.errmsg) {
                    // 出现错误
                    jBoxUtil.noticeError({content: data.errmsg});
                    return;
                }

                jBoxUtil.noticeInfo({content: '已保存'});
            },
            error: function () {
                jBoxUtil.noticeError({content: '通信错误'});
            },
            complete: function () {
                NProgress.done();
                $("#btn-duoshuo-config").button('reset');
            }
        });
    });

    function makeDuoshuoData() {
        var data = {};
        data.key = $("#duoshuo-key").val();
        data.url = $("#duoshuo-url").val();

        return data;
    }

    function validateDuoshuo(data) {
        if (!data || !data.key || !data.url) {
            return false;
        }

        return commonutils.hasText(data.key) && commonutils.hasText(data.url);
    }

    $(".list-group-item[data-action='sysconfig']").css("visibility", "visible");
});