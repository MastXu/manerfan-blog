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
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;

import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleCategoryMap;
import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.entities.article.CategoryEntity;
import com.manerfan.blog.dao.repositories.UserRepository;
import com.manerfan.blog.dao.repositories.article.ArticleCategoryMapRepository;
import com.manerfan.blog.dao.repositories.article.ArticleRepository;
import com.manerfan.blog.dao.repositories.article.CategoryRepository;

/**
 * <pre>文章操作</pre>
 *
 * @author ManerFan 2016年4月3日
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public class ArticleService implements InitializingBean {

    private static final SimpleDateFormat PATH_SDF = new SimpleDateFormat("/yyyy/MM/");

    @Value("${article.basedir}")
    private String baseDir;

    private File articleDir;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleCategoryMapRepository articleCategoryMapRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * <pre>
     * 保存或更新文章,并返回文章ID
     * </pre>
     *
     * @param   article 文章
     * @return  文章标题
     * @throws IOException 
     */
    public long saveOrUpdate(ArticleBO article) throws IOException {
        /* 文章信息 */

        if (article.getUid() < 1) {
            article.setUid(System.currentTimeMillis());
        }

        boolean isNew = false;
        ArticleEntity articleEntity = articleRepository.findOneByUid(article.getUid());
        if (null == articleEntity) {
            articleEntity = new ArticleEntity();
            isNew = true;
        }

        BeanUtils.copyProperties(article, articleEntity, "uuid", "createTime", "lastModTime",
                "hits", "author");

        /* 作者 */

        if (isNew) {
            articleEntity.setAuthor(userRepository.findOneByName(article.getAuthor()));
        }

        articleRepository.save(articleEntity);

        /* 分类 */

        /* 不存在数据库中的分类 */
        List<CategoryEntity> categoryEntities = additionalCategories(article.getCategories());
        /* 保存分类 */
        categoryRepository.save(categoryEntities);

        // 重新设置分类

        /* 删除该文章的所有分类 */
        articleCategoryMapRepository.deleteByArticleUid(articleEntity.getUid());
        /* 重新保存分类 */
        saveCategoriesToArticle(categoryRepository.findByNameIn(article.getCategories()),
                articleEntity);

        // 保存文件 
        File dir = new File(articleDir, transPath(articleEntity.getUid()));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // markdown
        FileCopyUtils.copy(article.getContentWithMD(),
                new FileWriter(new File(dir, articleEntity.getUid() + ".md")));
        // html
        FileCopyUtils.copy(article.getContentWithHTML(),
                new FileWriter(new File(dir, articleEntity.getUid() + ".html")));
        // text
        FileCopyUtils.copy(article.getContentWithTEXT(),
                new FileWriter(new File(dir, articleEntity.getUid() + ".txt")));

        return articleEntity.getUid();
    }

    private List<CategoryEntity> additionalCategories(List<String> names) {
        if (ObjectUtils.isEmpty(names)) {
            return null;
        }

        /* 在数据库中已存在的分类 */
        List<String> categoryNames = categoryRepository.findNameByNameIn(names);
        List<CategoryEntity> additionalCategories = new LinkedList<>();
        for (String name : names) {
            if (!categoryNames.contains(name)) {
                /* 数据库中不包含此分类 */
                additionalCategories.add(new CategoryEntity(name));
            }
        }

        return additionalCategories;
    }

    private void saveCategoriesToArticle(List<CategoryEntity> categoryEntities,
            ArticleEntity articleEntity) {
        if (null == articleEntity || ObjectUtils.isEmpty(categoryEntities)) {
            return;
        }

        List<ArticleCategoryMap> articleCategoryMaps = new LinkedList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            articleCategoryMaps.add(new ArticleCategoryMap(categoryEntity, articleEntity));
        }

        articleCategoryMapRepository.save(articleCategoryMaps);
    }

    private String transPath(long name) {
        Date storeDate = new Date(name);
        return PATH_SDF.format(storeDate);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(baseDir);

        articleDir = new File(baseDir, "article");
        if (!articleDir.exists()) {
            Assert.isTrue(articleDir.mkdirs());
        }

        Assert.isTrue(articleDir.isDirectory());
    }
}
