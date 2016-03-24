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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.manerfan.blog.interceptor.VersionInterceptorHandler;

/**
 * <pre>
 * Spring MVC 配置
 * 
 *   &lt;!-- 扫描注解 --&gt;
 *   &lt;context:annotation-config /&gt;
 *   &lt;context:component-scan base-package="com.manerfan.blog.webapp" /&gt;
 * </pre>
 *
 * @author ManerFan 2016年3月24日
 */
@Configuration
@EnableSpringConfigured
@EnableWebMvc
@ComponentScan(basePackages = "com.manerfan.blog.webapp", useDefaultFilters = false, includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Controller.class))
public class SpringMVCConfiguration extends WebMvcConfigurationSupport implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * <pre>
     * 视图解析
     * 
     *   &lt;!-- ViewResolver --&gt;  
     *   &lt;bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"&gt;  
     *       &lt;property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/&gt;&lt;!-- 使用jstl --&gt; 
     *       &lt;property name="prefix" value="/view/pages/"/&gt;  
     *       &lt;property name="suffix" value=".jsp"/&gt;  
     *   &lt;/bean&gt;
     * </pre>
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/view/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * <pre>
     * 资源
     * 
     *   &lt;mvc:resources mapping="/tags/**" location="/tags/" cache-period="31556926"/&gt; 
     *   &lt;mvc:resources mapping="/view/**" location="/view/" cache-period="31556926"/&gt;
     *   &lt;mvc:resources mapping="/*.*" location="/" cache-period="31556926"/&gt; 
     * </pre>
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry)
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        int cachePeriod = 31556926; /* 1年 */
        registry.addResourceHandler("/tags/**").addResourceLocations("/tags/")
                .setCachePeriod(cachePeriod);
        registry.addResourceHandler("/view/**").addResourceLocations("/view/")
                .setCachePeriod(cachePeriod);
        registry.addResourceHandler("/*.*").addResourceLocations("/").setCachePeriod(cachePeriod);
    }

    /**
     * <pre>
     * 使用的国际化文件
     * 
     *   &lt;!-- 使用的国际化文件 bean的名称必须定义为messageSource --&gt;
     *   &lt;bean id="messageSource"
     *       class="org.springframework.context.support.ResourceBundleMessageSource"
     *       p:basename="msg_manerfan"/&gt;
     * </pre>
     */
    @Bean(name = "messageSource" /* 必须为messageSource */)
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("msg_manerfan");
        return messageSource;
    }

    /**
     * <pre>
     * locale解析器
     * 
     *   &lt;!-- 使用accept-language头信息解析浏览器语言 --&gt;
     *   &lt;bean id="localeResolver" class="org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver"/&gt;
     * </pre>
     */
    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    /**
     * <pre>
     * 自动注册
     * ByteArrayHttpMessageConverter
     * StringHttpMessageConverter
     * ResourceHttpMessageConverter
     * SourceHttpMessageConverter
     * AllEncompassingFormHttpMessageConverter
     * MappingJackson2HttpMessageConverter(isPresent com.fasterxml.jackson.databind.ObjectMapper && isPresent com.fasterxml.jackson.core.JsonGenerator)
     * </pre>
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#requestMappingHandlerAdapter()
     */
    @Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return super.requestMappingHandlerAdapter();
    }

    /**
     * <pre>
     * 上传文件
     * 
     *   &lt;!-- 支持上传文件 --&gt;  
     *   &lt;!-- id="multipartResolver"必须是multipartResolver --&gt;
     *   &lt;bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"&gt;
     *       &lt;!-- maxUploadSize:文件上传的最大值以byte为单位 --&gt;
     *       &lt;property name="maxUploadSize" value="1024000"&gt;&lt;/property&gt;
     *       &lt;property name="defaultEncoding" value="utf-8"&gt;&lt;/property&gt;
     *   &lt;/bean&gt; 
     * </pre>
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024000l);
        multipartResolver.setDefaultEncoding("UTF-8");

        return multipartResolver;
    }

    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return super.requestMappingHandlerMapping();
    }

    @Bean
    @Override
    public HandlerMapping resourceHandlerMapping() {
        return super.resourceHandlerMapping();
    }

    /**
     * <pre>
     * 拦截器
     * 
     *   &lt;mvc:interceptors&gt;
     * </pre>
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        /* 版本管理拦截器 */
        registry.addInterceptor(beanFactory.getBean(VersionInterceptorHandler.class));
    }

}
