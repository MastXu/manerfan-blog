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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Controller;

import com.manerfan.blog.listener.H2DBServerListener;
import com.manerfan.common.utils.lucene.LuceneManager;
import com.manerfan.spring.configuration.web.FilterConfiguration;

/**
 * <pre>
 * 博客spring配置
 * </pre>
 *
 * @author ManerFan 2016年3月24日
 */
@Configuration
@EnableConfigurationProperties(MblogProperties.class)
@ImportResource("classpath*:spring/*.xml") /* 支持传统xml配置 */
@ComponentScan(basePackages = "com.manerfan", excludeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Controller.class), /* 不扫描sping mvc相关 */
        @Filter(type = FilterType.REGEX, pattern = "com\\.manerfan\\.spring\\..*") }) /* 不扫描spring javaconfig相关 */
@Import({ FilterConfiguration.class, DataJpaRepositoryConfiguration.class, /* 数据库 */
        H2DBServerListener.class, /* 数据库 */
        SpringMVCConfiguration.class, /* spring mvc */
        SpringSecurityConfiguration.class /* spring security */
})
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MBlogConfiguration {

    private @Value("${mblog.basedir}") String basedir;

    private @Value("${mblog.h2dbdir}") String jdbcDir;

    @Bean(name = "articleLuceneManager", destroyMethod = "shutdown")
    @Lazy(false)
    public LuceneManager luceneManager(MblogProperties mblogProperties) throws IOException {
        LuceneManager luceneManager = LuceneManager.newFSInstance(mblogProperties.getIndexDir());
        return luceneManager;
    }

}
