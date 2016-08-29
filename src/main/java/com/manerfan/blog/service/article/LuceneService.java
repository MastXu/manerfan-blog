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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.common.utils.lucene.LuceneIndexSnapshot;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneService.class);

    @Autowired
    private LuceneManager luceneManager;

    /**
     * <pre>
     * 每周六凌晨3点触发索引合并
     * </pre>
     */
    @Async
    @Scheduled(cron = "0 0 3 ? * SAT") /* [0秒 0分 3时 每天 每月 周六] 每个周六凌晨三点执行 */
    public void periodicMerge() {
        LOGGER.info("Start Merge Lucene Index!");
        try {
            /* 如果段数量大于5，则强制合并段，直到至多5个 */
            luceneManager.forceCommit();
            luceneManager.getIndexWriter().forceMerge(5, false);
            luceneManager.forceCommit();
        } catch (IOException e) {
            LOGGER.error("Error! When forceMerge Lucene Index!", e);
        }
    }

    /**
     * <pre>
     * 查找相似文章，并返回至多前numHits条记录
     * </pre>
     *
     * @param uid
     * @param numHits
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<ArticleBO> morelike(String uid, int numHits) throws IOException, ParseException {
        TopDocs docs = luceneManager.getIndexSearcher().search(new TermQuery(new Term("uid", uid)),
                1);
        if (null == docs) {
            return null;
        }

        if (docs.totalHits > 1) {
            throw new IllegalStateException(
                    "More than one results found by Article Uid [" + uid + "]");
        }

        if (ObjectUtils.isEmpty(docs.scoreDocs)) {
            return null;
        }

        TopDocs topDocs = LuceneUtils.moreLikeThis(luceneManager.getIndexSearcher(),
                docs.scoreDocs[0].doc, numHits + 1, "title", "summary", "content");

        return parseArticles(topDocs, uid);
    }

    /**
     * <pre>
     * 根据关键词，分页全文检索文章
     * </pre>
     *
     * @param keywords
     * @param after
     * @param numHits
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public TopDocs search(String keywords, ScoreDoc after, int numHits)
            throws IOException, ParseException {
        return LuceneUtils.searchAfter(luceneManager.getIndexSearcher(), after, numHits, keywords,
                "title", "summary", "content");
    }

    /**
     * <pre>
     * 将TopDocs解析为ArticleBOs，并排除uid在uidExclusions中的文章
     * </pre>
     *
     * @param docs
     * @param uidExclusions
     * @return
     * @throws IOException
     */
    public List<ArticleBO> parseArticles(TopDocs docs, String... uidExclusions) throws IOException {
        if (null == docs || ObjectUtils.isEmpty(docs.scoreDocs)) {
            return null;
        }

        Set<String> exclusions = new HashSet<>();
        if (!ObjectUtils.isEmpty(uidExclusions)) {
            exclusions.addAll(Arrays.asList(uidExclusions));
        }

        List<ArticleBO> articles = new LinkedList<>();

        for (ScoreDoc doc : docs.scoreDocs) {
            Document document = luceneManager.getIndexSearcher().doc(doc.doc);
            ArticleBO article = parseDocument(document);
            if (null != article && !exclusions.contains(article.getUid())) {
                article.setScore(doc.score);
                articles.add(article);
            }
        }

        return articles;
    }

    /**
     * <pre>
     * 将Document解析为ArticleBO
     * </pre>
     *
     * @param doc
     * @return
     */
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
            article.setSummary(summaryField.stringValue());
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

    public void backup(LuceneIndexSnapshot<Set<File>> snapshots) throws IOException {
        luceneManager.snapshot(snapshots);
    }
}
