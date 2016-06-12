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

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.manerfan.blog.service.article.LuceneService;
import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>
 * 文章搜索
 * </pre>
 *
 * @author ManerFan 2016年5月24日
 */
@Controller
@RequestMapping("/article/search")
public class ArticleSearchController extends ControllerBase {

    @Autowired
    private LuceneService luceneService;

    @RequestMapping
    public ModelAndView search(@RequestParam("kw") String keywords) {
        ModelAndView mv = new ModelAndView("article/searchList");
        if (!StringUtils.hasText(keywords)) {
            mv.setViewName("article/search");
        } else {
            mv.addObject("keywords", keywords);
        }
        return mv;
    }

    @RequestMapping("/query")
    @ResponseBody
    public Object search(@RequestParam("kw") String keywords, @ModelAttribute PagedDoc after,
            @RequestParam int numHits) {
        Map<String, Object> data = makeAjaxData();

        try {
            if (null != after && -1 == after.doc) {
                after = null;
            }

            TopDocs topDocs = luceneService.search(keywords, after, numHits);
            if (null == topDocs || ObjectUtils.isEmpty(topDocs.scoreDocs)) {
                data.put("total", 0);
                return data;
            }

            data.put("total", topDocs.totalHits);
            data.put("articles", luceneService.parseArticles(topDocs));
            data.put("after", topDocs.scoreDocs[topDocs.scoreDocs.length - 1]);
        } catch (IOException | ParseException e) {
            data.put(ERRMSG, "Internal Error!");
            MLogger.ROOT_LOGGER.error("Some Error Occured when Search Lucene Index!", e);
        }

        return data;
    }

    @RequestMapping("/morelike")
    @ResponseBody
    public Object morelike(@RequestParam String uid, @RequestParam int numHits) {
        Map<String, Object> data = makeAjaxData();

        try {
            data.put("articles", luceneService.morelike(uid, numHits));
        } catch (Exception e) {
            data.put(ERRMSG, "Internal Error!");
            MLogger.ROOT_LOGGER.error("Some Error Occured when find MoreLike Articles", e);
        }

        return data;
    }

}
