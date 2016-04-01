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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>欢迎页</pre>
 *
 * @author ManerFan 2016年1月18日
 */
@Controller
@RequestMapping("/editor")
public class Editor {

    @RequestMapping
    public ModelAndView editor() {
        return new ModelAndView("editor/editor");
    }

    /**
     * <pre>
     * 编辑文章
     * </pre>
     *
     * @param fileId    文章ID
     * @return
     */
    @RequestMapping("/{fileId}")
    public ModelAndView edit(@PathVariable("fileId") long fileId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("fileId", fileId);
        mv.setViewName("editor/editor");
        return mv;
    }

}
