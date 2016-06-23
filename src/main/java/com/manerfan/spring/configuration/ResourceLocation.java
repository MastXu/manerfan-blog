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
package com.manerfan.spring.configuration;

import java.io.File;

/**
 * <pre>配置资源位置</pre>
 *
 * @author ManerFan 2016年6月23日
 */
public class ResourceLocation {
    
    /**
     * 资源目录
     */
    protected File baseDir;

    /**
     * 存放图片的目录
     */
    protected File imageDir;

    /**
     * 存放文章的目录
     */
    protected File articleDir;

    /**
     * 存放索引的目录
     */
    protected File indexDir;

    /**
     * 存放数据库的目录
     */
    protected File dbDir;

    public File getImageDir() {
        return imageDir;
    }

    public File getArticleDir() {
        return articleDir;
    }

    public File getIndexDir() {
        return indexDir;
    }

    public File getDbDir() {
        return dbDir;
    }

    public File getBaseDir() {
        return baseDir;
    }

}
