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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.common.utils.dao.entities.CommonEntity;

/**
 * <pre>文章</pre>
 *
 * @author ManerFan 2016年2月24日
 */
@Entity(name = "Article")
@Table(name = "article")
@EntityListeners({ AuditingEntityListener.class })
@Indexed(index = "article_index") /* 索引文件 */
@Analyzer(impl = SmartChineseAnalyzer.class) /* 中文分词器 */
public class ArticleEntity extends CommonEntity {

    /**
     * UID
     */
    private static final long serialVersionUID = 9125986139056496627L;

    /**
     * 标题
     */
    @Field(store = Store.NO)
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 摘要
     */
    @Field(store = Store.NO)
    @Column(name = "summary", length = 1024)
    private String summary;

    /**
     * 文章id
     */
    @Column(name = "uid", unique = true, nullable = false)
    private String uid;

    /**
     * 创建时间
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /**
     * 最后一次修改时间
     */
    @CreatedDate
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_time", nullable = false)
    private Date lastModTime;

    /**
     * 阅读次数
     */
    @Column(name = "hits", nullable = false, columnDefinition = "int default 0")
    private int hits = 0;

    /**
     * 文章状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    /**
     * 作者
     */
    @IndexedEmbedded(depth = 1)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "author", nullable = false/* 不能使用referencedColumnName，否则LAZY就不生效了 */)
    private UserEntity author;

    public static enum State {
        /**
         * 已发布
         */
        PUBLISHED,
        /**
         * 草稿
         */
        DRAFT,
        /**
         * 已删除
         */
        DELETED
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

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArticleEntity other = (ArticleEntity) obj;
        if (state != other.state)
            return false;
        if (uid == null) {
            if (other.uid != null)
                return false;
        } else if (!uid.equals(other.uid))
            return false;
        return true;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
