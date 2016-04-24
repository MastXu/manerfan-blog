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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.manerfan.blog.security.CustomLogoutFilter;
import com.manerfan.blog.security.CustomUserDetailsService;
import com.manerfan.blog.security.CustomUsernamePasswordAuthenticationFilter;
import com.manerfan.blog.service.RSAService;

/**
 * <pre>
 * Spring Security 配置
 * </pre>
 *
 * @author ManerFan 2016年3月24日
 */
@Configuration
@EnableSpringConfigured
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter
        implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * <pre>
     * &lt;http security="none" pattern="/*.*" /&gt;
     * </pre>
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/*.*", "/init/**", "/view/**", "/article/image/inline/**",
                "/article/list", "/category/hots/*", "/tags/**");
    }

    /**
     * <pre>
     *   &lt;http auto-config="false" use-expressions="true" entry-point-ref="authenticationEntryPoint"&gt;
     *       &lt;!-- 启用匿名用户 --&gt;
     *       &lt;anonymous enabled="true" key ="doesNotMatter" username="anonymous" granted-authority="ROLE_ANONYMOUS" /&gt;
     *   
     *       &lt;intercept-url pattern="/" access="hasAnyRole('ROLE_ANONYMOUS','ROLE_ADMIN','ROLE_USER')" /&gt;
     *       &lt;intercept-url pattern="/login/**" access="hasAnyRole('ROLE_ANONYMOUS','ROLE_ADMIN','ROLE_USER')" /&gt;
     *       &lt;intercept-url pattern="/editor" access="hasAnyRole('ROLE_ANONYMOUS','ROLE_ADMIN','ROLE_USER')" /&gt;
     *       &lt;intercept-url pattern="/**" access="hasAnyRole('ROLE_ADMIN','ROLE_USER')" /&gt;
     *   
     *       &lt;!-- 登陆
     *         login-page                  登陆地址
     *         login-processing-url        验证地址，默认/j-spring-security-check
     *         username-parameter          用户名参数
     *         password-parameter          密码参数
     *         default-target-url          登陆后默认跳转的页面
     *         authentication-failure-url  登陆失败后跳转的页面
     *        --&gt;
     *       &lt;!-- 
     *       &lt;form-login login-page="/login"
     *        login-processing-url="/login/check" 
     *        username-parameter="username"
     *        password-parameter="password"
     *        default-target-url="/"
     *        authentication-failure-url="/login" /&gt;
     *        --&gt;
     *        
     *       &lt;!-- 替换默认的LoginFilter --&gt;
     *       &lt;custom-filter ref="customLoginFilter" position="FORM_LOGIN_FILTER" /&gt;
     *        
     *       &lt;!-- 登出 --&gt;
     *       &lt;!-- &lt;logout logout-url="/logout" logout-success-url="/login" /&gt; --&gt;
     *       &lt;!-- 替换默认的LogoutFilter --&gt;
     *       &lt;custom-filter ref="customLogoutFilter" position="LOGOUT_FILTER" /&gt;
     *       
     *       &lt;csrf disabled="true"/&gt; &lt;!-- 搞不定csrf，先禁用 --&gt;
     *   &lt;/http&gt; 
     * </pre>
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* 启用匿名用户 */
        http.anonymous().key("doesNotMatter")
                .principal(new User("anonymous", "anonymous",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")))
                .authorities("ROLE_ANONYMOUS");

        http.authorizeRequests().expressionHandler(webSecurityExpressionHandler())
                /* 任意用户可访问的 */
                .antMatchers("/", "/login", "/editor", "/editor/fileImport").permitAll()
                /* 只有admin可访问的 */
                /*.antMatchers("/**").hasAnyRole("ADMIN")*/
                /* 登陆后可访问的 */
                .antMatchers("/**").hasAnyRole("ADMIN", "USER")/* [spring会自动添加ROLE_前缀] */
                .and().httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint())/* 指定EntryPoint */
                .and().exceptionHandling().accessDeniedPage("/login");

        /* 自定义登陆过滤器 */
        http.addFilter(customLoginFilter());

        /* 自定义登出过滤器 */
        http.addFilter(customLogoutFilter());

        /* 搞不定csrf，先禁用 */
        http.csrf().disable();
    }

    /**
     * <pre>
     * 表达式控制器
     * </pre>
     */
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }

    /**
     * <pre>
     * EntryPoint
     * </pre>
     */
    @Bean
    public LoginUrlAuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login");
    }

    /**
     * <pre>
     * 认证管理器，实现用户认证的入口
     * 
     *   &lt;!-- 认证管理器，实现用户认证的入口  --&gt; 
     *   &lt;authentication-manager alias="authenticationManager"&gt;
     *       &lt;authentication-provider user-service-ref="userDetailsService" /&gt;
     *   &lt;/authentication-manager&gt;
     * </pre>
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider
                .setUserDetailsService(beanFactory.getBean(CustomUserDetailsService.class));
        return daoAuthenticationProvider;
    }

    /**
     * <pre>
     * 自定义登陆认证过滤器 
     * 
     *   &lt;!-- 自定义登陆认证过滤器 --&gt;
     *   &lt;beans:bean name="customLoginFilter"
     *       class="com.manerfan.blog.security.CustomUsernamePasswordAuthenticationFilter"&gt;
     *       &lt;beans:constructor-arg value="/login/check"/&gt;&lt;!-- 认证地址 --&gt;
     *       &lt;beans:property name="authenticationManager" ref="authenticationManager" /&gt;
     *       &lt;beans:property name="usernameParameter" value="username" /&gt;
     *       &lt;beans:property name="passwordParameter" value="password" /&gt;
     *       &lt;beans:property name="authenticationSuccessHandler" ref="loginSuccessHandler" /&gt;
     *       &lt;beans:property name="authenticationFailureHandler" ref="loginFailureHandler" /&gt;
     *       &lt;beans:property name="rsaService" ref="passwordRSAService" /&gt;
     *   &lt;/beans:bean&gt;
     * </pre>
     * @return
     * @throws Exception
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customLoginFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter customLoginFilter = new CustomUsernamePasswordAuthenticationFilter(
                "/login/check");
        customLoginFilter.setAuthenticationManager(authenticationManagerBean());
        customLoginFilter.setUsernameParameter("username");
        customLoginFilter.setPasswordParameter("password");
        customLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        customLoginFilter.setRsaService(beanFactory.getBean(RSAService.class));

        return customLoginFilter;
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        loginSuccessHandler.setDefaultTargetUrl("/"); /* 登录成功默认跳转地址 */
        loginSuccessHandler.setAlwaysUseDefaultTargetUrl(false);
        return loginSuccessHandler;
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler loginFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login"); /* 登陆失败跳转地址 */
    }

    /**
     * <pre>
     * 自定义注销过滤器
     * 
     *   &lt;!-- 自定义注销过滤器 --&gt;
     *   &lt;beans:bean name="customLogoutFilter"
     *       class="com.manerfan.blog.security.CustomLogoutFilter"&gt;
     *       &lt;beans:constructor-arg value="/login"/&gt;&lt;!-- 注销成功后的跳转地址 --&gt; 
     *       &lt;beans:constructor-arg&gt;  
     *           &lt;beans:list&gt;  
     *               &lt;beans:ref bean="securityContextLogoutHandler"/&gt;  
     *           &lt;/beans:list&gt;  
     *       &lt;/beans:constructor-arg&gt;      
     *   &lt;/beans:bean&gt;
     * </pre>
     */
    @Bean
    public CustomLogoutFilter customLogoutFilter() {
        return new CustomLogoutFilter("/login", securityContextLogoutHandler());
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.setInvalidateHttpSession(true); /* 注销后置session失效 */
        securityContextLogoutHandler.setClearAuthentication(true); /* 注销后清除认证信息 */
        return securityContextLogoutHandler;
    }

}
