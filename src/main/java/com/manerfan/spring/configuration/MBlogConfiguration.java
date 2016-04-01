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

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

/**
 * <pre>
 * 博客spring配置
 * </pre>
 *
 * @author ManerFan 2016年3月24日
 */
@Import({ DataJpaRepositoryConfiguration.class, /* 数据库 */
        CacheConfiguration.class, /* 缓存 */
        SpringMVCConfiguration.class, /* spring mvc */
        SpringSecurityConfiguration.class /* spring security */
})
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MBlogConfiguration {

}
