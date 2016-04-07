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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.blog.interceptor.UserInfoInterceptorHandler;
import com.manerfan.blog.service.article.ArticleService;
import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>文章操作</pre>
 *
 * @author ManerFan 2016年4月1日
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends ControllerBase {

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
            MLogger.ROOT_LOGGER.error("", e);
            data.put(ERRMSG, "写文章错误");
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
