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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.blog.interceptor.UserInfoInterceptorHandler;
import com.manerfan.blog.service.article.ArticleService;
import com.manerfan.blog.service.article.ArticleService.FileType;
import com.manerfan.blog.webapp.ControllerBase;

/**
 * <pre>文章操作</pre>
 *
 * @author ManerFan 2016年4月1日
 */
@Controller
@RequestMapping("/article")
public class ArticleOprController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleOprController.class);

    @Autowired
    private UserInfoInterceptorHandler userInfo;

    @Autowired
    private ArticleService articleService;

    /**
     * <pre>
     * 发布文章/保存草稿
     * </pre>
     *
     * @param article   文章内容
     * @return
     */
    @RequestMapping("/publish")
    @ResponseBody
    public Object publish(@ModelAttribute ArticleBO article) {
        Map<String, Object> data = makeAjaxData();

        try {
            String errMsg = checkArticle(article);
            if (StringUtils.hasText(errMsg)) {
                /* 有错误 */
                data.put(ERRMSG, errMsg);
                return data;
            }

            /**
             * 设置作者
             */
            article.setAuthor(userInfo.userName());

            data.put("uid", articleService.saveOrUpdate(article));
        } catch (Exception e) {
            LOGGER.error("Save Article Error!", e);
            data.put(ERRMSG, "写文章错误");
        }

        return data;
    }

    /**
     * <pre>
     * 获取文章
     * </pre>
     *
     * @param   uid     文章标识
     * @param   type    文章类型
     * @return
     */
    @RequestMapping("/content/{uid}/{type}")
    @ResponseBody
    public Object article(@PathVariable("uid") String uid, @PathVariable("type") FileType type) {
        Map<String, Object> data = makeAjaxData();

        try {
            ArticleBO article = articleService.get(uid, type);
            data.put("article", article);
        } catch (IOException e) {
            LOGGER.error("Read Article File [{}] Error!", uid, e);
            data.put(ERRMSG, "读取文章错误");
        }

        return data;
    }

    /**
     * <pre>
     * 删除文章
     * </pre>
     *
     * @param   uid 文章标识
     * @return
     */
    @RequestMapping("/delete/{uid}")
    @ResponseBody
    public Object delete(@PathVariable String uid) {
        Map<String, Object> data = makeAjaxData();
        try {
            articleService.deleteArticle(uid);
        } catch (Exception e) {
            LOGGER.error("Delete Article File [{}] Error!", uid, e);
            data.put(ERRMSG, "删除文章错误");
        }
        return data;
    }

    /**
     * <pre>
     * 修改文章状态 发布/草稿/回收站
     * </pre>
     *
     * @param state
     * @param uid
     * @return
     */
    @RequestMapping("/update/state")
    @ResponseBody
    public Object updateState(@RequestParam State state, @RequestParam String uid) {
        Map<String, Object> data = makeAjaxData();

        try {
            articleService.updateArticleState(state, uid);
        } catch (Exception e) {
            LOGGER.error("Cannot Update the state of Article {} to {}.",
                    new Object[] { uid, state, e });
            data.put(ERRMSG, "数据库错误");
        }
        return data;
    }

    /**
     * <pre>
     * 检查文章
     * </pre>
     *
     * @param   article   文章
     * @return  错误信息    
     */
    private String checkArticle(ArticleBO article) {
        if (!StringUtils.hasText(article.getTitle())) {
            /* 没有标题 */
            return "请填写文章标题";
        }

        if (!StringUtils.hasText(article.getContentWithMD())
                || !StringUtils.hasText(article.getContentWithHTML())
                || !StringUtils.hasText(article.getContentWithTEXT())) {
            /* 没有内容 */
            return "请填写文章内容";
        }

        switch (article.getState()) {
            case PUBLISHED:
                /* 发布 */
                if (!StringUtils.hasText(article.getSummary())) {
                    return "请填写摘要";
                }
                if (ObjectUtils.isEmpty(article.getCategories())) {
                    return "请填写分类";
                }
                article.setState(State.PUBLISHED);
                break;
            case DRAFT:
                /* 草稿 */
                article.setState(State.DRAFT);
                break;
            case DELETED:
                /* 删除 */
                return "不能在此处执行文章删除操作";
            default:
                /* 未知 */
                return "未知操作";
        }

        return null;
    }

}
