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
package com.manerfan.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.manerfan.blog.dao.entities.SysConfEntity;
import com.manerfan.blog.dao.repositories.SysConfRepository;

/**
 * <pre>系统参数配置</pre>
 *
 * @author ManerFan 2016年6月23日
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public class SysConfService {

    @Autowired
    private SysConfRepository sysConfRepository;

    public void updateOrSave(String key, Object value) {
        // TODO
    }

    public SysConfEntity get(String key) {
        return sysConfRepository.findOneByKey(key);
    }

    public String getString(String key) {
        SysConfEntity sysConf = get(key);
        if (null == sysConf) {
            return null;
        }

        return sysConf.getValue();
    }

    public Integer getInt(String key) {
        String value = getString(key);
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return Integer.valueOf(value);
    }

    public Long getLong(String key) {
        String value = getString(key);
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return Long.valueOf(value);
    }

}
