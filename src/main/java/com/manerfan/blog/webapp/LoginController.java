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
package com.manerfan.blog.webapp;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.blog.dao.repositories.UserRepository;
import com.manerfan.blog.interceptor.UserInfoInterceptorHandler;
import com.manerfan.blog.service.RSAService;

/**
 * <pre>登陆</pre>
 *
 * @author ManerFan 2016年1月21日
 */
@Controller
@RequestMapping("/login")
public class LoginController extends ControllerBase {

    public static final String INFO_MSG = "INFO_MSG";
    public static final String ERR_MSG = "ERR_MSG";

    @Autowired
    private UserInfoInterceptorHandler userInfo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RSAService rsaService;

    @RequestMapping
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("login");
        HttpSession session = request.getSession(true);

        Long adminNum = userRepository.countByRoleContaining(UserEntity.ROLE_ADMIN);
        if (null == adminNum || adminNum.longValue() < 1) {
            // 还没有管理员用户，重定向到初始化
            mv.setViewName("redirect:/init");
            return mv;
        }

        if (!userInfo.isAnonymous()) {
            // 已经登录
            mv.setViewName("redirect:/");
            return mv;
        }

        String info = (String) session.getAttribute(INFO_MSG);
        String err = (String) session.getAttribute(ERR_MSG);
        session.removeAttribute(INFO_MSG);
        session.removeAttribute(ERR_MSG);

        if (StringUtils.hasText(info)) {
            mv.addObject("msg", info);
        }

        if (StringUtils.hasText(err)) {
            mv.addObject("err", err);
        }

        // 获取rsa密钥
        KeyPair keyPair = rsaService.getKeyPair();
        if (null != keyPair) {
            // 将rsa公钥传给页面
            RSAPublicKey pk = (RSAPublicKey) keyPair.getPublic();
            mv.addObject("exponent", pk.getPublicExponent().toString(16));
            mv.addObject("modulus", pk.getModulus().toString(16));

            // 将rsa私钥放入缓存
            String id = request.getSession(true).getId();
            rsaService.putPrivateKey(id, (RSAPrivateKey) keyPair.getPrivate());
        }

        return mv;
    }

    /**
     * <pre>
     * 获取公钥
     * </pre>
     *
     * @return
     */
    @RequestMapping("/publickey")
    @ResponseBody
    public Object publicKey(HttpServletRequest request) {
        Map<String, Object> data = makeAjaxData();

        // 获取rsa密钥
        KeyPair keyPair = rsaService.getKeyPair();
        if (null != keyPair) {
            // 将rsa公钥传给页面
            RSAPublicKey pk = (RSAPublicKey) keyPair.getPublic();
            data.put("exponent", pk.getPublicExponent().toString(16));
            data.put("modulus", pk.getModulus().toString(16));

            // 将rsa私钥放入缓存
            String id = request.getSession(true).getId();
            rsaService.putPrivateKey(id, (RSAPrivateKey) keyPair.getPrivate());
        } else {
            data.put(ERRMSG, "生成公钥失败");
        }

        return data;
    }
}
