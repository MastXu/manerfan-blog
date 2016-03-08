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
package com.manerfan.blog.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

/**
 * <pre>自定义登陆验证过滤器</pre>
 *
 * @author ManerFan 2016年3月8日
 */
public class CustomUsernamePasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    public CustomUsernamePasswordAuthenticationFilter() {
    }

    public CustomUsernamePasswordAuthenticationFilter(String loginPage) {
        Assert.hasText(loginPage);
        setFilterProcessesUrl(loginPage);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        // TODO 这里对password做rsa解密
        return super.obtainPassword(request);
    }

}
