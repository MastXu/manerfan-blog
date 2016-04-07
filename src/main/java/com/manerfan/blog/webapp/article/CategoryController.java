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
package com.manerfan.blog.webapp.article;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.service.article.CategoryService;
import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>文章分类</pre>
 *
 * @author ManerFan 2016年4月7日
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends ControllerBase {

    @Autowired
    private CategoryService categoryService;

    /**
     * <pre>
     * 获取使用率最高的几条分类信息
     * key:分类名
     * value:使用次数
     * </pre>
     *
     * @return
     */
    @RequestMapping("/hots")
    @ResponseBody
    public Object hotCategories() {
        Map<String, Object> data = makeAjaxData();
        try {
            data.put("categories", categoryService.hotCategories());
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Hot Categories Error", e);
            data.put(ERRMSG, "获取常用分类失败");
        }

        return data;
    }
}
