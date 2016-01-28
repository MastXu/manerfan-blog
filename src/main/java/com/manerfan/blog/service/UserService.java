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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.manerfan.blog.dao.entities.UserEntity;
import com.manerfan.common.utils.dao.common.CriteriaSet;
import com.manerfan.common.utils.dao.common.IRepositoryUtils;
import com.manerfan.common.utils.dao.exception.DaoCommonException;
import com.manerfan.common.utils.logger.Logger;

/**
 * <pre>用户操作</pre>
 *
 * @author ManerFan 2016年1月28日
 */
@Service
public class UserService {

    @Autowired
    private IRepositoryUtils repositoryUtils;

    public UserEntity findByName(String name) {
        try {
            return repositoryUtils.findUniqByAttrEqual("name", name, UserEntity.class);
        } catch (DaoCommonException e) {
            Logger.LOGGER.error("Cannot Find an User on Name[{}]", name, e);
            return null;
        }
    }

    public List<UserEntity> findAdmins() {
        try {
            return repositoryUtils.listResult(new CriteriaSet() {
                @Override
                public void criteriaSet(Criteria criteria) {
                    criteria.add(
                            Restrictions.ilike("role", UserEntity.ROLE_ADMIN, MatchMode.ANYWHERE));
                }
            }, UserEntity.class);
        } catch (DaoCommonException e) {
            Logger.LOGGER.error("Cannot Fina any Admins.", e);
            return null;
        }
    }

    public boolean createAdmin(UserEntity user) {
        try {
            user.setRole(UserEntity.ROLE_ADMIN);
            if (!StringUtils.hasText(user.getEmail())) {
                user.setEmail(null);
            }
            if (!StringUtils.hasText(user.getAvatar())) {
                user.setAvatar(null);
            }
            repositoryUtils.save(user);
            return true;
        } catch (Exception e) {
            Logger.LOGGER.error("Cannot Create an User[{}]", user, e);
            return false;
        }
    }

}
