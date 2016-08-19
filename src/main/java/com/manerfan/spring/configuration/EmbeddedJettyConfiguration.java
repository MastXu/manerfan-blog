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

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.jasper.runtime.TldScanner;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.resource.PathResource;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.manerfan.blog.Http2ConnectionFactory;

/**
 * <pre>
 * 额外的嵌入式JETTY配置
 * </pre>
 *
 * @author ManerFan 2016年8月19日
 */
@Configuration
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
public class EmbeddedJettyConfiguration {

    public void setBaseResource(String baseResource) {
        this.baseResource = baseResource;
    }

    private String baseResource;

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        try {
            Field f = TldScanner.class.getDeclaredField("systemUris");
            f.setAccessible(true);
            ((Set<?>) f.get(null)).clear();
        } catch (Exception e) {
            throw new RuntimeException("Could not clear TLD system uris.", e);
        }

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof JettyEmbeddedServletContainerFactory) {
                    ((JettyEmbeddedServletContainerFactory) container)
                            .addServerCustomizers(new JettyServerCustomizer() {
                                @Override
                                public void customize(Server server) {
                                    Handler handler = server.getHandler();
                                    if (handler instanceof HandlerWrapper) {
                                        handler = ((HandlerWrapper) handler).getHandler();
                                    }

                                    if (handler instanceof ContextHandler) {
                                        ((ContextHandler) handler).setBaseResource(
                                                PathResource.newClassPathResource(baseResource));
                                    }

                                    for (Connector connector : server.getConnectors()) {
                                        if (connector instanceof AbstractConnector) {
                                            ((AbstractConnector) connector).addConnectionFactory(
                                                    new Http2ConnectionFactory());
                                        }
                                    }
                                }
                            });
                }
            }
        };
    }

}
