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

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * <pre>
 * 自定义资源视图解析器
 * 
 * 如果带有后缀，则使用 prefix + viewName
 * 否则，使用 prefix + viewName + suffix
 * </pre>
 *
 * @author ManerFan 2016年6月16日
 */
public class CustomSuffixResourceViewResolver extends InternalResourceViewResolver {

    private ThreadLocal<String> viewLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        /* 将viewName放入ThreadLocal */
        viewLocal.set(viewName);
        /* buildView中会调用getSuffix */
        return super.buildView(viewName);
    }

    @Override
    protected String getSuffix() {
        /* 从ThreadLocal中取出viewName */
        String viewName = viewLocal.get();
        if (StringUtils.hasText(viewName) && viewName.contains(".")) {
            /* 如果viewName中已经包含后缀，则使用prefix + viewName */
            return "";
        } else {
            /* 否则，使用prefix + viewName + suffix */
            return super.getSuffix();
        }
    }

}
