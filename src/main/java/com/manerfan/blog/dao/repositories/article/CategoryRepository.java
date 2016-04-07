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

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.manerfan.blog.dao.entities.article.CategoryEntity;
import com.manerfan.common.utils.dao.repositories.BasicJpaRepository;

/**
 * <pre>分类操作类接口</pre>
 *
 * @author ManerFan 2016年4月5日
 */
public interface CategoryRepository extends BasicJpaRepository<CategoryEntity, String> {

    /**
     * <pre>
     * 查找所有分类名在names中的分类名
     * </pre>
     *
     * @param   names   分类名
     * @return  分类名
     */
    @Query("select category.name from Category category where category.name in ?1")
    public List<String> findNameByNameIn(Collection<String> names);

    /**
     * <pre>
     * 根据分类名查询分类
     * </pre>
     *
     * @param   names 分类名
     * @return  分类
     */
    public List<CategoryEntity> findByNameIn(Collection<String> names);
}
