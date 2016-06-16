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

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>错误页面</pre>
 *
 * @author ManerFan 2016年6月16日
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends ControllerBase {

    @RequestMapping({ "/400", "/404" })
    public ModelAndView error404(HttpServletResponse response) {
        /* 目前只能使用这种办法强制设置Content-Type */
        response.setContentType("text/html;charset=UTF-8");
        return new ModelAndView("error/404.html");
    }

    @RequestMapping("/500")
    public ModelAndView error500(HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        return new ModelAndView("error/500.html");
    }

}
