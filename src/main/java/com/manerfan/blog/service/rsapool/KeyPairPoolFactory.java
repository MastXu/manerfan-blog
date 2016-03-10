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
package com.manerfan.blog.service.rsapool;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * <pre>RSA KeyPair池工厂类</pre>
 *
 * @author ManerFan 2016年3月9日
 */
public class KeyPairPoolFactory extends BasePooledObjectFactory<KeyPair> {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_LEN = 1024;

    @Override
    public KeyPair create() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM,
                SecurityBCProvider.getProvider());
        keyPairGen.initialize(KEY_LEN);
        return keyPairGen.generateKeyPair();
    }

    @Override
    public PooledObject<KeyPair> wrap(KeyPair keyPair) {
        return new DefaultPooledObject<KeyPair>(keyPair);
    }
}
