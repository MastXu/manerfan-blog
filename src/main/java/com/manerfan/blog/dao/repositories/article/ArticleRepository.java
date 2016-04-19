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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.common.utils.dao.repositories.BasicJpaRepository;

/**
 * <pre>文章操作接口</pre>
 *
 * @author ManerFan 2016年4月3日
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public interface ArticleRepository extends BasicJpaRepository<ArticleEntity, String> {

    /**
     * <pre>
     * 通过文章ID查找文章
     * </pre>
     *
     * @param   uid   文章ID
     * @return  文章
     */
    public ArticleEntity findOneByUid(String uid);

    /**
     * <pre>
     * 通过文章ID删除文章
     * </pre>
     *
     * @param uid   文章ID
     */
    public void deleteByUid(String uid);

    /**
     * <pre>
     * 设置文章的状态
     * hql中，表名可使用@Entity指定的名称
     * </pre>
     *
     * @param   state   文章状态
     * @param   uid     文章ID
     */
    @Modifying
    @Query("update Article article set article.state = ?1 where article.uid = ?2")
    public void updateArticleState(State state, String uid);

    /**
     * <pre>
     * 按照创建时间降序排序分页查询
     * </pre>
     *
     * @param pageable
     * @return
     */
    public Page<ArticleEntity> findAllByStateOrderByCreateTimeDesc(State state, Pageable pageable);

}
