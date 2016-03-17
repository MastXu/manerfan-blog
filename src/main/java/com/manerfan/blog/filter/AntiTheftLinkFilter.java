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
package com.manerfan.blog.filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.mather.AnyPathMatcher;
import com.manerfan.common.utils.mather.OrPathMatcher;

/**
 * <pre>防盗链</pre>
 *
 * @author ManerFan 2016年1月24日
 */
public class AntiTheftLinkFilter extends OncePerRequestFilter {

    private static File antiTheftImage;

    private static final String SPLITER = "[ ,\t\n]+";

    /**
     * referer白名单
     */
    private String refererWhiteList;

    /**
     * 需要过滤的资源
     */
    private String filteredResources;

    /**
     * 如果没有referer，是否允许访问资源
     */
    private boolean accessNonReferer = true;

    private OrPathMatcher whiteListMatcher;

    private OrRequestMatcher resourcesMatcher;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();

        /*
         * 初始化referer白名单 
         */
        if (StringUtils.hasText(refererWhiteList)) {
            String[] referers = refererWhiteList.split(SPLITER);
            if (ObjectUtils.isEmpty(referers)) { // 不过滤
                whiteListMatcher = new OrPathMatcher(AnyPathMatcher.INSTANCE);
            } else {
                whiteListMatcher = new OrPathMatcher(new AntPathMatcher("."/*对于域名来讲，路径分隔符是"."*/),
                        referers);
            }
        } else {
            whiteListMatcher = new OrPathMatcher(AnyPathMatcher.INSTANCE);
        }

        /*
         * 初始化过滤资源
         */
        if (StringUtils.hasText(filteredResources)) {
            String[] resources = filteredResources.split(SPLITER);
            if (ObjectUtils.isEmpty(resources)) {
                resourcesMatcher = new OrRequestMatcher(AnyRequestMatcher.INSTANCE);
            } else {
                List<RequestMatcher> matchers = new LinkedList<>();
                for (String resource : resources) {
                    matchers.add(new AntPathRequestMatcher(resource));
                }
                resourcesMatcher = new OrRequestMatcher(matchers);
            }
        } else {
            resourcesMatcher = new OrRequestMatcher(AnyRequestMatcher.INSTANCE);
        }

        try {
            antiTheftImage = ResourceUtils.getFile("classpath:antitheft.jpg");
        } catch (FileNotFoundException e) {
            MLogger.ROOT_LOGGER.error("Cannot Found AntiThelft Image");
            throw new ServletException("Cannot Found AntiThelft Image");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (resourcesMatcher.matches(request)) { // 防盗链资源
            handleAntiTheftLink(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void handleAntiTheftLink(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        String referer = request.getHeader("referer");
        if (StringUtils.hasText(referer)) {
            if (whiteListMatcher.matches(referer)) {
                filterChain.doFilter(request, response);
            } else { // 不在白名单内
                doAntiTheft(response);
            }
        } else { // 没有referer
            if (accessNonReferer) {
                filterChain.doFilter(request, response);
            } else {
                doAntiTheft(response);
            }
        }
    }

    private void doAntiTheft(HttpServletResponse response) throws IOException {
        response.addHeader("Content-Disposition", "inline; filename=\"ManerFanAntiTheft\"");
        response.setContentType("image/jpeg");
        response.setStatus(201);

        ServletOutputStream os = response.getOutputStream();
        os.write(FileUtils.readFileToByteArray(antiTheftImage));
        os.flush();
        response.flushBuffer();
    }

    public void setRefererWhiteList(String refererWhiteList) {
        this.refererWhiteList = refererWhiteList;
    }

    public void setFilteredResources(String filteredResources) {
        this.filteredResources = filteredResources;
    }

    public void setAccessNonReferer(boolean accessNonReferer) {
        this.accessNonReferer = accessNonReferer;
    }

}
