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
package com.manerfan.spring.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.manerfan.blog.listener.H2DBServerListener;

/**
 * <pre>
 * ServletContextListener配置
 * </pre>
 *
 * @author ManerFan 2016年8月22日
 */
@Configuration
public class ListenerConfiguration {

    /**
     * <pre>
     * 随系统启停H2DB
     * </pre>
     *
     * @author ManerFan 2016年8月22日
     */
    @Bean
    public H2DBServerListener h2dbServerListener() {
        return new H2DBServerListener();
    }
}
