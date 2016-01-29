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
package com.manerfan.blog.webapp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import com.manerfan.blog.dao.entities.UserEntity;

/**
 * <pre></pre>
 *
 * @author ManerFan 2016年1月28日
 */
public class ControllerBase {

    /**
     * 
     * <pre>判断当前登陆是否为匿名</pre>
     *
     * @return
     */
    protected boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication
                || UserEntity.NAME_ANONYMOUS.equals(authentication.getPrincipal())) {
            return true;
        }

        return false;
    }

    /**
     * 
     * <pre>获取国际化</pre>
     *
     * @param args  参数
     * @param code  国际化code
     * @return
     */
    protected String getLocaleMessage(Object[] args, String code) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        RequestContext context = new RequestContext(request);

        return context.getMessage(code, args);
    }

}
