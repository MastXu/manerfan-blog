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
package com.manerfan.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.common.utils.dao.common.IRepositoryUtils;
import com.manerfan.common.utils.dao.exception.DaoCommonException;

/**
 * <pre>用户信息</pre>
 *
 * @author ManerFan 2016年1月24日
 */
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IRepositoryUtils repositoryUtils;

    /**
     * 加载用户信息
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = repositoryUtils.findUniqByAttrEqual("name", username,
                    UserEntity.class);
            if (null == userEntity) {
                throw new UsernameNotFoundException("Cannot Find any User for " + username);
            }

            return new User(username, userEntity.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList(userEntity.getRole().split(",")));
        } catch (DaoCommonException e) {
            throw new UsernameNotFoundException("", e);
        }

    }

}
