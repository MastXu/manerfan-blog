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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <pre>pjax请求拦截</pre>
 *
 * @author ManerFan 2016年6月7日
 */
@Component
public class PjaxInterceptorHandler extends HandlerInterceptorAdapter {

    private ThreadLocal<Boolean> pjaxThreadLoacal = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        /* 判断是否为pjax请求 */
        pjaxThreadLoacal.set(Boolean.parseBoolean(request.getHeader("X-PJAX")));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (null == modelAndView) {
            return;
        }

        if (isPjax()) {
            /* 如果是pjax请求，则返回对应的Pjax页面  xxxPjax.jsp  */
            modelAndView.setViewName(modelAndView.getViewName() + "Pjax");
            modelAndView.addObject("pjax", true);
        } else {
            modelAndView.addObject("pjax", false);
        }
    }

    public boolean isPjax() {
        Boolean pjax = pjaxThreadLoacal.get();
        return null == pjax ? false : pjax.booleanValue();
    }

}
