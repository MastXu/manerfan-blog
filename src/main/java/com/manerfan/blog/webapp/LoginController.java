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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.manerfan.blog.service.UserService;

/**
 * <pre>登陆</pre>
 *
 * @author ManerFan 2016年1月21日
 */
@Controller
@RequestMapping("/login")
public class LoginController extends ControllerBase {

    @Autowired
    private UserService userService;

    @RequestMapping
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("login");

        if (ObjectUtils.isEmpty(userService.findAdmins())) {
            // 还没有管理员用户，重定向到初始化
            mv.setViewName("redirect:/init");
            return mv;
        }

        if (!isAnonymous()) {
            // 已经登录
            mv.setViewName("redirect:/");
            return mv;
        }

        if (request.getParameterMap().containsKey("auth-fail")) {
            mv.addObject("error", "用户名或密码错误");
        }

        if (request.getParameterMap().containsKey("logout")) {
            mv.addObject("msg", "您已成功退出系统");
        }

        return mv;
    }
}
