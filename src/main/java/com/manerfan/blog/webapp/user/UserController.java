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
package com.manerfan.blog.webapp.user;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.dao.repositories.UserRepository;
import com.manerfan.blog.interceptor.UserInfoInterceptorHandler;
import com.manerfan.blog.service.RSAService;
import com.manerfan.blog.webapp.ControllerBase;

/**
 * <pre>用户操作</pre>
 *
 * @author ManerFan 2016年4月8日
 */
@Controller
@RequestMapping("/user")
public class UserController extends ControllerBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoInterceptorHandler userInfo;

    @Autowired
    private RSAService rsaService;

    /**
     * <pre>
     * 修改用户邮箱
     * </pre>
     *
     * @param email
     * @return
     */
    @RequestMapping(path = "/modify/email", method = RequestMethod.POST)
    @ResponseBody
    public Object updateEmail(@RequestParam(name = "email", required = false) String email) {
        Map<String, Object> data = makeAjaxData();

        if (!StringUtils.hasText(email)) {
            email = null;
        }

        userRepository.updateEmail(email, userInfo.userName());

        data.put("email", email);
        return data;
    }

    /**
     * <pre>
     * 修改用户密码
     * </pre>
     *
     * @param email
     * @return
     */
    @RequestMapping(path = "/modify/passwd", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePasswd(@RequestParam("orgPasswd") String orgPasswd,
            @RequestParam("newPasswd") String newPasswd, HttpServletRequest request) {
        Map<String, Object> data = makeAjaxData();

        if (!StringUtils.hasText(orgPasswd)) {
            data.put(ERRMSG, "请填写原密码");
            return data;
        }

        if (!StringUtils.hasText(newPasswd)) {
            data.put(ERRMSG, "请填写新密码");
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
            orgPasswd = rsaService.decodeAddSalt(key, orgPasswd);
            newPasswd = rsaService.decodeAddSalt(key, newPasswd);

            String passwd = userRepository.findPasswordByName(userInfo.userName());
            if (!ObjectUtils.nullSafeEquals(orgPasswd, passwd)) {
                data.put(ERRMSG, "密码不正确");
                return data;
            }

            userRepository.updatePasswd(newPasswd, userInfo.userName());
        } catch (Exception e) {
            data.put(ERRMSG, "密码修改失败，请重新操作");
        }

        return data;
    }

}
