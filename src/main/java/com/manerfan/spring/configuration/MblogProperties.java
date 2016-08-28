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

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * <pre>
 * 博客运行配置
 * </pre>
 *
 * @author ManerFan 2016年8月28日
 */
@ConfigurationProperties(prefix = "mblog", ignoreUnknownFields = true)
public class MblogProperties implements InitializingBean {

    /**
     * mblog.version
     * 
     * 博客版本
     */
    private String version;

    /**
     * mblog.workDir
     * mblog.work-dir
     * 
     * 博客运行资源目录
     */
    @NotNull
    private String workDir;

    /**
     * mblog.h2dbDir
     * mblog.h2db-dir
     * 
     * H2DB目录
     */
    @NotNull
    private String h2dbDir;

    /**
     * mblog.webappDir
     * mblog.webapp-dir
     * 
     * webapp目录
     */
    @NotNull
    private String webappDir;

    public String getWebappDir() {
        return webappDir;
    }

    public void setWebappDir(String webappDir) {
        this.webappDir = webappDir;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getH2dbDir() {
        return h2dbDir;
    }

    public void setH2dbDir(String h2dbDir) {
        this.h2dbDir = h2dbDir;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

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

    /**
     * 存在备份的目录
     */
    protected File backupDir;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(workDir);
        Assert.hasText(h2dbDir);

        File workDir = new File(this.workDir);
        if (!workDir.exists()) {
            Assert.isTrue(workDir.mkdirs());
        }

        File imageDir = new File(workDir, "image");
        if (!imageDir.exists()) {
            Assert.isTrue(imageDir.mkdirs());
        }
        this.imageDir = imageDir;

        File articleDir = new File(workDir, "article");
        if (!articleDir.exists()) {
            Assert.isTrue(articleDir.mkdirs());
        }
        this.articleDir = articleDir;

        File indexDir = new File(workDir, "index");
        if (!indexDir.exists()) {
            Assert.isTrue(indexDir.mkdirs());
        }
        this.indexDir = indexDir;

        File dbDir = new File(h2dbDir);
        if (!dbDir.exists()) {
            Assert.isTrue(dbDir.mkdirs());
        }
        this.dbDir = dbDir;

        File backupDir = new File(workDir, "backup");
        if (!backupDir.exists()) {
            Assert.isTrue(backupDir.mkdirs());
        }
        this.backupDir = backupDir;
    }

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

    public File getBackupDir() {
        return backupDir;
    }

}
