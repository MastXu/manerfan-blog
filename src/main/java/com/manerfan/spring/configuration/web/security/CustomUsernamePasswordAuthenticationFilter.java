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
package com.manerfan.spring.configuration.web.security;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.manerfan.blog.service.RSAService;
import com.manerfan.blog.webapp.LoginController;

/**
 * <pre>自定义登陆验证过滤器</pre>
 *
 * @author ManerFan 2016年3月8日
 */
public class CustomUsernamePasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private RSAService rsaService;

    public CustomUsernamePasswordAuthenticationFilter() {
    }

    public CustomUsernamePasswordAuthenticationFilter(String loginPage) {
        Assert.hasText(loginPage);
        setFilterProcessesUrl(loginPage);
    }

    /**
     * 登陆失败时的处理逻辑
     * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#unsuccessfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
                    throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if (!StringUtils.hasText((String) session.getAttribute(LoginController.ERR_MSG))) {
            session.setAttribute(LoginController.ERR_MSG, "用户名或密码错误，请重新登陆");
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }

    /**
     * 获取密码，密码RSA解密
     * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#obtainPassword(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        // rsa密文
        String passwordrsa = super.obtainPassword(request);

        // 从缓存中获取rsa私钥
        RSAPrivateKey key = rsaService.getPrivateKey(session.getId());
        if (null == key) {
            // 页面停留时间过长导致key过期
            session.setAttribute(LoginController.ERR_MSG, "页面停留时间过长，请重新登陆");
            throw new TimeoutException("页面停留时间过长，请重新登陆");
        }

        try {
            return rsaService.decodeAddSalt(key, passwordrsa);
        } catch (InternalAuthenticationServiceException e) {
            session.setAttribute(LoginController.ERR_MSG, "登录失败，请联系管理员或重新登陆");
            throw e;
        }
    }

    public void setRsaService(RSAService rsaService) {
        this.rsaService = rsaService;
    }

    /**
     * <pre>
     * 登陆超时异常
     * 
     * 
     * AbstractAuthenticationProcessingFilter.doFilter
     * 会对InternalAuthenticationServiceException异常做日志处理
     * 对其他AuthenticationException异常则不做日志处理
     * 
     * 所有AuthenticationException异常均会停止过滤器链，调用unsuccessfulAuthentication
     * </pre>
     *
     * @author ManerFan 2016年3月11日
     */
    public static class TimeoutException extends AuthenticationException {

        private static final long serialVersionUID = -4467745677676343227L;

        public TimeoutException(String msg) {
            super(msg);
        }
    }
}
