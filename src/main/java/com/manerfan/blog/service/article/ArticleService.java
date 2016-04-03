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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.repositories.ArticleRepository;
import com.manerfan.blog.dao.repositories.UserRepository;

/**
 * <pre>文章操作</pre>
 *
 * @author ManerFan 2016年4月3日
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * <pre>
     * 保存或更新文章,并返回文章ID
     * </pre>
     *
     * @param   article 文章
     * @return  文章标题
     */
    public long saveOrUpdate(ArticleBO article) {
        /* 文章信息 */

        if (article.getUid() < 1) {
            article.setUid(System.currentTimeMillis());
        }

        boolean isNew = true;
        ArticleEntity articleEntity = articleRepository.findOneByUid(article.getUid());
        if (null == articleEntity) {
            articleEntity = new ArticleEntity();
            isNew = false;
        }

        BeanUtils.copyProperties(article, articleEntity, "uuid", "createTime", "lastModTime",
                "hits", "author");

        /* 作者 */

        if (!isNew) {
            articleEntity.setAuthor(userRepository.findOneByName(article.getAuthor()));
        }

        /* 分类 */

        // TODO 重新设置分类

        articleRepository.save(articleEntity);
        return article.getUid();
    }
}
