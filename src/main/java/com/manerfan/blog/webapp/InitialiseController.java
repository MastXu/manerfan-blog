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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.blog.dao.repositories.UserRepository;
import com.manerfan.blog.service.RSAService;

/**
 * <pre>网站初始化</pre>
 *
 * @author ManerFan 2016年1月28日
 */
@Controller
@RequestMapping("/init")
public class InitialiseController extends ControllerBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RSAService rsaService;

    @RequestMapping
    public ModelAndView init(HttpServletRequest request/*RedirectAttributes.addFlashAttribute*/) {
        ModelAndView mv = new ModelAndView("init");
        HttpSession session = request.getSession(true);

        Long adminNum = userRepository.countByRoleContaining(UserEntity.ROLE_ADMIN);
        if (null != adminNum && adminNum.longValue() > 0) {
            // 已经有管理员用户了，直接登录
            mv.setViewName("redirect:/login");
            session.setAttribute(LoginController.ERR_MSG, "请勿重复初始化");
            return mv;
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

    @RequestMapping("/check")
    public ModelAndView check(@ModelAttribute UserEntity user, RedirectAttributes attr,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession(true);

        // 从缓存中获取rsa私钥
        RSAPrivateKey key = rsaService.getPrivateKey(request.getSession(true).getId());
        if (null == key) {
            // 页面停留时间过长导致key过期
            mv.setViewName("redirect:/init");
            session.setAttribute(LoginController.ERR_MSG, "页面停留时间过长，请重新初始化");
            return mv;
        }

        try {
            user.setPassword(rsaService.decode(key, user.getPassword()));
        } catch (InternalAuthenticationServiceException e) {
            mv.setViewName("redirect:/init");
            session.setAttribute(LoginController.ERR_MSG, "初始化失败，请联系管理员或重新初始化");

            return mv;
        }

        user.setRole(UserEntity.ROLE_ADMIN);
        if (!StringUtils.hasText(user.getEmail())) {
            user.setEmail(null);
        }
        if (!StringUtils.hasText(user.getAvatar())) {
            user.setAvatar(null);
        }

        userRepository.save(user);
        mv.setViewName("redirect:/login");
        session.setAttribute(LoginController.INFO_MSG, "初始化完成，请登录");

        return mv;
    }

}
