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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manerfan.blog.dao.entities.article.CategoryBO;
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
    public Page<CategoryBO> findCategoryList(int pageNum, int pageSize) {
        Pageable pageable = new QPageRequest(pageNum, pageSize);
        StringBuilder hql = new StringBuilder();
        hql.append("select new ").append(CategoryBO.class.getName());
        hql.append("(ac.category.name, count(ac.article)) "); /* 将结果封装成CategoryBO */
        hql.append("from ArticleCategoryMap ac "); /* 从中间关联表中查询 */
        hql.append("where 1=1 ");
        hql.append("group by ac.category "); /* 按分类分组 */
        hql.append("order by count(ac.article) desc"); /* 按使用率由高到低排序 */
        List<CategoryBO> categories = articleCategoryMapRepository.find(hql.toString(),
                CategoryBO.class, pageable, null);
        long total = categoryRepository.count();

        return new PageImpl<>(categories, pageable, total);
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

}
