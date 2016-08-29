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
package com.manerfan.blog.dao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.manerfan.jpa.entities.CommonEntity;


/**
 * <pre>用户</pre>
 *
 * @author ManerFan 2016年1月24日
 */
@Entity(name = "User")
@Table(name = "user", indexes = @Index(name = "user_name_index", columnList = "name", unique = true))
public class UserEntity extends CommonEntity {

    /**
     * UID
     */
    private static final long serialVersionUID = -6655242366381426428L;

    public transient static final String ROLE_ADMIN = "ROLE_ADMIN";
    public transient static final String ROLE_USER = "ROLE_USER";
    public transient static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public transient static final String NAME_ANONYMOUS = "anonymous";

    /**
     * 用户名
     */
    @Column(name = "name", unique = true, nullable = false, length = 12)
    private String name;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 角色
     */
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * 头像地址
     */
    @Column(name = "avatar")
    private String avatar;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserEntity other = (UserEntity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
