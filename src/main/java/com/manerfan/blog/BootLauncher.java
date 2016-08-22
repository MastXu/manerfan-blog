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
package com.manerfan.blog;

import java.io.IOException;

import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextClosedEvent;

import com.manerfan.blog.dao.entities.SysConfEntity;
import com.manerfan.blog.service.BackupService;
import com.manerfan.blog.service.SysConfService;
import com.manerfan.common.utils.beans.SpringBeanFactory;
import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.tools.QuartzManager;
import com.manerfan.spring.configuration.MBlogConfiguration;

/**
 * <pre>
 * 主入口
 * 命令解析采用 apache commons-cli
 * </pre>
 *
 * @author ManerFan 2016年2月24日
 */
@SpringBootApplication
@Import(MBlogConfiguration.class)
public class BootLauncher {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                try {
                    while ('\n' != System.in.read()) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }.start();

        SpringApplication application = new SpringApplication(BootLauncher.class);
        application.addListeners(new ApplicationListener<ApplicationReadyEvent>() {
            /* 系统启动时 */
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                // 初始化系统参数
                initSysConf();

                // 启动系统备份quartz任务
                startBackupService();
            }
        }, new ApplicationListener<ContextClosedEvent>() {
            /* 系统关闭时 */
            @Override
            public void onApplicationEvent(ContextClosedEvent event) {
                try {
                    QuartzManager.shutdown();
                } catch (SchedulerException e) {
                    MLogger.ROOT_LOGGER.error("Cannot shutdown Quartz Task", e);
                }
            }
        });
        application.run(args);
    }

    private static void initSysConf() {
        SysConfService sysConfService = SpringBeanFactory.getBean(SysConfService.class);

        SysConfEntity week = sysConfService.get(SysConfService.BACKUP_WEEK);
        if (null == week) {
            sysConfService.updateOrSave(SysConfService.BACKUP_WEEK, "SUN");
        }

        SysConfEntity hour = sysConfService.get(SysConfService.BACKUP_HOUR);
        if (null == hour) {
            sysConfService.updateOrSave(SysConfService.BACKUP_HOUR, 3);
        }

        SysConfEntity keep = sysConfService.get(SysConfService.BACKUP_KEEP);
        if (null == keep) {
            sysConfService.updateOrSave(SysConfService.BACKUP_KEEP, 12);
        }
    }

    private static void startBackupService() {
        BackupService backupService = SpringBeanFactory.getBean(BackupService.class);
        try {
            backupService.start();
        } catch (SchedulerException e) {
            MLogger.ROOT_LOGGER.error("Cannot Start Quartz Task", e);
            System.exit(1);
        }
    }

}
