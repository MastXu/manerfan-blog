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
package com.manerfan.blog.service.article;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.lucene.LuceneManager;
import com.manerfan.common.utils.lucene.LuceneUtils;

/**
 * <pre>
 * Lucene全文检索Service
 * </pre>
 *
 * @author ManerFan 2016年5月24日
 */
@Service
public class LuceneService {

    @Autowired
    private LuceneManager luceneManager;

    /**
     * <pre>
     * 每周六凌晨3点触发索引合并
     * </pre>
     */
    @Async
    @Scheduled(cron = "0 0 3 * * SAT") /* 0秒 0分 3时 每天 每月 周六 */
    public void periodicMerge() {
        MLogger.ROOT_LOGGER.info("Start Merge Lucene Index!");
        try {
            /* 如果段数量大于5，则强制合并段，直到至多5个 */
            luceneManager.getIndexWriter().forceMerge(5, false);
        } catch (IOException e) {
            MLogger.ROOT_LOGGER.error("Error! When forceMerge Lucene Index!", e);
        }
    }

    public List<ArticleBO> morelike(String uid, int numHits) throws IOException {
        TopDocs topDocs = luceneManager.getIndexSearcher()
                .search(new TermQuery(new Term("uid", uid)), 1);
        if (null == topDocs) {
            return null;
        }

        if (topDocs.totalHits > 1) {
            throw new IllegalStateException(
                    "More than one results found by Article Uid [" + uid + "]");
        }

        return parseArticles(topDocs);
    }

    public TopDocs search(String keywords, ScoreDoc after, int numHits)
            throws IOException, ParseException {
        return LuceneUtils.searchAfter(luceneManager.getIndexSearcher(), after, numHits, keywords,
                "title", "summury", "content");
    }

    public List<ArticleBO> parseArticles(TopDocs docs) throws IOException {
        if (null == docs || ObjectUtils.isEmpty(docs.scoreDocs)) {
            return null;
        }

        List<ArticleBO> articles = new LinkedList<>();

        for (ScoreDoc doc : docs.scoreDocs) {
            Document document = luceneManager.getIndexSearcher().doc(doc.doc);
            ArticleBO article = parseDocument(document);
            if (null != article) {
                article.setScore(doc.score);
                articles.add(article);
            }
        }

        return articles;
    }

    private ArticleBO parseDocument(Document doc) {
        if (null == doc) {
            return null;
        }

        ArticleBO article = new ArticleBO();

        IndexableField uidField = doc.getField("uid");
        if (null != uidField) {
            article.setUid(uidField.stringValue());
        }

        IndexableField titleField = doc.getField("title");
        if (null != titleField) {
            article.setTitle(titleField.stringValue());
        }

        IndexableField summaryField = doc.getField("summary");
        if (null != summaryField) {
            article.setTitle(summaryField.stringValue());
        }

        IndexableField createTimeField = doc.getField("createTime");
        if (null != createTimeField) {
            article.setCreateTime(new Date(createTimeField.numericValue().longValue()));
        }

        IndexableField lastModTimeField = doc.getField("lastModTime");
        if (null != lastModTimeField) {
            article.setLastModTime(new Date(lastModTimeField.numericValue().longValue()));
        }

        IndexableField[] categoryFields = doc.getFields("category");
        if (!ObjectUtils.isEmpty(categoryFields)) {
            Set<String> categories = new HashSet<>();
            for (IndexableField categoryField : categoryFields) {
                categories.add(categoryField.stringValue());
            }
            article.setCategories(categories);
        }

        return article;
    }
}
