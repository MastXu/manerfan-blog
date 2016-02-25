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
package com.manerfan.blog.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.h2.tools.Server;

import com.manerfan.common.utils.logger.Logger;

/**
 * <pre>随系统启停H2DB</pre>
 *
 * @author ManerFan 2016年2月25日
 */
public class H2DBServerListener implements ServletContextListener {

    /*private Server tcpServer;*/

    private Server webServer;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            /*Logger.LOGGER.info("Start H2DB TCP Server!");
            tcpServer = Server.createTcpServer("-tcpPort", "9092").start();*/

            Logger.LOGGER.info("Start H2DB Web Server!");
            webServer = Server.createWebServer("-webPort", "8082").start();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.LOGGER.error("H2DB Error!", e);
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        /*tcpServer.stop();*/
        /*Logger.LOGGER.warn("Stop H2DB TCP Server!");*/

        webServer.stop();
        Logger.LOGGER.warn("Stop H2DB Web Server!");
    }

}
