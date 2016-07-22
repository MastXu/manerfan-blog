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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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

    /** 邮件相关 */
    public static final String EMAIL_STMP_HOST = "email_stmp_host";
    public static final String EMAIL_STMP_PORT = "email_stmp_port";
    public static final String EMAIL_SSL_ENABLE = "email_ssl_enable";
    public static final String EMAIL_USERNAME = "email_username";
    public static final String EMAIL_PASSWORD = "email_password";

    /** 多说相关 */
    public static final String DUOSHUO_KEY = "duoshuo_key";
    public static final String DUOSHUO_URL = "duoshuo_url";

    @Autowired
    private SysConfRepository sysConfRepository;

    @CacheEvict(cacheNames = "config-cache", beforeInvocation = true, allEntries = true)
    public void remove(String key) {
        sysConfRepository.deleteByKey(key);
    }

    @CacheEvict(cacheNames = "config-cache", beforeInvocation = true, allEntries = true)
    public void removes(Collection<String> keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return;
        }
        sysConfRepository.deleteByKeys(keys);
    }

    @CacheEvict(cacheNames = "config-cache", beforeInvocation = true, allEntries = true)
    public void removes(String... keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return;
        }
        sysConfRepository.deleteByKeys(Arrays.asList(keys));
    }

    @CacheEvict(cacheNames = "config-cache", beforeInvocation = true, allEntries = true)
    public void updateOrSave(String key, Object value) {
        if (!StringUtils.hasText(key) || null == value) {
            return;
        }

        SysConfEntity entity = get(key);
        if (null == entity) {
            entity = new SysConfEntity();
            entity.setKey(key);
        }
        entity.setValue(value.toString());

        sysConfRepository.save(entity);
    }

    @Cacheable(cacheNames = "config-cache")
    public SysConfEntity get(String key) {
        return sysConfRepository.findOneByKey(key);
    }

    @Cacheable(cacheNames = "config-cache")
    public List<SysConfEntity> gets(Collection<String> keys) {
        return sysConfRepository.findByKeyIn(keys);
    }

    @Cacheable(cacheNames = "config-cache")
    public String getString(String key) {
        SysConfEntity sysConf = get(key);
        if (null == sysConf) {
            return null;
        }

        return sysConf.getValue();
    }

    @Cacheable(cacheNames = "config-cache")
    public Map<String, String> getMap(Collection<String> keys) {
        Map<String, String> map = new HashMap<>();

        List<SysConfEntity> entities = gets(keys);
        if (ObjectUtils.isEmpty(entities)) {
            return map;
        }

        entities.forEach(entity -> map.put(entity.getKey(), entity.getValue()));

        return map;
    }

    @Cacheable(cacheNames = "config-cache")
    public Map<String, String> getMap(String... keys) {
        return getMap(Arrays.asList(keys));
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
