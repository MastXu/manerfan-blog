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
package com.manerfan.blog.service.article;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>图片相关</pre>
 *
 * @author ManerFan 2016年3月15日
 */
@Component
public class ImageService implements InitializingBean {

    private static final SimpleDateFormat NAME_SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat PATH_SDF = new SimpleDateFormat("/yyyy/MM/");
    private static final String suffix = "[\\.jpg|\\.jpeg|\\.png|\\.gif]";
    private static final Pattern pattern = Pattern.compile(".+" + suffix);

    @Value("${article.basedir}")
    private String baseDir;

    private File imageDir;

    private File defaultImage;

    /**
     * <pre>
     * </pre>
     *
     * @param image
     * @return
     */
    public String save(MultipartFile image) {
        String imageName = image.getOriginalFilename();
        if (!pattern.matcher(imageName).matches()) {
            throw new IllegalArgumentException("文件格式错误");
        }

        String name = NAME_SDF.format(Calendar.getInstance().getTime());
        try {
            File dir = new File(imageDir, transPath(name));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            image.transferTo(new File(dir, name + getSuffix(imageName)));
            return name;
        } catch (IllegalStateException | IOException e) {
            MLogger.ROOT_LOGGER.error("Save Image File Error.", e);
            throw new IllegalArgumentException("保存图片错误");
        }
    }

    public File get(String name) {
        Pattern pattern = Pattern.compile(name + suffix);

        String path = transPath(name);
        File searchDir = new File(imageDir, path);
        File[] files = searchDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });

        return ObjectUtils.isEmpty(files) ? defaultImage : files[0];
    }

    private String transPath(String name) {
        try {
            Date storeDate = NAME_SDF.parse(name);
            return PATH_SDF.format(storeDate);
        } catch (ParseException e) {
            MLogger.ROOT_LOGGER.error("", e);
            return "/";
        }
    }

    public String getSuffix(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }

        int index = fileName.lastIndexOf(".");
        if (index < 1) {
            return "";
        }

        return fileName.substring(index).toLowerCase();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(baseDir);

        imageDir = new File(baseDir, "image");
        if (!imageDir.exists()) {
            Assert.isTrue(imageDir.mkdirs());
        }

        Assert.isTrue(imageDir.isDirectory());

        defaultImage = ResourceUtils.getFile("classpath:antitheft.jpg");
        Assert.isTrue(defaultImage.exists());
    }
}
