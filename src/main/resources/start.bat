@echo off
set cdir=%~dp0

set RUNJAVA="D:\Program Files\Tools\java8\bin\java.exe"
REM -Xms 最小堆空间
REM -Xmx 最大堆空间
REM -XX:MaxNewSize 最大新生代空间
REM -XX:MaxMetaspaceSize 最大元空间(Java8 使用元空间取代持久代)
set JAVA_OPTS=-server -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Xms64M -Xmx512M -XX:MaxNewSize=256M -XX:MaxMetaspaceSize=128M

set CLASSPATH=.\resources
set WEB_CONTENT=webapp

set CONTEXT_PATH=/
set PORT=80

call %RUNJAVA% -Xbootclasspath/a:%CLASSPATH% %JAVA_OPTS% -jar mblog.jar -cp %CONTEXT_PATH% -wc %WEB_CONTENT% -p %PORT%