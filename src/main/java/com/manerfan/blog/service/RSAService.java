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
package com.manerfan.blog.service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.manerfan.blog.service.rsapool.CipherPoolFactory;
import com.manerfan.blog.service.rsapool.KeyPairPoolFactory;
import com.manerfan.common.utils.logger.MLogger;

/**
 * <pre>rsa密钥缓存</pre>
 *
 * @author ManerFan 2016年3月8日
 */
@Component
public class RSAService implements InitializingBean {

    @Autowired
    private CacheManager cacheManager;
    private Cache cache;

    private GenericObjectPool<KeyPair> keyPairPool;
    private GenericObjectPool<Cipher> cipherPool;

    /**
     * <pre>
     * 将RSAPrivateKey放入缓存中
     * </pre>
     *
     * @param   id  session id
     * @param   key RSAPrivateKey
     */
    public void putPrivateKey(String id, RSAPrivateKey key) {
        if (!StringUtils.hasText(id) || null == key) {
            return;
        }

        cache.put(id, key);
    }

    /**
     * <pre>
     * 从缓存中提取RSAPrivateKey
     * </pre>
     *
     * @param   id    session id
     * @return  RSAPrivateKey
     */
    public RSAPrivateKey getPrivateKey(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }

        ValueWrapper value = cache.get(id);
        if (null == value) {
            return null;
        }

        return (RSAPrivateKey) value.get();
    }

    /**
     * <pre>
     * 从密钥池中取出一对儿RSA密钥
     * </pre>
     *
     * @return
     */
    public KeyPair getKeyPair() {
        try {
            // 从池中取出
            KeyPair keyPair = keyPairPool.borrowObject();
            // 随即将其从池中销毁(对于密钥只能使用一次)
            // 这里主要使用BaseGenericObjectPool.Evictor定时器，在后台线程中自动为keypair池创建密钥
            keyPairPool.invalidateObject(keyPair);

            return keyPair;
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Borrow KeyPair Error!", e);
            return null;
        }

    }

    /**
     * <pre>从池中拿一个cipher，使用完毕后需要使用{@code returnCipher}归还</pre>
     *
     * @return RSA Cipher
     */
    public Cipher borrowCipher() {
        try {
            return cipherPool.borrowObject();
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("Borrow Cipher Error!", e);
            return null;
        }
    }

    /**
     * <pre>归还cipher，归还的cipher必须为借出去的cipher</pre>
     *
     * @param c 归还的cipher，必须为借出去的cipher
     */
    public void returnCipher(Cipher c) {
        if (null == c) {
            return;
        }

        cipherPool.returnObject(c);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache("rsa-keypair-cache");

        initKeyPairPool();
        initCipherPool();
    }

    /**
     * 初始化Cipher池
     * @throws Exception 
     */
    private void initKeyPairPool() throws Exception {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        /* 设置池中最大idle，若idle数量大于此值，则会清理多余idle */
        poolConfig.setMaxIdle(20);
        /* 设置多久没有borrow则设置为idle(5分钟) */
        poolConfig.setMinEvictableIdleTimeMillis(5 * 60 * 1000);

        /* 设置池中最小idle，若idle数量小于此值，则在Evictor定时器中会自动创建idle */
        poolConfig.setMinIdle(10);
        /* 设置Evictor定时器周期并启动定时器(30秒) */
        poolConfig.setTimeBetweenEvictionRunsMillis(30 * 1000);

        /* 设置池中最大数量，若达到上限时borrow，则阻塞 */
        poolConfig.setMaxTotal(50);

        keyPairPool = new GenericObjectPool<>(new KeyPairPoolFactory(), poolConfig);
        // 初始化池中idle keypare个数到minIdle，提前create，免得在使用时再创建
        // BaseGenericObjectPool.Evictor定时器会定时执行ensureMinIdle，确保idle keypare个数可以达到minIdle
        keyPairPool.preparePool();
    }

    /**
     * 初始化Cipher池
     * @throws Exception 
     */
    private void initCipherPool() throws Exception {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        /* 设置池中最大idle，若idle数量大于此值，则会清理多余idle */
        poolConfig.setMaxIdle(10);
        /* 设置多久没有borrow则设置为idle(1小时) */
        poolConfig.setMinEvictableIdleTimeMillis(60 * 60 * 1000);

        /* 设置池中最小idle，若idle数量小于此值，则在Evictor定时器中会自动创建idle */
        poolConfig.setMinIdle(5);
        /* 设置Evictor定时器周期并启动定时器(30秒) */
        poolConfig.setTimeBetweenEvictionRunsMillis(30 * 1000);

        /* 设置池中最大数量，若达到上限时borrow，则阻塞 */
        poolConfig.setMaxTotal(20);

        cipherPool = new GenericObjectPool<>(new CipherPoolFactory(), poolConfig);
        cipherPool.preparePool();
    }

    public String decode(RSAPrivateKey key, String passwordrsa) throws Exception {
        // 取出一个Cipher
        Cipher c = borrowCipher();
        if (null == c) {
            MLogger.ROOT_LOGGER.warn("Borrow RSA Cipher Failed.");
            throw new InternalError("Borrow RSA Cipher Failed.");
        }

        try {
            // 转换BCD
            byte[] bytersa = Hex.decodeHex(passwordrsa.toCharArray());

            // 解密
            c.init(Cipher.DECRYPT_MODE, key);

            // 加盐
            return new String(c.doFinal(bytersa));
        } finally {
            if (null != c) {
                // 最后无论如何都要把Cipher还回去
                returnCipher(c);
            }
        }

    }

    public String decodeAddSalt(RSAPrivateKey key, String passwordrsa) {
        try {
            // 加盐
            return addSalt(decode(key, passwordrsa));
        } catch (Exception e) {
            MLogger.ROOT_LOGGER.error("DecoderOrEncryptException!", e);
            throw new InternalAuthenticationServiceException("登录失败，请联系管理员或重新登陆", e);
        }
    }

    /**
     * <pre>
     * 加盐处理
     * </pre>
     *
     * @param s 密码
     * @return  sha256(passwd+salt)
     */
    public String addSalt(String s) {
        String salt = calculateSalt(s); /* 动态计算盐值 */
        return DigestUtils.sha256Hex(s + salt); /* 加盐后加密处理 */
    }

    /**
     * <pre>
     * 计算盐值
     * </pre>
     *
     * @param s 密码
     * @return  计算的盐值
     */
    private String calculateSalt(String s) {
        byte[] bs = s.getBytes();
        long salt = 0;
        for (byte b : bs) {
            salt += b;
        }

        return Long.toString(salt);
    }

}
