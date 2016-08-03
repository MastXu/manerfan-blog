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
package com.manerfan.spring.configuration;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.util.Assert;

import com.manerfan.common.utils.lucene.LuceneManager;

/**
 * <pre>
 * 博客spring配置
 * </pre>
 *
 * @author ManerFan 2016年3月24日
 */
@Import({ ComponentConfiguration.class, /* bean扫描 */
        PropertyPlaceholderConfiguration.class, /* properties */
        TaskConfiguration.class, /* 定时任务 */
        DataJpaRepositoryConfiguration.class, /* 数据库 */
        CacheConfiguration.class, /* 缓存 */
        SpringMVCConfiguration.class, /* spring mvc */
        SpringSecurityConfiguration.class /* spring security */
})
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MBlogConfiguration {

    private @Value("${mblog.basedir}") String basedir;

    private @Value("${mblog.h2dbdir}") String jdbcDir;

    @Bean
    @Lazy(false)
    public ResourceLocation resourceLocation() {
        Assert.hasText(basedir);
        Assert.hasText(jdbcDir);

        ResourceLocation location = new ResourceLocation();

        File baseDir = new File(basedir);
        if (!baseDir.exists()) {
            Assert.isTrue(baseDir.mkdirs());
        }

        File imageDir = new File(baseDir, "image");
        if (!imageDir.exists()) {
            Assert.isTrue(imageDir.mkdirs());
        }
        location.imageDir = imageDir;

        File articleDir = new File(baseDir, "article");
        if (!articleDir.exists()) {
            Assert.isTrue(articleDir.mkdirs());
        }
        location.articleDir = articleDir;

        File indexDir = new File(baseDir, "index");
        if (!indexDir.exists()) {
            Assert.isTrue(indexDir.mkdirs());
        }
        location.indexDir = indexDir;

        File dbDir = new File(jdbcDir);
        if (!dbDir.exists()) {
            Assert.isTrue(dbDir.mkdirs());
        }
        location.dbDir = dbDir;

        File backupDir = new File(baseDir, "backup");
        if (!backupDir.exists()) {
            Assert.isTrue(backupDir.mkdirs());
        }
        location.backupDir = backupDir;

        return location;
    }

    @Bean(name = "articleLuceneManager", destroyMethod = "shutdown")
    @Lazy(false)
    public LuceneManager luceneManager() throws IOException {
        LuceneManager luceneManager = LuceneManager.newFSInstance(resourceLocation().indexDir);
        return luceneManager;
    }
}
