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
package com.manerfan.blog.dao.repositories;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <b>BO工具类</b>
 * <p>
 * BO工具类
 *
 * @author ManerFan 2015年1月22日
 */
public class BOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BOUtils.class);

    /**
     * <p>
     * <b>将PO-list转为BO-list</b>
     * <p>
     * 将PO-list转为BO-list，对应的BO中必须有 public static BO-TYPE transFromPO(PO-TYPE) 方法
     *
     * @param pos       需要转换的PO-list
     * @param clazzbo   BO的class
     * @param clazzpo   PO的class
     * @return          转换的BO-list
     */
    public static <B, P> List<B> transFromPOs(Collection<? extends P> pos, Class<B> clazzbo,
            Class<P> clazzpo) {
        if (null == pos || pos.isEmpty()) {
            return null;
        }

        if (null == clazzbo || null == clazzpo) {
            LOGGER.warn("clazzbo or clazzpo is null.");
            return null;
        }

        List<B> vos = new LinkedList<>();
        try {
            Method transMethod = clazzbo.getMethod("transFromPO", clazzpo);
            for (P po : pos) {
                B bo = clazzbo.cast(transMethod.invoke(clazzbo, po));
                if (null != bo) {
                    vos.add(bo);
                }
            }

            return vos;
        } catch (Exception e) {
            LOGGER.error("Cannot Find Method [transFromPO] in class {}", clazzbo.getName(), e);
            return null;
        }

    }
}
