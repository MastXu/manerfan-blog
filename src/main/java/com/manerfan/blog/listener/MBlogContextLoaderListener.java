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
package com.manerfan.blog.listener;

import javax.servlet.ServletContextEvent;

import org.quartz.SchedulerException;
import org.springframework.web.context.ContextLoaderListener;

import com.manerfan.blog.dao.entities.SysConfEntity;
import com.manerfan.blog.service.BackupService;
import com.manerfan.blog.service.SysConfService;
import com.manerfan.common.utils.beans.SpringBeanFactory;
import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.tools.QuartzManager;

/**
 * <pre>
 * 上下文初始化
 * </pre>
 *
 * @author ManerFan 2016年8月2日
 */
public class MBlogContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);

        // 初始化系统参数
        initSysConf();

        // 启动系统备份quartz任务
        BackupService backupService = SpringBeanFactory.getBean(BackupService.class);
        try {
            backupService.start();
        } catch (SchedulerException e) {
            MLogger.ROOT_LOGGER.error("Cannot Start Quartz Task", e);
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);

        try {
            QuartzManager.shutdown();
        } catch (SchedulerException e) {
            MLogger.ROOT_LOGGER.error("Cannot shutdown Quartz Task", e);
        }
    }

    private void initSysConf() {
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

}
