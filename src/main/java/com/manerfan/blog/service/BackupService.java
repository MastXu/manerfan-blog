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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.h2.tools.Backup;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.manerfan.blog.service.article.LuceneService;
import com.manerfan.common.utils.tools.QuartzManager;
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

    @Autowired
    private SysConfService sysConfService;

    private int keep = 12;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd");

    private static final String QUARTZ_JOB_NAME = "bak_job";

    public void start() throws SchedulerException {
        update();
    }

    @PreDestroy
    public void shutdown() throws SchedulerException {
        QuartzManager.deleteJob(QUARTZ_JOB_NAME);
    }

    public void update() throws SchedulerException {
        Map<String, String> params = sysConfService.getMap(SysConfService.BACKUP_WEEK,
                SysConfService.BACKUP_HOUR, SysConfService.BACKUP_KEEP);

        keep = Integer.parseInt(params.get(SysConfService.BACKUP_KEEP));

        String cron = "0 0 " + params.get(SysConfService.BACKUP_HOUR) + " * * "
                + params.get(SysConfService.BACKUP_WEEK);

        if (QuartzManager.checkExist(QUARTZ_JOB_NAME)) {
            QuartzManager.rescheduleJob(QUARTZ_JOB_NAME, cron);
        } else {
            QuartzManager.scheduleJob(QUARTZ_JOB_NAME, BackupJob.class, cron);
        }
    }

    public void backup() throws IOException, SQLException {
        // 计算需要备份到的目录
        File bakFile = new File(resourceLocation.getBackupDir(),
                sdf.format(Calendar.getInstance().getTime()));
        if (bakFile.exists()) {
            // 如果存在，则清空
            FileUtils.deleteQuietly(bakFile);
        }

        // 按keep清除备份文件
        cleanWithKeep(keep);

        // 创建备份文件夹
        bakFile.mkdirs();

        // 备份文章
        articleBackup(bakFile);
        // 备份图片
        imageBackup(bakFile);
        // 备份lucene索引
        luceneBackup(bakFile);
        // 备份数据库
        dbBackup(bakFile);
    }

    /**
     * <pre>
     * 按keep清除备份文件
     * </pre>
     *
     * @author ManerFan 2016年8月3日
     * @param keep 仅保留最新的keep个备份
     */
    private void cleanWithKeep(int keep) {
        List<File> bakFiles = listBakFiles();
        if (ObjectUtils.isEmpty(bakFiles) || bakFiles.size() < keep) {
            // 不需要清除
            return;
        }

        // 排序
        bakFiles.sort((a, b) -> {
            if (null == a) {
                return -1;
            }

            if (null == b) {
                return 1;
            }

            return a.getAbsolutePath().compareTo(b.getAbsolutePath());
        });

        bakFiles.subList(keep, bakFiles.size()).forEach(file -> {
            // 删除文件夹
            FileUtils.deleteQuietly(file);
            if (ObjectUtils.isEmpty(file.getParentFile().list())) {
                // 如果所在文件夹已空，则删除
                FileUtils.deleteQuietly(file.getParentFile());
            }
        });
    }

    /**
     * <pre>
     * 按照时间先后列出所有的备份文件
     * </pre>
     *
     * @author ManerFan 2016年8月3日
     * @return
     */
    private List<File> listBakFiles() {
        List<File> bakFiles = new LinkedList<>();
        Arrays.stream(resourceLocation.getBackupDir().listFiles()).forEach(ymonth -> {
            // 这里拿到年月文件夹
            Arrays.stream(ymonth.listFiles()).forEach(day -> {
                // 这里拿到日文件夹
                bakFiles.add(day);
            });
        });

        return bakFiles;
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

    /**
     * 备份文章
     */
    protected void articleBackup(File store) throws IOException {
        ZipCompressUtil.compress(resourceLocation.getArticleDir().listFiles(),
                new File(store, ARTICLE_BAK_NAME));
    }

    /**
     * 备份图片
     */
    protected void imageBackup(File store) throws IOException {
        ZipCompressUtil.compress(resourceLocation.getImageDir().listFiles(),
                new File(store, IMAGE_BAK_NAME));
    }

    /**
     * 备份lucene索引
     */
    protected void luceneBackup(File store) throws IOException {
        luceneService.backup(snapshots -> {
            ZipCompressUtil.compress(snapshots, new File(store, LUCENE_BAK_NAME));
        });
    }

    /**
     * 备份数据库
     */
    protected void dbBackup(File store) throws SQLException {
        Backup.execute(new File(store, DB_BAK_NAME).getAbsolutePath(),
                resourceLocation.getDbDir().getAbsolutePath(), null, false);
    }

    /**
     * <pre>
     * 获取文件(夹)列表
     * </pre>
     *
     * @param   rd  相对路径
     * @return
     */
    public List<BackupFile> listFiles(String rd) {
        File searchDir = StringUtils.hasText(rd) ? new File(resourceLocation.getBackupDir(), rd)
                : resourceLocation.getBackupDir();
        List<File> files = Arrays.asList(searchDir.listFiles());
        files.sort((a, b) -> {
            if (null == a) {
                return -1;
            }

            if (null == b) {
                return 1;
            }

            return a.getName().compareTo(b.getName());
        });

        List<BackupFile> backupFiles = new LinkedList<>();
        files.forEach(file -> {
            BackupFile backupFile = new BackupFile();
            backupFile.path = file.getPath();
            backupFile.name = file.getName();
            backupFile.size = file.length() / 1024.0 / 1024.0;
            backupFiles.add(backupFile);
        });
        return backupFiles;
    }

    public List<BackupFile> listFiles(String ymonth, String day) {
        return listFiles(ymonth + File.separator + day);
    }

    public static class BackupFile {
        private String name;
        private String path;
        private double size;

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public double getSize() {
            return size;
        }
    }

}
