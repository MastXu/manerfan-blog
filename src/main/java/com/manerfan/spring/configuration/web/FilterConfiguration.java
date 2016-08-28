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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.manerfan.blog.filter.AntiTheftLinkFilter;
import com.manerfan.blog.filter.MobileDeviceFilter;

/**
 * <pre>
 * Filter配置
 * </pre>
 *
 * @author ManerFan 2016年8月22日
 */
@Configuration
@ConfigurationProperties(prefix = "mblog.filter", ignoreUnknownFields = true)
public class FilterConfiguration {

    @NestedConfigurationProperty
    private AntiTheftLinkFilter antiTheftLinkFilter = new AntiTheftLinkFilter();

    public AntiTheftLinkFilter getAntiTheftLinkFilter() {
        return antiTheftLinkFilter;
    }

    private int antiTheftLinkFilterOrder;

    public void setAntiTheftLinkFilterOrder(int antiTheftLinkFilterOrder) {
        this.antiTheftLinkFilterOrder = antiTheftLinkFilterOrder;
    }

    /**
     * <pre>
     * AntiTheftLinkFilter
     * 防盗链
     * </pre>
     *
     * @author ManerFan 2016年8月22日
     * @return
     */
    @Bean
    public FilterRegistrationBean antiTheftLinkFilterBean() {
        FilterRegistrationBean antiTheftLinkFilterBean = new FilterRegistrationBean();

        antiTheftLinkFilterBean.addServletNames("AntiTheftLinkFilter");
        antiTheftLinkFilterBean.addUrlPatterns("/*");
        antiTheftLinkFilterBean.setFilter(antiTheftLinkFilter);
        antiTheftLinkFilterBean.setOrder(antiTheftLinkFilterOrder);

        return antiTheftLinkFilterBean;
    }

    @NestedConfigurationProperty
    private MobileDeviceFilter mobileDeviceFilter = new MobileDeviceFilter();

    public MobileDeviceFilter getMobileDeviceFilter() {
        return mobileDeviceFilter;
    }

    private int mobileDeviceFilterOrder;

    /**
     * <pre>
     * 移动设备访问
     * </pre>
     *
     * @author ManerFan 2016年8月22日
     * @return
     */
    @Bean
    public FilterRegistrationBean mobileDeviceFilterBean() {
        FilterRegistrationBean mobileDeviceFilterBean = new FilterRegistrationBean();

        mobileDeviceFilterBean.addServletNames("MobileDeviceFilter");
        mobileDeviceFilterBean.addUrlPatterns("/*");
        mobileDeviceFilterBean.setFilter(mobileDeviceFilter);
        mobileDeviceFilterBean.setOrder(mobileDeviceFilterOrder);

        return mobileDeviceFilterBean;
    }

    public void setMobileDeviceFilterOrder(int mobileDeviceFilterOrder) {
        this.mobileDeviceFilterOrder = mobileDeviceFilterOrder;
    }

    @NestedConfigurationProperty
    private CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();

    public CharacterEncodingFilter getCharacterEncodingFilter() {
        return characterEncodingFilter;
    }

    private int characterEncodingFilterOrder;

    public void setCharacterEncodingFilterOrder(int characterEncodingFilterOrder) {
        this.characterEncodingFilterOrder = characterEncodingFilterOrder;
    }

    /**
     * <pre>
     * CharacterEncodingFilter
     * 字符编码转换
     * </pre>
     *
     * @author ManerFan 2016年8月22日
     * @return
     */
    @Bean
    public FilterRegistrationBean characterEncodingFilterBean() {
        FilterRegistrationBean characterEncodingFilterBean = new FilterRegistrationBean();

        characterEncodingFilterBean.addServletNames("CharacterEncodingFilter");
        characterEncodingFilterBean.addUrlPatterns("/*");
        characterEncodingFilterBean.setFilter(characterEncodingFilter);
        characterEncodingFilterBean.setOrder(characterEncodingFilterOrder);

        return characterEncodingFilterBean;
    }

    @NestedConfigurationProperty
    private OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();

    public OpenEntityManagerInViewFilter getOpenEntityManagerInViewFilter() {
        return openEntityManagerInViewFilter;
    }

    private int openEntityManagerInViewFilterOrder;

    public void setOpenEntityManagerInViewFilterOrder(int openEntityManagerInViewFilterOrder) {
        this.openEntityManagerInViewFilterOrder = openEntityManagerInViewFilterOrder;
    }

    /**
     * <pre>
     * OpenEntityManagerInViewFilter
     * Controller中使用Entity Lazy
     * </pre>
     *
     * @author ManerFan 2016年8月22日
     * @return
     */
    @Bean
    public FilterRegistrationBean openEntityManagerInViewFilterBean() {
        FilterRegistrationBean openEntityManagerInViewFilterBean = new FilterRegistrationBean();

        openEntityManagerInViewFilterBean.addServletNames("Spring OpenEntityManagerInViewFilter");
        openEntityManagerInViewFilterBean.addUrlPatterns("/*");
        openEntityManagerInViewFilterBean.setFilter(openEntityManagerInViewFilter);
        openEntityManagerInViewFilterBean.setOrder(openEntityManagerInViewFilterOrder);

        return openEntityManagerInViewFilterBean;
    }

}
