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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.common.utils.dao.common.CommonEntity;

/**
 * <pre>文章</pre>
 *
 * @author ManerFan 2016年2月24日
 */
@Entity(name = "ARTICLE")
public class ArticleEntity extends CommonEntity {

    /**
     * UID
     */
    private static final long serialVersionUID = 9125986139056496627L;

    /**
     * 标题
     */
    @Column(name = "TITLE", nullable = false)
    private String title;

    /**
     * 摘要
     */
    @Column(name = "SUMMARY", nullable = false, length = 1024)
    private String summary;

    /**
     * 正文[路径]
     */
    @Column(name = "CONTENT", nullable = false)
    private String content;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    private Date createTime;

    /**
     * 最后一次修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MOD_TIME", nullable = false)
    private Date lastModTime;

    /**
     * 阅读次数
     */
    @Column(name = "HITS", nullable = false, columnDefinition = "int default 0")
    private int hits;

    /**
     * 文章状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    private State state;

    /**
     * 文章类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private Type type;

    /**
     * 作者
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "AUTHOR", nullable = false/* 不能使用referencedColumnName，否则LAZY就不生效了 */)
    private UserEntity author;

    public static enum Type {
        /**
         * 博客文章
         */
        ARTICLE,
        /**
         * 关于
         */
        ABOUT,
        /**
         * 更新日志
         */
        CHANGELOG
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (state != other.state)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
