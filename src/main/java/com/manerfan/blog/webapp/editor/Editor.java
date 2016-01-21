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
package com.manerfan.blog.webapp.editor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>欢迎页</pre>
 *
 * @author ManerFan 2016年1月18日
 */
@Controller
@RequestMapping("/")
public class Editor {

    @RequestMapping
    public ModelAndView landing() {
        return new ModelAndView("editor/landing");
    }

    @RequestMapping("/editor")
    public ModelAndView editor() {
        return new ModelAndView("editor/editor");
    }
    
    @RequestMapping("/viewer")
    public ModelAndView viewer() {
        return new ModelAndView("editor/viewer");
    }

}
