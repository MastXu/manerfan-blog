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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.manerfan.common.utils.ShutdownHook;
import com.manerfan.spring.autoconfigure.Http2ConnectionFactory;

/**
 * <pre>
 * jetty 主入口
 * 命令解析采用 apache commons-cli
 * </pre>
 *
 * @author ManerFan 2016年2月24日
 */
public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    private static Options opts;

    private static CommandLineParser parser = new DefaultParser();

    private static HelpFormatter hf = new HelpFormatter();

    private static String cmdstr = "mblog";

    private final static String PARAMNAME = "D";
    private final static String PORT = "port";
    private final static String CONTENTPATH = "contentpath";
    private final static String WEBCONTENT = "webapp";

    public static void main(String[] args) {
        initCLI();
        handleArgs(args);
    }

    /**
     * <pre>初始化command line interface</pre>
     */
    private static void initCLI() {
        opts = new Options();

        /* 帮助 */
        opts.addOption(Option.builder("h").longOpt("help").hasArg(false).desc("显示帮助信息")
                .required(false).build());

        /* 调试模式 */
        opts.addOption(Option.builder("debug").hasArg(false).desc("调试模式").required(false).build());

        /* 参数 */
        opts.addOption(Option.builder(PARAMNAME).hasArgs().argName("启动参数")
                .desc("port 发布端口 contentpath 发布路径").required(false).build());

        /* context path */
        /*opts.addOption(Option.builder("cp").longOpt("contextpath").hasArg().type(String.class)
                .argName("ContextPath[e.g. /mblog]").desc("Web Context Path").required().build());*/

        hf.setWidth(110);
    }

    /**
     * <pre>处理命令参数</pre>
     * @param args  命令参数
     */
    private static void handleArgs(String[] args) {
        try {
            CommandLine commandLine = parser.parse(opts, args);

            if (commandLine.hasOption('h')) {
                // 打印使用帮助
                hf.printHelp(cmdstr, opts, true);
            }

            if (commandLine.hasOption("debug")) {
                System.err.println("You're using DEBUG MODE; click in this console and "
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

            Properties params = commandLine.getOptionProperties(PARAMNAME);

            // 解析发布端口
            int port = 80;
            if (params.containsKey(PORT)) {
                try {
                    port = Integer.parseInt(params.getProperty(PORT));
                    LOGGER.info("设置发布端口为 {}", port);
                } catch (NumberFormatException e) {
                    port = 80;
                    LOGGER.error("{} 不是一个有效的数字，使用默认端口80", params.getProperty(PORT));
                }
            } else {
                LOGGER.warn("未指定发布端口，使用默认值80");
            }

            // 解析发布路径
            String contextpath = "/";
            if (params.containsKey(CONTENTPATH)) {
                contextpath = params.getProperty(CONTENTPATH);
                LOGGER.info("设置发布路径为 {}", contextpath);
            } else {
                LOGGER.warn("未指定发布路径，使用默认值/");
            }

            // 使用-Xbootclasspath/a将WEBCONTENT目录加入classpath，或使用java service wrapper之类的工具
            // 设置webcontent
            File content = ResourceUtils.getFile("classpath:" + WEBCONTENT);
            if (null == content || !content.exists()) {
                LOGGER.error("{} 缺失", WEBCONTENT);
                System.err.println(WEBCONTENT + "缺失");
                System.exit(1);
            }

            String resourceBase = content.getAbsolutePath();
            String descriptor = new File(resourceBase, "WEB-INF/web.xml").getAbsolutePath();

            /* 初始化webserver */
            Server server = new Server();

            // 使用HTTP/2.0
            ServerConnector connector = new ServerConnector(server,
                    /*new SslConnectionFactory(HttpVersion.HTTP_2.asString()),*/
                    new Http2ConnectionFactory());
            connector.setPort(port);

            server.setConnectors(new Connector[] { connector });

            /* 配置webcontent */
            WebAppContext context = new WebAppContext();
            context.setContextPath(contextpath);
            context.setDescriptor(descriptor);
            context.setResourceBase(resourceBase);
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            context.setConfigurationDiscovered(true);
            context.setParentLoaderPriority(true);

            server.setHandler(context);

            /* 
             * 向JVM注册一个HOOK
             * listener由server管理清理
             * spring bean由spring管理清理
             * 其他由shutdownhook管理清理
             */
            ShutdownHook.hook(() -> {
                try {
                    if (server.isStarted()) {
                        server.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("=============  MBLOG is SHUTDOWN =============");
            });

            /* 启动web & spring */
            server.start(); // 阻塞
        } catch (ParseException e) {
            hf.printHelp(cmdstr, opts, true);
        } catch (Exception e) {
            LOGGER.error("Start Server Error.", e);
            System.exit(1);
        }
    }

}
