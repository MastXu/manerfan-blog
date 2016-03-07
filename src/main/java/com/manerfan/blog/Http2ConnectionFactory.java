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
package com.manerfan.blog;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.AbstractConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.util.annotation.Name;

/**
 * <pre>Http/2.0</pre>
 *
 * @author ManerFan 2016年3月7日
 */
public class Http2ConnectionFactory extends AbstractConnectionFactory
        implements HttpConfiguration.ConnectionFactory {
    private final HttpConfiguration _config;

    public Http2ConnectionFactory() {
        this(new HttpConfiguration());
    }

    public Http2ConnectionFactory(@Name("config") HttpConfiguration config) {
        super(HttpVersion.HTTP_2.asString());
        _config = config;
        if (config == null)
            throw new IllegalArgumentException("Null HttpConfiguration");
        addBean(_config);
    }

    @Override
    public HttpConfiguration getHttpConfiguration() {
        return _config;
    }

    @Override
    public Connection newConnection(Connector connector, EndPoint endPoint) {
        return configure(new HttpConnection(_config, connector, endPoint), connector, endPoint);
    }
}