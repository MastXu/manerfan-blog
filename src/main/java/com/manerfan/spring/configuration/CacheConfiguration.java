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

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * <pre>
 * Cache 配置
 * </pre>
 *
 * @author ManerFan 2016年3月24日
 */
@Configuration
@EnableSpringConfigured
@EnableCaching(proxyTargetClass = true)
public class CacheConfiguration extends CachingConfigurerSupport {
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * <pre>
     *   &lt;bean id="ehcacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean"&gt;
     *       &lt;property name="configLocation" value="classpath:ehcache/ehcache.xml" /&gt;
     *       &lt;property name="cacheManagerName" value="SpringEhcacheManager" /&gt; &lt;!-- 防止ehcache冲突 --&gt;
     *       &lt;property name="shared" value="false"/&gt;
     *   &lt;/bean&gt;
     * </pre>
     */
    @Bean
    public EhCacheManagerFactoryBean ehcacheManager() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(
                resourcePatternResolver.getResource("classpath:ehcache/ehcache.xml"));
        cacheManagerFactoryBean.setCacheManagerName("SpringEhcacheManager"); /* 防止ehcache冲突 */
        cacheManagerFactoryBean.setShared(false);
        return cacheManagerFactoryBean;
    }

    /**
     * <pre>
     *   &lt;!-- Cache cache = cacheManager.getCache("xxx"); --&gt;
     *   &lt;bean id="ehcacheCacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager"&gt;
     *       &lt;property name="cacheManager" ref="ehcacheManager" /&gt;
     *   &lt;/bean&gt;
     * </pre>
     */
    @Bean
    public EhCacheCacheManager ehcacheCacheManager() {
        EhCacheCacheManager cacheCacheManager = new EhCacheCacheManager();
        cacheCacheManager.setCacheManager(ehcacheManager().getObject());
        return cacheCacheManager;
    }

    /**
     * <pre>
     *   &lt;!-- 开启cache注解 @Cacheable @CacheEvict --&gt;
     *   &lt;cache:annotation-driven cache-manager="ehcacheCacheManager"/&gt;
     * </pre>
     * @see org.springframework.cache.annotation.CachingConfigurerSupport#cacheManager()
     */
    @Override
    public CacheManager cacheManager() {
        return ehcacheCacheManager();
    }

}
