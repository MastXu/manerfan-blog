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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.manerfan.blog.service.article.resource.ImageCachingFile;
import com.manerfan.spring.configuration.MblogProperties;

/**
 * <pre>图片相关</pre>
 *
 * @author ManerFan 2016年3月15日
 */
@Service
public class ImageService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private static final SimpleDateFormat NAME_SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat PATH_SDF = new SimpleDateFormat("/yyyy/MM/");
    private static final String suffix = "\\.(jpe?g|png|gif)";
    private static final Pattern pattern = Pattern.compile(".+" + suffix);

    @Autowired
    private MblogProperties mblogProperties;

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
            File dir = new File(mblogProperties.getImageDir(), transPath(name));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            image.transferTo(new File(dir, name + getSuffix(imageName)));
            return name;
        } catch (IllegalStateException | IOException e) {
            LOGGER.error("Save Image File Error.", e);
            throw new IllegalArgumentException("保存图片错误");
        }
    }

    /**
     * <pre>
     * 获取图片
     * </pre>
     *
     * @param name
     * @return
     * @throws IOException
     */
    @Cacheable(cacheNames = "resources-cache", keyGenerator = "ResourceKeyGenerator")
    public ImageCachingFile get(String name) throws IOException {
        File[] files = getImageFiles(name);

        ImageCachingFile image = new ImageCachingFile();
        File imageFile = defaultImage;
        if (!ObjectUtils.isEmpty(files)) {
            imageFile = files[0];
        }

        image.setFileName(imageFile.getName());
        image.setContent(FileUtils.readFileToByteArray(imageFile));
        return image;
    }

    private File[] getImageFiles(String name) {
        Pattern pattern = Pattern.compile(name + suffix);

        String path = transPath(name);
        File searchDir = new File(mblogProperties.getImageDir(), path);
        File[] files = searchDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });
        return files;
    }

    /**
     * <pre>
     * 删除图片
     * </pre>
     *
     * @param name
     */
    @CacheEvict(cacheNames = "resources-cache", keyGenerator = "ResourceKeyGenerator", beforeInvocation = true)
    public void delete(String name) {
        File[] files = getImageFiles(name);

        if (!ObjectUtils.isEmpty(files)) {
            Arrays.asList(files).forEach(file -> {
                file.delete(); // 删除图片文件
                if (ObjectUtils.isEmpty(file.getParentFile().list())) {
                    // 如果所在文件夹已空，则删除
                    file.getParentFile().delete();
                }
            });
        }
    }

    /**
     * <pre>
     * 获取文件(夹)列表
     * </pre>
     *
     * @param   rd  相对路径
     * @return
     */
    public List<String> listFiles(String rd) {
        File searchDir = StringUtils.hasText(rd) ? new File(mblogProperties.getImageDir(), rd)
                : mblogProperties.getImageDir();
        List<String> files = Arrays.asList(searchDir.list());
        Collections.sort(files);
        return files;
    }

    public List<String> listFiles(String year, String month) {
        return listFiles(year + File.separator + month);
    }

    private String transPath(String name) {
        try {
            Date storeDate = NAME_SDF.parse(name);
            return PATH_SDF.format(storeDate);
        } catch (ParseException e) {
            LOGGER.error("", e);
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
        defaultImage = ResourceUtils
                .getFile(mblogProperties.getWebappDir() + "/view/images/antitheft.jpg");
        Assert.isTrue(defaultImage.exists());
    }

}
