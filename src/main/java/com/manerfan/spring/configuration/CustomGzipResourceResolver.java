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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.EncodedResource;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 * <pre>
 * 自定义gzip资源压缩
 * </pre>
 *
 * @author ManerFan 2016年3月25日
 */
public class CustomGzipResourceResolver extends GzipResourceResolver {

    private static boolean debug;

    public static void setDebug(boolean debug) {
        CustomGzipResourceResolver.debug = debug;
    }

    @Override
    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {

        Resource resource = chain.resolveResource(request, requestPath, locations);
        if ((resource == null) || (request != null && !isGzipAccepted(request))) {
            return resource;
        }

        try {
            Resource gzipped = new GzippedResource(resource);
            if (gzipped.exists()) {
                return gzipped;
            }
        } catch (IOException | CompressorException ex) {
            logger.trace("No gzipped resource for [" + resource.getFilename() + "]", ex);
        }

        return resource;
    }

    private boolean isGzipAccepted(HttpServletRequest request) {
        String value = request.getHeader("Accept-Encoding");
        return (value != null && value.toLowerCase().contains("gzip"));
    }

    private static final class GzippedResource extends AbstractResource implements EncodedResource {

        private final int BUFFER = 1024;

        private final Resource original;

        private final Resource gzipped;

        public GzippedResource(Resource original) throws IOException, CompressorException {
            this.original = original;
            this.gzipped = original.createRelative(original.getFilename() + ".gz");

            /* 如果original存在且gzip不存在，则创建gzip */
            if (!debug && this.original.exists() && !exists()) {
                InputStream is = this.original.getInputStream();
                OutputStream os = new FileOutputStream(this.gzipped.getFile());

                CompressorOutputStream gzippedOut = new CompressorStreamFactory()
                        .createCompressorOutputStream(CompressorStreamFactory.GZIP, os);

                int count;
                byte data[] = new byte[BUFFER];
                while ((count = is.read(data, 0, BUFFER)) != -1) {
                    gzippedOut.write(data, 0, count);
                }

                gzippedOut.flush();
                gzippedOut.close();
                is.close();
            }
        }

        public InputStream getInputStream() throws IOException {
            return this.gzipped.getInputStream();
        }

        public boolean exists() {
            return this.gzipped.exists();
        }

        public boolean isReadable() {
            return this.gzipped.isReadable();
        }

        public boolean isOpen() {
            return this.gzipped.isOpen();
        }

        public URL getURL() throws IOException {
            return this.gzipped.getURL();
        }

        public URI getURI() throws IOException {
            return this.gzipped.getURI();
        }

        public File getFile() throws IOException {
            return this.gzipped.getFile();
        }

        public long contentLength() throws IOException {
            return this.gzipped.contentLength();
        }

        public long lastModified() throws IOException {
            return this.gzipped.lastModified();
        }

        public Resource createRelative(String relativePath) throws IOException {
            return this.gzipped.createRelative(relativePath);
        }

        public String getFilename() {
            return this.original.getFilename();
        }

        public String getDescription() {
            return this.gzipped.getDescription();
        }

        public String getContentEncoding() {
            return "gzip";
        }
    }

}
