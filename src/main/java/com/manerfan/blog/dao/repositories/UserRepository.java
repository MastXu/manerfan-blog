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

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.common.utils.dao.repositories.BasicJpaRepository;

/**
 * <pre>用户操作接口</pre>
 *
 * @author ManerFan 2016年4月1日
 */
public interface UserRepository extends BasicJpaRepository<UserEntity, String> {
    /**
     * <pre>
     * 根据用户名查找用户
     * </pre>
     *
     * @param   name    用户名
     * @return  用户
     */
    public UserEntity findOneByName(String name);

    /**
     * <pre>
     * 查询包含role角色的用户数量
     * </pre>
     *
     * @param   role    角色
     * @return  用户数量
     */
    public Long countByRoleContaining(String role);
}
