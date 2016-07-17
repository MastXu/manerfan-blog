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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.manerfan.blog.dao.entities.article.ArchiveBO;
import com.manerfan.blog.dao.entities.article.ArticleBO;
import com.manerfan.blog.dao.entities.article.ArticleCategoryMap;
import com.manerfan.blog.dao.entities.article.ArticleEntity;
import com.manerfan.blog.dao.entities.article.ArticleEntity.State;
import com.manerfan.blog.dao.entities.article.CategoryEntity;
import com.manerfan.blog.dao.repositories.UserRepository;
import com.manerfan.blog.dao.repositories.article.ArticleCategoryMapRepository;
import com.manerfan.blog.dao.repositories.article.ArticleRepository;
import com.manerfan.blog.dao.repositories.article.CategoryRepository;
import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.lucene.LuceneManager;
import com.manerfan.common.utils.lucene.LuceneUtils;
import com.manerfan.common.utils.lucene.annotations.manager.LuceneCommit;
import com.manerfan.spring.configuration.ResourceLocation;

/**
 * <pre>文章操作</pre>
 *
 * @author ManerFan 2016年4月3日
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public class ArticleService implements InitializingBean {
    private static final SimpleDateFormat NAME_SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat PATH_SDF = new SimpleDateFormat("/yyyy/MM/");

    @Autowired
    private ResourceLocation resourceLocation;

    private File defaultArticle;

    @Autowired
    private EhCacheCacheManager cacheCacheManager;
    private Cache cache;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleCategoryMapRepository articleCategoryMapRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LuceneManager luceneManager;

    /**
     * <pre>
     * 保存或更新文章,并返回文章ID
     * </pre>
     *
     * @param   article 文章
     * @return  文章标题
     * @throws IOException 
     * @throws ParseException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    /* 这里需要销毁多个缓存，就不使用注解了 @CacheEvict(cacheNames = "resources-cache", beforeInvocation = true, keyGenerator = "ResourceKeyGenerator")*/
    @LuceneCommit(managerBeanName = "articleLuceneManager")
    public String saveOrUpdate(ArticleBO article) throws IOException, ParseException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        /* 文章信息 */

        if (!StringUtils.hasText(article.getUid())) {
            article.setUid(NAME_SDF.format(Calendar.getInstance().getTime()));
        }

        evictArticleCache(article.getUid());

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
        File dir = new File(resourceLocation.getArticleDir(), transPath(articleEntity.getUid()));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // markdown
        FileCopyUtils.copy(article.getContentWithMD(),
                new FileWriter(new File(dir, articleEntity.getUid() + FileType.markdown.type)));
        // html
        FileCopyUtils.copy(article.getContentWithHTML(),
                new FileWriter(new File(dir, articleEntity.getUid() + FileType.html.type)));
        // text
        FileCopyUtils.copy(article.getContentWithTEXT(),
                new FileWriter(new File(dir, articleEntity.getUid() + FileType.txt.type)));

        /* 保存/更新 索引 */
        if (State.PUBLISHED.equals(articleEntity.getState())) {
            // 非发布状态，删除索引
            article.setCreateTime(articleEntity.getCreateTime());
            article.setLastModTime(articleEntity.getLastModTime());
            LuceneUtils.addOrUpdateIndex(luceneManager.getIndexWriter(), article);
        } else {
            // 发布状态，更新/建立索引
            LuceneUtils.deleteIndex(luceneManager.getIndexWriter(), "uid", articleEntity.getUid());
        }

        return articleEntity.getUid();
    }

    /**
     * <pre>
     * 指定分类名，查询数据库中不存在的项
     * </pre>
     *
     * @param names 分类名
     * @return
     */
    private List<CategoryEntity> additionalCategories(Set<String> names) {
        if (ObjectUtils.isEmpty(names)) {
            return null;
        }

        /* 在数据库中已存在的分类 */
        List<String> categoryNames = categoryRepository.findNameByNameIn(names);
        List<CategoryEntity> additionalCategories = new LinkedList<>();
        names.stream().filter(name -> !categoryNames.contains(name)) /* 数据库中不包含此分类 */
                .forEach(name -> additionalCategories.add(new CategoryEntity(name)));

        return additionalCategories;
    }

    /**
     * <pre>
     * 将分类与文章关联存储
     * </pre>
     *
     * @param categoryEntities  分类
     * @param articleEntity     文章
     */
    private void saveCategoriesToArticle(List<CategoryEntity> categoryEntities,
            ArticleEntity articleEntity) {
        if (null == articleEntity || ObjectUtils.isEmpty(categoryEntities)) {
            return;
        }

        List<ArticleCategoryMap> articleCategoryMaps = new LinkedList<>();
        categoryEntities.forEach(categoryEntity -> articleCategoryMaps
                .add(new ArticleCategoryMap(categoryEntity, articleEntity)));

        articleCategoryMapRepository.save(articleCategoryMaps);
    }

    private String transPath(String name) {
        try {
            Date storeDate = NAME_SDF.parse(name);
            return PATH_SDF.format(storeDate);
        } catch (ParseException e) {
            MLogger.ROOT_LOGGER.error("", e);
            return "/";
        }
    }

    /**
     * <pre>
     * 获取文章内容
     * </pre>
     *
     * @param   uid     文章标识
     * @param   type    文章类型
     * @return
     * @throws IOException
     */
    @Cacheable(cacheNames = "resources-cache", keyGenerator = "ResourceKeyGenerator")
    public ArticleBO get(String uid, FileType type) throws IOException {
        ArticleEntity articleEntity = articleRepository.findOneByUid(uid);
        if (null == articleEntity) {
            return null;
        }

        /* 文章基本信息 */
        ArticleBO article = ArticleBO.transFromPO(articleEntity);

        /* 文章分类信息 */
        List<CategoryEntity> categoryEntities = articleCategoryMapRepository.findByArticleUid(uid);
        categoryEntities.forEach(category -> article.getCategories().add(category.getName()));

        String content = getContent(uid, type);
        switch (type) {
            case markdown:
                article.setContentWithMD(content);
                break;
            case html:
                article.setContentWithHTML(content);
                break;
            case txt:
                article.setContentWithTEXT(content);
                break;
            default:
                break;
        }

        return article;
    }

    private String getContent(String uid, FileType type) throws IOException, FileNotFoundException {
        /* 读取文章内容 */
        Pattern pattern = Pattern.compile(uid + "\\" + type.type());
        String path = transPath(uid);
        File searchDir = new File(resourceLocation.getArticleDir(), path);
        File[] files = searchDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });

        File articleFile = defaultArticle;
        if (!ObjectUtils.isEmpty(files)) {
            articleFile = files[0];
        }

        String content = FileCopyUtils.copyToString(new FileReader(articleFile));
        return content;
    }

    /**
     * <pre>
     * 更新文章状态
     * </pre>
     *
     * @param   state   文章状态
     * @param   uid     文章标识
     * @throws IOException 
     */
    @LuceneCommit(managerBeanName = "articleLuceneManager")
    public void updateArticleState(State state, String uid) throws IOException {
        articleRepository.updateArticleState(state, uid);
        evictArticleCache(uid);

        if (!State.PUBLISHED.equals(state)) {
            // 非发布状态，将索引删除
            LuceneUtils.deleteIndex(luceneManager.getIndexWriter(), "uid", uid);
        } else {
            // 发布状态，重新建立索引
            reIndexArticle(articleRepository.findOneByUid(uid),
                    articleCategoryMapRepository.findByArticleUid(uid));
        }
    }

    /**
     * <pre>
     * 重新建立索引
     * </pre>
     *
     * @param article
     * @param categories
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected void reIndexArticle(ArticleEntity article, List<CategoryEntity> categories)
            throws FileNotFoundException, IOException {
        if (null == article) {
            return;
        }

        ArticleBO bo = new ArticleBO();
        BeanUtils.copyProperties(article, bo);
        bo.setContentWithTEXT(getContent(article.getUid(), FileType.txt));

        if (!ObjectUtils.isEmpty(categories)) {
            categories.forEach(category -> bo.getCategories().add(category.getName()));
        }
    }

    public static enum FileType {
                                 markdown(".md"), html(".html"), txt(".txt");

        private String type;

        private FileType(String type) {
            this.type = type;
        }

        public String type() {
            return type;
        }
    }

    /**
     * <pre>
     * 根据文章创建时间，按照降序排序，分页查询
     * </pre>
     *
     * @param   pageNum  第几页（从0开始）
     * @param   pageSize 每页条数
     * @return
     */
    public Page<ArticleEntity> findArticleList(State state, int pageNum, int pageSize) {
        return articleRepository.findAllByStateOrderByCreateTimeDesc(state,
                new QPageRequest(pageNum, pageSize));
    }

    /**
     * <pre>
     * 删除文章
     * </pre>
     *
     * @param   uid 文章标识
     * @throws IOException 
     */
    @LuceneCommit(managerBeanName = "articleLuceneManager")
    public void deleteArticle(String uid) throws IOException {
        evictArticleCache(uid);

        articleCategoryMapRepository.deleteByArticleUid(uid);
        articleRepository.deleteByUid(uid);

        Pattern pattern = Pattern.compile(uid + "\\.\\w+");
        String path = transPath(uid);
        File searchDir = new File(resourceLocation.getArticleDir(), path);
        File[] files = searchDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });

        if (!ObjectUtils.isEmpty(files)) {
            Arrays.asList(files).forEach(file -> {
                file.delete(); // 删除文章文件
                if (ObjectUtils.isEmpty(file.getParentFile().list())) {
                    // 如果所在文件夹已空，则删除
                    file.getParentFile().delete();
                }
            });
        }

        LuceneUtils.deleteIndex(luceneManager.getIndexWriter(), "uid", uid);
    }

    /**
     * <pre>
     * 文章阅读数量加一
     * </pre>
     *
     * @param uid
     */
    public void addArticleHits(String uid) {
        articleRepository.addArticleHits(uid);
    }

    /**
     * <pre>
     * 清除缓存 此处为@CacheEvict的替代方案(@CacheEvict仅能指定一个key)
     * </pre>
     *
     * @param uid
     */
    private void evictArticleCache(String uid) {
        /* 清除缓存 此处为@CacheEvict的替代方案(@CacheEvict仅能指定一个key) */
        Arrays.asList(FileType.values())
                .forEach(type -> cache.evict("ARTICLE" + uid + type.toString()));
    }

    /**
     * <pre>
     * 按月进行文章个数统计，获取最新的top条
     * 
     * 这里由于使用H2DB，HQL不太好用，只得直接上原生SQL
     * </pre>
     *
     * @param top
     * @return
     */
    public List<ArchiveBO> hotsArchives(int top) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select formatdatetime(article.create_time,'YYYY/MM') as df, count(article.uid) "); /* 将结果封装成ArchiveBO */
        sql.append("from article "); /* 从文章中查询 */
        sql.append("where 1=1 ");
        sql.append("group by df "); /* 按时间段分组 */
        sql.append("order by df desc "); /* 按时间段排序 */
        sql.append("limit 0," + top);
        List<Object> results = articleRepository.findOnSQL(sql.toString(), null, null);

        List<ArchiveBO> archives = new LinkedList<>();
        results.forEach(result -> {
            Object[] rs = (Object[]) result;
            archives.add(new ArchiveBO((String) rs[0], ((Number) rs[1]).intValue()));
        });
        return archives;
    }

    /**
     * <pre>
     * 按月进行文章个数统计，返回所有统计结果
     * </pre>
     *
     * @return
     */
    public List<ArchiveBO> findArchiveListAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select formatdatetime(article.create_time,'YYYY/MM') as df, count(article.uid) "); /* 将结果封装成ArchiveBO */
        sql.append("from article "); /* 从文章中查询 */
        sql.append("where 1=1 ");
        sql.append("group by df "); /* 按时间段分组 */
        sql.append("order by df desc"); /* 按时间段排序 */
        List<Object> results = articleRepository.findOnSQL(sql.toString(), null, null);

        List<ArchiveBO> archives = new LinkedList<>();
        results.forEach(result -> {
            Object[] rs = (Object[]) result;
            archives.add(new ArchiveBO((String) rs[0], ((Number) rs[1]).intValue()));
        });
        return archives;
    }

    /**
     * <pre>
     * 查询点击最多的top条文章
     * </pre>
     *
     * @param top
     * @return
     */
    public List<ArticleBO> hotsHits(int top) {
        List<ArticleEntity> articleEntities = articleRepository
                .findAllByStateOrderByHitsDesc(State.PUBLISHED, new QPageRequest(0, top));
        List<ArticleBO> articles = new LinkedList<>();
        articleEntities.forEach(articleEntity -> {
            ArticleBO article = new ArticleBO();
            BeanUtils.copyProperties(articleEntity, article);
            articles.add(article);
        });

        return articles;
    }

    /**
     * <pre>
     * 按照年月归档查询文章列表
     * </pre>
     *
     * @param year
     * @param month
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<ArticleBO> findByArchive(String year, String month, State state, int pageNum,
            int pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("select article.* ");
        sql.append("from article "); /* 从文章中查询 */
        sql.append(
                "where article.state=? and formatdatetime(article.create_time,'YYYY/MM')=? "); /* 按年月查询 */
        sql.append("order by article.create_time desc"); /* 按时间排序 */
        List<Object> articleEntities = articleRepository.findOnSQL(sql.toString(),
                ArticleEntity.class, query -> {
                    query.setParameter(1, state.name());
                    query.setParameter(2, year + "/" + month);
                    query.setFirstResult(pageNum * pageSize);
                    query.setMaxResults(pageSize);
                });

        List<ArticleBO> articles = new LinkedList<>();
        articleEntities.forEach(article -> {
            ArticleBO bo = new ArticleBO();
            BeanUtils.copyProperties(article, bo);
            articles.add(bo);
        });

        return articles;
    }

    /**
     * <pre>
     * 按照年月归档统计文章数量
     * </pre>
     *
     * @param year
     * @param month
     * @return
     */
    public long countByArchive(String year, String month, State state) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(article.uuid) ");
        sql.append("from article "); /* 从文章中查询 */
        sql.append(
                "where article.state=? and formatdatetime(article.create_time,'YYYY/MM')=? "); /* 按年月查询 */

        return articleRepository.countOnSQL(sql.toString(), query -> {
            query.setParameter(1, state.name());
            query.setParameter(2, year + "/" + month);
        });
    }

    /**
     * <pre>
     * 查找上一篇/下一篇
     * </pre>
     *
     * @param uid   当前文章标识
     * @param prev  true 上一篇 false 下一篇
     * @return
     */
    public ArticleEntity getNeighbor(final String uid, final boolean prev) {
        String hql = "select article from Article article where article.state=?1 and article.createTime "
                + (prev ? "<" : ">")
                + " (select art.createTime from Article art where art.uid=?2) order by article.createTime "
                + (prev ? "desc" : "asc");
        return articleRepository.findOne(hql, querySet -> {
            querySet.setParameter(1, State.PUBLISHED);
            querySet.setParameter(2, uid);
            querySet.setFirstResult(0).setMaxResults(1);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultArticle = ResourceUtils.getFile("classpath:webapp/view/pages/editor/WELCOME.md");
        Assert.isTrue(defaultArticle.exists());

        Assert.notNull(cacheCacheManager);
        cache = cacheCacheManager.getCache("resources-cache");
    }
}
