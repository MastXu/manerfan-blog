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
package com.manerfan.blog.interceptor;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <pre>用户信息拦截器</pre>
 *
 * @author ManerFan 2016年4月3日
 */
@Component
public class UserInfoInterceptorHandler extends HandlerInterceptorAdapter {

    private User anonymous = new User("anonymous", "anonymous",
            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    private ThreadLocal<User> userThreadLoacal = new ThreadLocal<User>() {
        @Override
        protected User initialValue() {
            userThreadLoacal.set(anonymous);
            return super.initialValue();
        }
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            userThreadLoacal.set(anonymous);
        } else {
            userThreadLoacal.set((User) authentication.getPrincipal());
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * <pre>
     * 判断当前用户是否为匿名用户
     * </pre>
     *
     * @return
     */
    public boolean isAnonymous() {
        return ObjectUtils.nullSafeEquals(userThreadLoacal.get().getUsername(), "anonymous");
    }

    /**
     * <pre>
     * 获取用户名
     * </pre>
     *
     * @return
     */
    public String userName() {
        return userThreadLoacal.get().getUsername();
    }

    /**
     * <pre>
     * 获取权限信息
     * </pre>
     *
     * @return
     */
    public Collection<GrantedAuthority> authorities() {
        return userThreadLoacal.get().getAuthorities();
    }

}
