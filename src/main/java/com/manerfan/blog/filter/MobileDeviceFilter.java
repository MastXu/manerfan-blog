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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.manerfan.common.utils.logger.MLogger;
import com.manerfan.common.utils.mather.NonePathMatcher;
import com.manerfan.common.utils.mather.OrPathMatcher;

/**
 * <pre>
 * 判断请求是否来自移动设备
 * </pre>
 *
 * @author ManerFan 2016年6月17日
 */
public class MobileDeviceFilter extends OncePerRequestFilter {

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
    // 字符串在编译时会被转码一次,所以是 "\\b"    
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
    private static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            + "|windows (phone|ce)|blackberry"
            + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            + "|laystation portable)|nokia|fennec|htc[-_]"
            + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    private static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    //移动设备正则匹配：手机端、平板  
    private static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    private static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /** 
     * 检测是否是移动设备访问 
     *  
     * @Title: check 
     * @Date : 2014-7-7 下午01:29:07 
     * @param userAgent 浏览器标识 
     * @return true:移动设备接入，false:pc端接入 
     */
    private static boolean fromMobile(String userAgent) {
        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配    
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find() || matcherTable.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 白名单
     */
    private String whiteList;

    private OrPathMatcher whiteListMatcher;

    private static final String SPLITER = "[ ,\t\n]+";

    private static byte[] notSupportedPageBytes;

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();

        /*
         * 初始化白名单 
         */
        if (StringUtils.hasText(whiteList)) {
            String[] request = whiteList.split(SPLITER);
            if (ObjectUtils.isEmpty(request)) {
                whiteListMatcher = new OrPathMatcher(NonePathMatcher.INSTANCE);
            } else {
                whiteListMatcher = new OrPathMatcher(new AntPathMatcher("/"), request);
            }
        } else {
            whiteListMatcher = new OrPathMatcher(NonePathMatcher.INSTANCE);
        }

        try {
            notSupportedPageBytes = FileUtils
                    .readFileToByteArray(ResourceUtils.getFile("classpath:notSupported.html"));
        } catch (IOException e) {
            MLogger.ROOT_LOGGER.error("Cannot Found or Read NotSupported Html");
            throw new ServletException("Cannot Found or Read NotSupported Html");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        //获取ua，用来判断是否为移动端访问  
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        if (!StringUtils.hasLength(userAgent)) {
            userAgent = "";
        }

        if (fromMobile(userAgent) && !whiteListMatcher.matches(request.getRequestURI())) {
            /* 暂不支持移动设备 */
            notSupported(response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void notSupported(HttpServletResponse response) throws IOException {
        response.setContentLength(notSupportedPageBytes.length);
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(201);

        ServletOutputStream os = response.getOutputStream();
        os.write(notSupportedPageBytes);
        os.flush();
        response.flushBuffer();
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

}
