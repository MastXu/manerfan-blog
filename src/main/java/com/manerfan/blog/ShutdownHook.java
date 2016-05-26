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

import java.io.IOException;

import org.eclipse.jetty.server.Server;

/**
 * <pre>
 * 使用Runtime.getRuntime().addShutdownHook向JVM注册一个HOOK
 * 
 * 以下情况触发
 * 1) 程序正常退出
 * 2) 使用System.exit()
 * 3) 终端使用Ctrl+C触发的中断
 * 4) 系统关闭
 * 5) 使用Kill pid命令杀掉进程
 * 
 * 注: kill -9 pid 不会触发
 * </pre>
 *
 * @author ManerFan 2016年5月26日
 */
public class ShutdownHook extends Thread {

    private Server server;

    public ShutdownHook(Server server) {
        this.server = server;

        /**
         * Run → Run Configurations → Java Application → Environment → New [RUNNING_IN_ECLIPSE=true]
         * 如上配置，如果在eclipse中调试运行，则在控制台中点击回车，正常结束程序
         */
        if (Boolean.parseBoolean(System.getenv("RUNNING_IN_ECLIPSE"))) {
            System.out.println("You're using Eclipse; click in this console and "
                    + "press ENTER to call System.exit() and run the shutdown routine.");
            new Thread() {
                @Override
                public void run() {
                    try {
                        while ('\n' != System.in.read()) {
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }.start();
        }
    }

    @Override
    public void run() {
        try {
            if (server.isStarted()) {
                server.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("=============  MBLOG is SHUTDOWN =============");
    }

}
