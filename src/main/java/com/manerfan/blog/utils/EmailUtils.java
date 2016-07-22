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
package com.manerfan.blog.utils;

import java.io.File;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * 邮件工具
 * </pre>
 *
 * @author ManerFan 2016年7月22日
 */
public class EmailUtils {

    private static String testMsg = "此为系统测试邮件，请勿直接回复！<br>若您看到此邮件，说明您刚配置的邮件信息正确！<br>"
            + "有疑问请联系<a href='mailto:manerfan@foxmail.com?subject=邮件配置相关疑问'>ManerFan</a> · <a href='http://www.manerfan.com'>主站</a>";

    public static void test(String stmpHost, int stmpPort, boolean sslEnable, String username,
            String password) throws EmailException {
        send(stmpHost, stmpPort, sslEnable, username, password, "MBLOG邮件测试", testMsg);
    }

    public static void send(String stmpHost, int stmpPort, boolean sslEnable, String username,
            String password, String subject, String msg) throws EmailException {
        // 配置邮件
        HtmlEmail email = configEmail(stmpHost, stmpPort, sslEnable, username, password, subject,
                msg);
        // 发送
        email.send();
    }

    public static void send(String stmpHost, int stmpPort, boolean sslEnable, String username,
            String password, String subject, String msg, File... files) throws EmailException {
        // 配置邮件
        HtmlEmail email = configEmail(stmpHost, stmpPort, sslEnable, username, password, subject,
                msg);
        // 添加附件
        for (File file : files) {
            email.attach(file);
        }
        // 发送
        email.send();
    }

    private static HtmlEmail configEmail(String stmpHost, int stmpPort, boolean sslEnable,
            String username, String password, String subject, String msg) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(stmpHost); // smtp服务地址
        // smtp服务端口
        if (sslEnable) {
            email.setSslSmtpPort(String.valueOf(stmpPort));
            email.setSSLOnConnect(true);
        } else {
            email.setSmtpPort(stmpPort);
        }
        email.setAuthentication(username, password); // 登陆邮件服务器的用户名和密码
        email.addTo(username); // 接收人
        email.setFrom(username, "MBLOG"); // 发送人
        email.setCharset("utf-8"); // 设置编码
        email.setSubject(subject); // 标题
        // 邮件内容
        if (StringUtils.hasText(msg)) {
            email.setHtmlMsg(msg);
        }
        return email;
    }
}
