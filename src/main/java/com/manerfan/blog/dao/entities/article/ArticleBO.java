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
package com.manerfan.blog.dao.entities.article;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.IndexOptions;
import org.springframework.beans.BeanUtils;

import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.common.utils.lucene.annotations.DocumentId;
import com.manerfan.common.utils.lucene.annotations.LuceneIndexed;
import com.manerfan.common.utils.lucene.annotations.NumericField;
import com.manerfan.common.utils.lucene.annotations.StringField;
import com.manerfan.common.utils.lucene.annotations.TextField;

/**
 * <pre>文章</pre>
 *
 * @author ManerFan 2016年4月1日
 */
@LuceneIndexed
public class ArticleBO implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 5183466177661564252L;

    /**
     * 标题
     */
    @TextField(name = "title", store = true, norms = true, boost = 1.5f)
    private String title;

    /**
     * 摘要
     */
    @TextField(name = "summary", store = true, norms = true, boost = 1.2f)
    private String summary;

    /**
     * 文章id
     */
    @DocumentId(name = "uid")
    private String uid;

    /**
     * markdown内容
     */
    private String contentWithMD;
    /**
     * html内容
     */
    private String contentWithHTML;
    /**
     * 纯文字内容
     */
    @TextField(name = "content", norms = true, boost = 1.0f)
    private String contentWithTEXT;

    /**
     * 创建时间
     */
    @NumericField(name = "createTime", store = true)
    private Date createTime;

    /**
     * 最后一次修改时间
     */
    @NumericField(name = "lastModTime", store = true, indexOptions = IndexOptions.NONE)
    private Date lastModTime;

    /**
     * 阅读次数
     */
    private int hits = 0;

    /**
     * 文章状态
     */
    private State state;

    /**
     * 作者
     */
    private String author;

    /**
     * 分类
     */
    @StringField(name = "category", store = true)
    private Set<String> categories = new HashSet<>();

    /**
     * lucene评分
     */
    private float score;

    public static ArticleBO transFromPO(ArticleEntity articleEntity) {
        if (null == articleEntity) {
            return null;
        }

        ArticleBO bo = new ArticleBO();
        BeanUtils.copyProperties(articleEntity, bo);
        bo.setAuthor(articleEntity.getAuthor().getName());

        return bo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModTime() {
        return lastModTime;
    }

    public void setLastModTime(Date lastModTime) {
        this.lastModTime = lastModTime;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getContentWithMD() {
        return contentWithMD;
    }

    public void setContentWithMD(String contentWithMD) {
        this.contentWithMD = contentWithMD;
    }

    public String getContentWithHTML() {
        return contentWithHTML;
    }

    public void setContentWithHTML(String contentWithHTML) {
        this.contentWithHTML = contentWithHTML;
    }

    public String getContentWithTEXT() {
        return contentWithTEXT;
    }

    public void setContentWithTEXT(String contentWithTEXT) {
        this.contentWithTEXT = contentWithTEXT;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

}
