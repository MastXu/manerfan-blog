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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.blog.dao.repositories.BOUtils;
import com.manerfan.blog.interceptor.UserInfoInterceptorHandler;
import com.manerfan.blog.service.article.ArticleService;
import com.manerfan.blog.webapp.ControllerBase;

/**
 * <pre>文章浏览</pre>
 *
 * @author ManerFan 2016年4月1日
 */
@Controller
@RequestMapping("/article")
public class ArticleViewController extends ControllerBase {

    @Autowired
    private UserInfoInterceptorHandler userInfo;

    @Autowired
    private ArticleService articleService;

    /**
     * <pre>
     * 浏览文章
     * </pre>
     *
     * @param mv
     */
    @RequestMapping("/{uid}")
    public void article(@PathVariable String uid, ModelAndView mv) {

    }

    /**
     * <pre>
     * 按创建时间浏览文章列表
     * </pre>
     *
     * @param mv
     */
    @RequestMapping
    public void articleList(ModelAndView mv) {

    }

    /**
     * <pre>
     * 按分类浏览文章列表
     * </pre>
     *
     * @param name
     * @param mv
     */
    @RequestMapping("/category/{name}")
    public void articleCategoryList(@PathVariable String name, ModelAndView mv) {

    }

    /**
     * <pre>
     * 按关键词搜索文章列表
     * </pre>
     *
     * @param key
     * @param mv
     */
    @RequestMapping("/search/{key}")
    public void articleSeartchList(@PathVariable String key, ModelAndView mv) {

    }

    /**
     * <pre>
     * 按归档浏览文章列表
     * </pre>
     *
     * @param year
     * @param month
     * @param mv
     */
    @RequestMapping("/archive/{year}/{month}")
    public void articleArchiveList(@PathVariable String year, @PathVariable String month,
            ModelAndView mv) {

    }

    /**
     * <pre>
     * 根据文章状态，按照创建时间降序排序，分页查询
     * </pre>
     *
     * @param   state   文章状态
     * @param   page    页数，从0开始
     * @param   size    每页个数
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object articleList(
            @RequestParam(required = false, defaultValue = "PUBLISHED") State state,
            @RequestParam int page, @RequestParam int size) {
        Map<String, Object> data = makeAjaxData();
        Page<ArticleEntity> articles = articleService.findArticleList(state, page, size);
        data.put("totalPages", articles.getTotalPages());
        data.put("articles",
                BOUtils.transFromPOs(articles.getContent(), ArticleBO.class, ArticleEntity.class));
        return data;
    }
}
