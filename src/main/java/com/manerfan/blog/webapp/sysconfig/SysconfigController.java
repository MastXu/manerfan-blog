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
package com.manerfan.blog.webapp.sysconfig;

import java.security.interfaces.RSAPrivateKey;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.service.RSAService;
import com.manerfan.blog.service.SysConfService;
import com.manerfan.blog.utils.EmailUtils;
import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.tools.EncryptBean;

/**
 * <pre>
 * 系统配置
 * </pre>
 *
 * @author ManerFan 2016年7月22日
 */
@Controller
@RequestMapping("/sysconfig")
public class SysconfigController extends ControllerBase {
    
    @Autowired
    private EncryptBean encrypt;

    @Autowired
    private RSAService rsaService;

    @Autowired
    private SysConfService sysConfService;

    @RequestMapping
    @ResponseBody
    public Object query(@RequestParam("keys[]") List<String> keys) {
        Map<String, Object> data = makeAjaxData();

        try {
            data.putAll(sysConfService.getMap(keys));
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when query {}", keys, e);
            data.put(ERRMSG, "内部错误");
        }

        return data;
    }

    @RequestMapping("/email/clean")
    @ResponseBody
    public Object clearEmailConf() {
        Map<String, Object> data = makeAjaxData();

        try {
            sysConfService.removes(SysConfService.EMAIL_STMP_HOST, SysConfService.EMAIL_STMP_PORT,
                    SysConfService.EMAIL_SSL_ENABLE, SysConfService.EMAIL_USERNAME,
                    SysConfService.EMAIL_PASSWORD);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when clear DuoShuoConfig", e);
            data.put(ERRMSG, "内部错误");
            return data;
        }

        return data;
    }

    @RequestMapping("/email/test")
    @ResponseBody
    public Object testEmailConf(@RequestParam String host, @RequestParam int port,
            @RequestParam boolean sslEnable, @RequestParam String username,
            @RequestParam String password, HttpServletRequest request) {
        Map<String, Object> data = makeAjaxData();

        if (!StringUtils.hasText(host) || !StringUtils.hasText(username)
                || !StringUtils.hasText(password)) {
            data.put(ERRMSG, "数据不完整");
            return data;
        }

        // 从缓存中获取rsa私钥
        RSAPrivateKey key = rsaService.getPrivateKey(request.getSession(true).getId());
        if (null == key) {
            // 页面停留时间过长导致key过期
            data.put(ERRMSG, "页面停留时间过长，请重新操作");
            return data;
        }

        try {
            String uname = rsaService.decode(key, username);
            String passw = rsaService.decode(key, password);

            EmailUtils.test(host, port, sslEnable, uname, passw);
        } catch (EmailException e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when Test Email Sysconfig.", e);
            data.put(ERRMSG, "测试失败");
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when update Email Sysconfig.", e);
            data.put(ERRMSG, "内部错误");
        }

        return data;
    }

    @RequestMapping("/email/update")
    @ResponseBody
    public Object updateEmailConf(@RequestParam String host, @RequestParam int port,
            @RequestParam boolean sslEnable, @RequestParam String username,
            @RequestParam String password, HttpServletRequest request) {
        Map<String, Object> data = makeAjaxData();

        if (!StringUtils.hasText(host) || !StringUtils.hasText(username)
                || !StringUtils.hasText(password)) {
            data.put(ERRMSG, "数据不完整");
            return data;
        }

        // 从缓存中获取rsa私钥
        RSAPrivateKey key = rsaService.getPrivateKey(request.getSession(true).getId());
        if (null == key) {
            // 页面停留时间过长导致key过期
            data.put(ERRMSG, "页面停留时间过长，请重新操作");
            return data;
        }

        try {
            String uname = rsaService.decode(key, username);
            String passw = rsaService.decode(key, password);

            sysConfService.updateOrSave(SysConfService.EMAIL_STMP_HOST, host);
            sysConfService.updateOrSave(SysConfService.EMAIL_STMP_PORT, port);
            sysConfService.updateOrSave(SysConfService.EMAIL_SSL_ENABLE, sslEnable);
            sysConfService.updateOrSave(SysConfService.EMAIL_USERNAME, uname);
            sysConfService.updateOrSave(SysConfService.EMAIL_PASSWORD, encrypt.encryptString(passw));
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when update Email Sysconfig.", e);
            data.put(ERRMSG, "内部错误");
        }

        return data;
    }

    @RequestMapping("/duoshuo/update")
    @ResponseBody
    public Object updateDuoshuoConf(@RequestParam String key, @RequestParam String url) {
        Map<String, Object> data = makeAjaxData();

        if (!StringUtils.hasText(key) || !StringUtils.hasText(url)) {
            data.put(ERRMSG, "数据不完整");
            return data;
        }

        try {
            sysConfService.updateOrSave(SysConfService.DUOSHUO_KEY, key);
            sysConfService.updateOrSave(SysConfService.DUOSHUO_URL, url);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error(
                    "Some Error Occured when update Sysconfig [{}: {}] or [{}: {}].", new Object[] {
                            SysConfService.DUOSHUO_KEY, key, SysConfService.DUOSHUO_URL, url, e });
            data.put(ERRMSG, "内部错误");
            return data;
        }

        return data;
    }
    
    @RequestMapping("/duoshuo/clean")
    @ResponseBody
    public Object clearDuoshuoConf() {
        Map<String, Object> data = makeAjaxData();

        try {
            sysConfService.removes(SysConfService.DUOSHUO_KEY, SysConfService.DUOSHUO_URL);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when clear DuoShuoConfig", e);
            data.put(ERRMSG, "内部错误");
            return data;
        }

        return data;
    }
}
