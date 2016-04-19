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

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.dao.entities.article.CategoryBO;
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
    @RequestMapping("/hots/{top}")
    @ResponseBody
    public Object hotCategories(@PathVariable("top") int top) {
        Map<String, Object> data = makeAjaxData();
        try {
            data.put("categories", categoryService.hotCategories(top));
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Hot Categories Error", e);
            data.put(ERRMSG, "获取常用分类失败");
        }

        return data;
    }

    /**
     * <pre>
     * 按照分类使用率排序，分页查询
     * </pre>
     *
     * @param   page    页数，从0开始
     * @param   size    每页个数
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object categoryList(@RequestParam int page, @RequestParam int size) {
        Map<String, Object> data = makeAjaxData();

        try {
            Page<CategoryBO> categories = categoryService.findCategoryList(page, size);
            data.put("categories", categories.getContent());
            data.put("totalPages", categories.getTotalPages());
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Categories Error", e);
            data.put(ERRMSG, "获取分类失败");
        }

        return data;
    }

    /**
     * <pre>
     * 根据分类名删除分类及其关联
     * </pre>
     *
     * @param name  分类名
     * @return
     */
    @RequestMapping("/delete/{name}")
    @ResponseBody
    public Object delete(@PathParam("name") String name) {
        Map<String, Object> data = makeAjaxData();

        try {
            categoryService.deleteByName(name);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Delete Category[{}] Error", name, e);
            data.put(ERRMSG, "删除分类失败");
        }

        return data;
    }
}
