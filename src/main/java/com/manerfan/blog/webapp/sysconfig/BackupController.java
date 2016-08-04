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
package com.manerfan.blog.webapp.sysconfig;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manerfan.blog.service.BackupService;
import com.manerfan.blog.service.SysConfService;
import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.tools.ZipCompressUtil;
import com.manerfan.spring.configuration.ResourceLocation;

/**
 * <pre>
 * 系统备份
 * </pre>
 *
 * @author ManerFan 2016年8月3日
 */
@Controller
@RequestMapping("/sysconfig/backup")
public class BackupController extends ControllerBase {

    @Autowired
    private SysConfService sysConfService;

    @Autowired
    private BackupService backupService;

    @Autowired
    private ResourceLocation resourceLocation;

    @RequestMapping("/update")
    @ResponseBody
    public Object updateBackupConf(@RequestParam String week, @RequestParam String hour,
            @RequestParam int keep) {
        Map<String, Object> data = makeAjaxData();

        if (!StringUtils.hasText(week) || !StringUtils.hasText(hour)) {
            data.put(ERRMSG, "数据不完整");
            return data;
        }

        if (keep < 0 || keep > 60) {
            data.put(ERRMSG, "保留个数必须在[1, 60]之间");
            return data;
        }

        try {
            // 保存数据
            sysConfService.updateOrSave(SysConfService.BACKUP_WEEK, week);
            sysConfService.updateOrSave(SysConfService.BACKUP_HOUR, hour);
            sysConfService.updateOrSave(SysConfService.BACKUP_KEEP, keep);

            // 更新quartz任务
            backupService.update();
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when update Backup Sysconfig", e);
            data.put(ERRMSG, "内部错误");
            return data;
        }

        return data;
    }

    @RequestMapping("/immediately")
    @ResponseBody
    public Object immediately() {
        Map<String, Object> data = makeAjaxData();

        try {
            if (backupService.isBackupRunning()) {
                data.put(ERRMSG, "备份进程正在运行");
                return data;
            }
            backupService.backup();
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Some Error Occured when Backup SysData immediately.", e);
            data.put(ERRMSG, "内部错误");
            return data;
        }

        return data;
    }

    @RequestMapping("/query")
    @ResponseBody
    public Object query() {
        Map<String, Object> data = makeAjaxData();
        data.put("dirs", backupService.listFiles(null));
        return data;
    }

    @RequestMapping("/query/{ymonth}")
    @ResponseBody
    public Object query(@PathVariable("ymonth") String ymonth) {
        Map<String, Object> data = makeAjaxData();
        data.put("dirs", backupService.listFiles(ymonth));
        return data;
    }

    @RequestMapping("/query/{ymonth}/{day}")
    @ResponseBody
    public Object query(@PathVariable("ymonth") String ymonth, @PathVariable("day") String day) {
        Map<String, Object> data = makeAjaxData();
        data.put("zips", backupService.listFiles(ymonth, day));
        return data;
    }

    @RequestMapping("/download")
    public void download(@RequestParam String path, @RequestParam String name,
            HttpServletResponse response) throws IOException {
        OutputStream os = null;
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + ";charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            os = response.getOutputStream();

            ZipCompressUtil.compress(Arrays.asList(new File(resourceLocation.getBackupDir(), path)),
                    os);
        } finally {
            if (null != os) {
                os.flush();
                os.close();
            }
        }
    }

}
