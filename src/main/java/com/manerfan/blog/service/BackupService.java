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

import com.manerfan.spring.configuration.ResourceLocation;

/**
 * <pre>
 * 实现数据备份
 * 
 * 文章、图片、数据库、lucene index 等
 * </pre>
 *
 * @author ManerFan 2016年7月17日
 */
@Service
public class BackupService {

    @Autowired
    private ResourceLocation resourceLocation;

    protected void articleBackup() {

    }

    protected void imageBackup() {

    }

    protected void luceneBackup() {

    }

    protected void dbBackup() {

    }

}
