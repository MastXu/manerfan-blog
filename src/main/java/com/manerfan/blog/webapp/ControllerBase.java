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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

/**
 * <pre></pre>
 *
 * @author ManerFan 2016年1月28日
 */
public class ControllerBase {

    protected static final String ERRMSG = "errmsg";

    protected Map<String, Object> makeAjaxData() {
        Map<String, Object> data = new HashMap<>();
        data.put(ERRMSG, null);

        return data;
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
