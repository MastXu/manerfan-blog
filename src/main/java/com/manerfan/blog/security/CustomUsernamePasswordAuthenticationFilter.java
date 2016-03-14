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
package com.manerfan.blog.security;

import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import com.manerfan.blog.service.RSAService;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>自定义登陆验证过滤器</pre>
 *
 * @author ManerFan 2016年3月8日
 */
public class CustomUsernamePasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private RSAService rsaService;

    public CustomUsernamePasswordAuthenticationFilter() {
    }

    public CustomUsernamePasswordAuthenticationFilter(String loginPage) {
        Assert.hasText(loginPage);
        setFilterProcessesUrl(loginPage);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        // rsa密文
        String passwordrsa = super.obtainPassword(request);

        // 从缓存中获取rsa私钥
        RSAPrivateKey key = rsaService.getPrivateKey(session.getId());
        if (null == key) {
            // 页面停留时间过长导致key过期
            return passwordrsa;
        }

        // 取出一个Cipher
        Cipher c = rsaService.borrowCipher();
        if (null == c) {
            return passwordrsa;
        }

        try {
            // 转换BCD
            byte[] bytersa = Hex.decodeHex(passwordrsa.toCharArray());

            // 解密
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(bytersa));
        } catch (DecoderException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            MLogger.ROOT_LOGGER.error("DecoderOrEncryptException!", e);
            return passwordrsa;
        } finally {
            if (null != c) {
                // 最后无论如何都要把Cipher还回去
                rsaService.returnCipher(c);
            }
        }
    }

    public void setRsaService(RSAService rsaService) {
        this.rsaService = rsaService;
    }

}
