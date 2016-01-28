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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.blog.service.UserService;

/**
 * <pre>网站初始化</pre>
 *
 * @author ManerFan 2016年1月28日
 */
@Controller
@RequestMapping("/init")
public class Initialise {

    @Autowired
    private UserService userService;

    @RequestMapping
    public ModelAndView init(RedirectAttributes attr) {
        ModelAndView mv = new ModelAndView("init");

        if (!ObjectUtils.isEmpty(userService.findAdmins())) {
            // 已经有管理员用户了，直接登录
            mv.setViewName("redirect:/login");
            attr.addFlashAttribute("error", "请勿重复初始化");
            return mv;
        }

        return mv;
    }

    @RequestMapping("/check")
    public ModelAndView check(@ModelAttribute UserEntity user, RedirectAttributes attr) {
        ModelAndView mv = new ModelAndView();

        if (userService.createAdmin(user)) {
            mv.setViewName("redirect:/login");
            attr.addFlashAttribute("msg", "初始化完成，请登录");
        } else {
            mv.setViewName("redirect:/init");
            attr.addFlashAttribute("error", "初始化失败，请重试");
        }

        return mv;
    }

}
