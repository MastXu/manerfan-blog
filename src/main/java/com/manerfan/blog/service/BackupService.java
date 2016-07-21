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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PreDestroy;

import org.h2.tools.Backup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manerfan.blog.service.article.LuceneService;
import com.manerfan.common.utils.tools.ZipCompressUtil;
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

    @Autowired
    private LuceneService luceneService;

    public static void start() {
        // TODO
    }

    @PreDestroy
    public static void shutdown() {
        // TODO
    }

    /**
     * 文章备份文件名
     */
    private static final String ARTICLE_BAK_NAME = "article.bak.zip";

    /**
     * 图片备份文件名
     */
    private static final String IMAGE_BAK_NAME = "image.bak.zip";

    /**
     * Lucene索引本分文件名
     */
    private static final String LUCENE_BAK_NAME = "lucene.bak.zip";

    /**
     * 数据库备份文件名
     */
    private static final String DB_BAK_NAME = "db.bak.zip";

    protected void articleBackup(File store) throws IOException {
        ZipCompressUtil.compress(resourceLocation.getArticleDir().listFiles(),
                new File(store, ARTICLE_BAK_NAME));
    }

    protected void imageBackup(File store) throws IOException {
        ZipCompressUtil.compress(resourceLocation.getImageDir().listFiles(),
                new File(store, IMAGE_BAK_NAME));
    }

    protected void luceneBackup(File store) throws IOException {
        luceneService.backup(snapshots -> {
            ZipCompressUtil.compress(snapshots, new File(store, LUCENE_BAK_NAME));
        });
    }

    protected void dbBackup(File store) throws SQLException {
        Backup.execute(new File(store, DB_BAK_NAME).getAbsolutePath(),
                resourceLocation.getDbDir().getAbsolutePath(), null, false);
    }

}
