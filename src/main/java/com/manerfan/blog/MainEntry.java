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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * <pre>
 * jetty 主入口
 * 命令解析采用 apache commons-cli
 * 
 * 参考
 * http://my.oschina.net/cloudcoder/blog/363793
 * http://blog.csdn.net/faye0412/article/details/2949753
 * </pre>
 *
 * @author ManerFan 2016年2月24日
 */
public class MainEntry {

    private static Options opts;

    private static CommandLineParser parser = new DefaultParser();

    private static HelpFormatter hf = new HelpFormatter();

    private static String formatstr = "mblog";

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

        /* 端口 */
        opts.addOption(Option.builder("p").longOpt("port").hasArg().type(int.class)
                .argName("HttpPort[e.g. 80]").desc("web服务端口").required().build());

        /* context path */
        opts.addOption(Option.builder("cp").longOpt("contextpath").hasArg().type(String.class)
                .argName("ContextPath[e.g. /mblog]").desc("Web Context Path").required().build());

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
                hf.printHelp(formatstr, opts, true);
            }

            int port = (int) commandLine.getParsedOptionValue("p");

        } catch (ParseException e) {
            hf.printHelp(formatstr, opts, true);
        }
    }

}
