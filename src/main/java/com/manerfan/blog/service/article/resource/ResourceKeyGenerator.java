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
package com.manerfan.blog.service.article.resource;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import com.manerfan.blog.service.article.ImageService;

/**
 * <pre>
 * 资源缓存key生成器
 * </pre>
 *
 * @author ManerFan 2016年3月25日
 */
@Component("ResourceKeyGenerator")
public class ResourceKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();

        if (ImageService.class.isInstance(target)) {
            /* 图片缓存 */
            sb.append("image_");
        }

        for (Object param : params) {
            sb.append(param);
        }
        return sb.toString();
    }

}
