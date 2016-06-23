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

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manerfan.blog.dao.entities.SysConfEntity;
import com.manerfan.common.utils.dao.repositories.BasicJpaRepository;

/**
 * <pre>系统参数接口</pre>
 *
 * @author ManerFan 2016年6月23日
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class,
        RuntimeException.class })
public interface SysConfRepository extends BasicJpaRepository<SysConfEntity, String> {

    /**
     * <pre>
     * 通过key查找一个系统参数
     * </pre>
     *
     * @param key
     * @return
     */
    public SysConfEntity findOneByKey(String key);

    /**
     * <pre>
     * 通过key查找多个系统参数
     * </pre>
     *
     * @param keys
     * @return
     */
    public List<SysConfEntity> findByKeyIn(Collection<String> keys);
}
