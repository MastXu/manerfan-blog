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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.manerfan.jpa.entities.CommonEntity;


/**
 * <pre>文章-分类 映射</pre>
 *
 * @author ManerFan 2016年2月24日
 */
@Entity(name = "ArticleCategoryMap")
@Table(name = "article_category_map", indexes = {
        @Index(name = "article_category_map_category_index", columnList = "category", unique = true),
        @Index(name = "article_category_map_article_index", columnList = "article", unique = true) })
/* 双主键必须设置复合主键 */
/* 复合主键不一定是自身，复合主键中需要且仅需要包含所有主键字段 */
public class ArticleCategoryMap extends CommonEntity {

    /**
     * UID
     */
    private static final long serialVersionUID = 4956329933692765943L;

    /**
     * 分类
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "category", /*referencedColumnName = "name",*/ nullable = false)
    private CategoryEntity category;

    /**
     * 文章
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "article", /*referencedColumnName = "uid",*/ nullable = false)
    private ArticleEntity article;

    public ArticleCategoryMap() {
    }

    public ArticleCategoryMap(CategoryEntity category, ArticleEntity article) {
        this.category = category;
        this.article = article;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public ArticleEntity getArticle() {
        return article;
    }

    public void setArticle(ArticleEntity article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((article == null) ? 0 : article.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArticleCategoryMap other = (ArticleCategoryMap) obj;
        if (article == null) {
            if (other.article != null)
                return false;
        } else if (!article.equals(other.article))
            return false;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        return true;
    }

}
