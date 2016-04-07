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
package com.manerfan.blog.dao.repositories.article;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.manerfan.blog.dao.entities.article.ArticleCategoryMap;
import com.manerfan.blog.dao.entities.article.CategoryEntity;
import com.manerfan.common.utils.dao.repositories.BasicJpaRepository;

/**
 * <pre>文章-分类映射操作接口</pre>
 *
 * @author ManerFan 2016年4月5日
 */
public interface ArticleCategoryMapRepository
        extends BasicJpaRepository<ArticleCategoryMap, String> {

    /**
     * <pre>
     * 查询指定文章的所有分类
     * </pre>
     *
     * @param   uid 文章ID
     * @return  该文章的分类
     */
    @Query("select map.category from ArticleCategoryMap map where map.article.uuid = (select article.uuid from Article article where article.uid = ?1)")
    public List<CategoryEntity> findByArticleUid(String uid);

    /**
     * <pre>
     * 删除指定文章的所有映射
     * </pre>
     *
     * @param uid   文章id
     */
    @Modifying
    @Query("delete from ArticleCategoryMap map where map.article.uuid = (select article.uuid from Article article where article.uid = ?1)")
    void deleteByArticleUid(String uid);

    /**
     * <pre>
     * 删除指定分类的所有映射
     * </pre>
     *
     * @param name   分类名
     */
    @Modifying
    @Query("delete from ArticleCategoryMap map where map.category.uuid = (select category.uuid from Category category where category.name = ?1)")
    void deleteByCategoryName(String name);

    /**
     * <pre>
     * 按照分类名进行分组，统计出每种分类的个数
     * </pre>
     *
     * @return  每种分类的个数
     */
    @Query("select new map(ac.category.name, count(ac.article)) from ArticleCategoryMap ac where 1=1 group by ac.category order by count(ac.article) desc")
    Map<String, Integer> findCategoryByGroup();
}
