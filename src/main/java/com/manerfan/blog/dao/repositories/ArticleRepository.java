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
package com.manerfan.blog.dao.repositories;

import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.common.utils.dao.repositories.BasicJpaRepository;

/**
 * <pre>文章操作接口</pre>
 *
 * @author ManerFan 2016年4月3日
 */
public interface ArticleRepository extends BasicJpaRepository<ArticleEntity, String> {

    /**
     * <pre>
     * 通过文章ID查找文章
     * </pre>
     *
     * @param   uid   文章ID
     * @return
     */
    public ArticleEntity findOneByUid(long uid);

}
