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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.manerfan.blog.dao.entities.article.ArchiveBO;
import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.blog.dao.repositories.BOUtils;
import com.manerfan.blog.service.article.ArticleService;
import com.manerfan.blog.service.article.ArticleService.FileType;
import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>文章浏览</pre>
 *
 * @author ManerFan 2016年4月1日
 */
@Controller
@RequestMapping("/article")
public class ArticleViewController extends ControllerBase {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public ModelAndView article(@PathVariable String uid) {
        ModelAndView mv = new ModelAndView("article/article");
        try {
            ArticleBO article = articleService.get(uid, FileType.html);
            if (null == article) {
                mv.setViewName("redirect:/article");
            } else {
                articleService.addArticleHits(uid);

                mv.addObject("title", article.getTitle());
                mv.addObject("content", article.getContentWithHTML());
                mv.addObject("uid", uid);
                mv.addObject("hits", article.getHits());
                mv.addObject("createTime", sdf.format(article.getCreateTime()));
            }
        } catch (IOException e) {
            MLogger.ROOT_LOGGER.error("Get Article[{}] Error!", uid, e);
            mv.setViewName("redirect:/article");
        }

        return mv;
    }

    /**
     * <pre>
     * 按创建时间浏览文章列表
     * </pre>
     */
    @RequestMapping
    public ModelAndView articleList() {
        ModelAndView mv = new ModelAndView("article/articleList");
        mv.addObject("funcname", "article");
        mv.addObject("funcparam", "");
        return mv;
    }

    /**
     * <pre>
     * 按分类浏览文章列表
     * </pre>
     *
     * @param name
     */
    @RequestMapping("/category/{name}")
    public ModelAndView articleCategoryList(@PathVariable String name) {
        ModelAndView mv = new ModelAndView("article/articleList");
        mv.addObject("funcname", "category");
        mv.addObject("funcparam", name);
        return mv;
    }

    /**
     * <pre>
     * 按关键词搜索文章列表
     * </pre>
     *
     * @param key
     */
    @RequestMapping("/search/{key}")
    public ModelAndView articleSeartchList(@PathVariable String key) {
        ModelAndView mv = new ModelAndView("article/articleList");
        mv.addObject("funcname", "search");
        mv.addObject("funcparam", key);
        return mv;
    }

    @RequestMapping({ "/archive", "/category" })
    public ModelAndView articleArchive() {
        ModelAndView mv = new ModelAndView("article/archive");
        return mv;
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
    public ModelAndView articleArchiveList(@PathVariable String year, @PathVariable String month) {
        ModelAndView mv = new ModelAndView("article/articleList");
        mv.addObject("funcname", "archive");
        mv.addObject("funcparam", year + "/" + month);
        return mv;
    }

    @RequestMapping("/archive/hots/{top}")
    @ResponseBody
    public Object hotArchives(@PathVariable("top") int top) {
        Map<String, Object> data = makeAjaxData();

        try {
            List<ArchiveBO> archives = articleService.hotsArchives(top);
            data.put("archives", archives);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Hots Archivies Error", e);
            data.put(ERRMSG, "获取归档失败");
        }

        return data;
    }

    @RequestMapping("/hit/hots/{top}")
    @ResponseBody
    public Object hotHits(@PathVariable("top") int top) {
        Map<String, Object> data = makeAjaxData();

        try {
            List<ArticleBO> articles = articleService.hotsHits(top);
            data.put("articles", articles);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Hots Articles Error", e);
            data.put(ERRMSG, "获取文章阅读排行失败");
        }

        return data;
    }

    @RequestMapping("/archive/list/all")
    @ResponseBody
    public Object archiveList() {
        Map<String, Object> data = makeAjaxData();

        try {
            List<ArchiveBO> archives = articleService.findArchiveListAll();
            data.put("archives", archives);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Archives Error", e);
            data.put(ERRMSG, "获取归档失败");
        }

        return data;
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
