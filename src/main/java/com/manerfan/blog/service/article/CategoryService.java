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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manerfan.blog.dao.entities.article.CategoryBO;
import com.manerfan.blog.dao.entities.article.CategoryEntity;
import com.manerfan.blog.dao.repositories.article.ArticleCategoryMapRepository;
import com.manerfan.blog.dao.repositories.article.CategoryRepository;

/**
 * <pre>分类操作</pre>
 *
 * @author ManerFan 2016年4月7日
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleCategoryMapRepository articleCategoryMapRepository;

    /**
     * <pre>
     * 获取使用率最高的几条分类信息
     * (分类名, 使用次数)
     * </pre>
     *
     * @return
     */
    public List<CategoryBO> hotCategories(int top) {
        StringBuilder hql = new StringBuilder();
        hql.append("select new ").append(CategoryBO.class.getName());
        hql.append("(ac.category.name, count(ac.article)) "); /* 将结果封装成CategoryBO */
        hql.append("from ArticleCategoryMap ac "); /* 从中间关联表中查询 */
        hql.append("where 1=1 ");
        hql.append("group by ac.category "); /* 按分类分组 */
        hql.append("order by count(ac.article) desc"); /* 按使用率由高到低排序 */
        return articleCategoryMapRepository.find(hql.toString(), CategoryBO.class,
                new QPageRequest(0, top)/* 只取前top个 */, null);
    }

    /**
     * <pre>
     * 按使用率排序，分页查询
     * </pre>
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<CategoryBO> findCategoryListAll() {
        /* 查询使用中的分类名及使用的数量 */
        StringBuilder hql = new StringBuilder();
        hql.append("select new ").append(CategoryBO.class.getName());
        hql.append("(acm.category.name, count(acm.article)) "); /* 将结果封装成CategoryBO */
        hql.append("from ArticleCategoryMap acm "); /* 从中间关联表中查询 */
        hql.append("where 1 = 1 ");
        hql.append("group by acm.category "); /* 按分类分组 */
        hql.append("order by count(acm.article) desc"); /* 按使用率由高到低排序 */
        List<CategoryBO> categories = articleCategoryMapRepository.find(hql.toString(),
                CategoryBO.class, null);

        /* 统计已使用的分类名 */
        Set<String> names = new HashSet<>();
        categories.forEach(category -> names.add(category.getName()));

        /* 查询所有的分类 */
        List<CategoryEntity> entities = categoryRepository.findAll();
        /* 统计未使用的分类 */
        List<CategoryBO> additions = new LinkedList<>();
        entities.forEach(entity -> {
            if (!names.contains(entity.getName())) {
                additions.add(new CategoryBO(entity.getName(), 0));
            }
        });

        /* 将使用中的及未使用的并到一起 */
        categories.addAll(additions);
        return categories;
    }

    /**
     * <pre>
     * 根据分类名删除分类及其关联
     * </pre>
     *
     * @param name
     */
    public void deleteByName(String name) {
        articleCategoryMapRepository.deleteByCategoryName(name);
        categoryRepository.deleteByName(name);
    }

    /**
     * <pre>
     * 修改分类名
     * </pre>
     *
     * @param oldName
     * @param newName
     */
    public void updateByName(String oldName, String newName) {
        categoryRepository.updateByName(oldName, newName);
    }

    public boolean exists(String name) {
        return null != categoryRepository.findOneByName(name);
    }

}
