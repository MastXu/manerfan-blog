@echo off
set cdir=%~dp0

set RUNJAVA="D:\Program Files\Tools\java8\bin\java.exe"
set JAVA_OPTS=-server -Xms64M -Xmx512M -XX:MaxNewSize=256M

set CLASSPATH=.\resources
set WEB_CONTENT=webapp

set CONTEXT_PATH=/
set PORT=80

call %RUNJAVA% -Xbootclasspath/a:%CLASSPATH% %JAVA_OPTS% -jar mblog.jar -cp %CONTEXT_PATH% -wc %WEB_CONTENT% -p %PORT%