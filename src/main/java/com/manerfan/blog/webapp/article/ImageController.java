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

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.manerfan.blog.service.article.ImageService;
import com.manerfan.blog.service.article.resource.ImageCachingFile;
import com.manerfan.blog.webapp.ControllerBase;

/**
 * <pre>图片操作</pre>
 *
 * @author ManerFan 2016年3月14日
 */
@Controller
@RequestMapping("/article/image")
public class ImageController extends ControllerBase {

    @Autowired
    private ImageService imageService;

    /**
     * <pre>
     * 上传保存图片
     * </pre>
     *
     * @param image
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(@RequestParam(name = "image", required = false) MultipartFile image) {
        Map<String, Object> data = makeAjaxData();

        if (null == image) {
            data.put(ERRMSG, "请选择图片上传");
            return data;
        }

        try {
            String name = imageService.save(image);
            data.put("url", "/article/image/inline/" + name);
        } catch (IllegalArgumentException e) {
            data.put(ERRMSG, e.getMessage());
        }

        return data;
    }

    /**
     * <pre>
     * 根据图片标识预览/下载图片
     * </pre>
     *
     * @param name  图片标识
     * @return
     * @throws IOException
     */
    @RequestMapping("/{dispos}/{name}")
    public ResponseEntity<byte[]> view(
            @PathVariable("dispos") String dispos/*inline预览;attachment下载;*/,
            @PathVariable("name") String name) throws IOException {
        ImageCachingFile image = imageService.get(name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType(image.getFileName()));
        headers.add("Content-Disposition", dispos + "; filename=\"" + image.getFileName() + "\"");
        return new ResponseEntity<byte[]>(image.getContent(), headers, HttpStatus.CREATED);
    }

    /**
     * <pre>
     * 删除图片
     * </pre>
     *
     * @param   name 图片名
     * @return
     */
    @RequestMapping("/delete/{name}")
    @ResponseBody
    public Object delete(@PathVariable String name) {
        Map<String, Object> data = makeAjaxData();
        imageService.delete(name);
        return data;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list() {
        Map<String, Object> data = makeAjaxData();
        data.put("dirs", imageService.listFiles(null));
        return data;
    }

    @RequestMapping("/list/{year}")
    @ResponseBody
    public Object list(@PathVariable("year") String year) {
        Map<String, Object> data = makeAjaxData();
        data.put("dirs", imageService.listFiles(year));
        return data;
    }

    @RequestMapping("/list/{year}/{month}")
    @ResponseBody
    public Object list(@PathVariable("year") String year, @PathVariable("month") String month) {
        Map<String, Object> data = makeAjaxData();
        data.put("imgs", imageService.listFiles(year, month));
        return data;
    }

    private MediaType mediaType(String name) {
        String suffix = imageService.getSuffix(name);
        switch (suffix) {
            case ".jpg":
            case ".jpeg":
                return MediaType.IMAGE_JPEG;
            case ".gif":
                return MediaType.IMAGE_GIF;
            case ".png":
            default:
                return MediaType.IMAGE_PNG;
        }
    }

}
