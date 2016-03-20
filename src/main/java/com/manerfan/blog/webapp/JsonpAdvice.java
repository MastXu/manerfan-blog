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

import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * <pre>为Spring MVC 提供JSONP支持</pre>
 *
 * @author ManerFan 2016年3月7日
 */
@ControllerAdvice("com.manerfan.blog.webapp")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("jsonpcallback", "callback", "jsonp");
    }

    /**
     * 为了保留charset等参数
     * @see org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice#getContentType(org.springframework.http.MediaType, org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse)
     */
    @Override
    protected MediaType getContentType(MediaType contentType, ServerHttpRequest request,
            ServerHttpResponse response) {
        return new MediaType("application", "javascript", contentType.getParameters());
    }
}
