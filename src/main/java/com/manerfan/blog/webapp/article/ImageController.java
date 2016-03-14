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
package com.manerfan.blog.webapp.article;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.manerfan.blog.webapp.ControllerBase;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>图片操作</pre>
 *
 * @author ManerFan 2016年3月14日
 */
@Controller
@RequestMapping("/image")
public class ImageController extends ControllerBase implements InitializingBean {

    @Value("${article.basedir}")
    private String baseDir;

    private File imageDir;

    private File defaultImage;

    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(@RequestParam(name = "image", required = false) MultipartFile image) {
        Map<String, Object> data = makeAjaxData();

        if (null == image) {
            data.put(ERRMSG, "请选择图片上传");
            return data;
        }

        String name = String.valueOf(Calendar.getInstance().getTime().getTime());
        try {
            image.transferTo(new File(imageDir, name + getSuffix(image.getName())));
            data.put("url", "/image/" + name);
        } catch (IllegalStateException | IOException e) {
            data.put(ERRMSG, "保存图片失败");
            MLogger.ROOT_LOGGER.error("", e);
        }

        return data;
    }

    @RequestMapping("/{name}")
    public ResponseEntity<byte[]> image(@PathVariable("name") String name) throws IOException {
        File image = imageDir; // TODO

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("inline", name); // attachment弹出下载框
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(image), headers,
                HttpStatus.CREATED);
    }

    private String getSuffix(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }

        int index = fileName.lastIndexOf(".");
        if (index < 1) {
            return "";
        }

        return fileName.substring(index);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(baseDir);

        imageDir = new File(baseDir, "image");
        if (!imageDir.exists()) {
            Assert.isTrue(imageDir.mkdirs());
        }

        Assert.isTrue(imageDir.isDirectory());
    }
}
