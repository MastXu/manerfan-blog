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
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.manerfan.blog.dao.entities.article.ArchiveBO;
import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.entities.article.CategoryBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.blog.dao.repositories.BOUtils;
import com.manerfan.blog.dao.repositories.article.ArticleCategoryMapRepository;
import com.manerfan.blog.service.article.ArticleService;
import com.manerfan.blog.service.article.CategoryService;
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleCategoryMapRepository articleCategoryMapRepository;

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

                /* 上一篇 */
                ArticleEntity articlePrev = articleService.getNeighbor(uid, true);
                /* 下一篇 */
                ArticleEntity articleNext = articleService.getNeighbor(uid, false);

                mv.addObject("title", article.getTitle());
                mv.addObject("content", article.getContentWithHTML());
                mv.addObject("uid", uid);
                mv.addObject("hits", article.getHits());
                mv.addObject("createTime", sdf.format(article.getCreateTime()));

                mv.addObject("articlePrev", (null == articlePrev) ? null : articlePrev);
                mv.addObject("articleNext", (null == articleNext) ? null : articleNext);
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
        mv.addObject("displayname", "");
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
        mv.addObject("displayname", "文章分类");
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
        mv.addObject("displayname", "全文检索");
        mv.addObject("funcname", "search");
        mv.addObject("funcparam", key);
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
        mv.addObject("displayname", "归档");
        mv.addObject("funcname", "archive");
        mv.addObject("funcparam", year + "/" + month);
        return mv;
    }

    @RequestMapping("/timeline")
    public ModelAndView articleArchive() {
        ModelAndView mv = new ModelAndView("article/timeline");

        List<ArchiveBO> archives = articleService.findArchiveListAll();
        List<CategoryBO> categories = categoryService.findCategoryListAll();

        mv.addObject("archives", archives);
        mv.addObject("categories", categories);

        return mv;
    }

    /**
     * <pre>
     * 获取最近的top个归档数据
     * </pre>
     *
     * @param top
     * @return
     */
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

    /**
     * <pre>
     *  获取浏览次数最多的top个文章列表
     * </pre>
     *
     * @param top
     * @return
     */
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

    /**
     * <pre>
     * 获取所有的归档数据
     * </pre>
     *
     * @return
     */
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
        try {
            Page<ArticleEntity> articles = articleService.findArticleList(state, page, size);
            data.put("totalPages", articles.getTotalPages());
            data.put("articles", BOUtils.transFromPOs(articles.getContent(), ArticleBO.class,
                    ArticleEntity.class));
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Article List Error", e);
            data.put(ERRMSG, "获取文章列表失败");
        }
        return data;
    }

    /**
     * <pre>
     * 根据文章分类，按照创建时间降序排序，分页查询
     * </pre>
     *
     * @param name
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/category/list/{name}")
    @ResponseBody
    public Object articleListByCategory(@PathVariable String name, @RequestParam int page,
            @RequestParam int size) {
        Map<String, Object> data = makeAjaxData();

        try {
            List<ArticleEntity> articleEntities = articleCategoryMapRepository
                    .findByCategoryName(name, new QPageRequest(page, size));
            long totalPages = articleCategoryMapRepository.countByCategorName(name);

            data.put("totalPages", totalPages);
            data.put("articles",
                    BOUtils.transFromPOs(articleEntities, ArticleBO.class, ArticleEntity.class));
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Article List By Category[{}] Error", name, e);
            data.put(ERRMSG, "获取文章列表失败");
        }

        return data;
    }

    /**
     * <pre>
     * 根据归档时间，按照创建时间降序排序，分页查询
     * </pre>
     *
     * @param year
     * @param month
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/archive/list/{year}/{month}")
    @ResponseBody
    public Object articleListByArchive(@PathVariable String year, @PathVariable String month,
            @RequestParam int page, @RequestParam int size) {
        Map<String, Object> data = makeAjaxData();

        try {
            List<ArticleBO> articles = articleService.findByArchive(year, month, page, size);
            long totalPages = articleService.countByArchive(year, month);
            data.put("totalPages", totalPages);
            data.put("articles", articles);
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Get Article List By Archive[{}] Error", year + "/" + month,
                    e);
            data.put(ERRMSG, "获取文章列表失败");
        }

        return data;
    }
}
