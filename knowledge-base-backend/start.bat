@echo off
REM 企业知识库系统 - 后端启动脚本
REM 请确保 D:/software/env/jdk22/jdk-22 目录存在

SET JAVA_HOME=D:\software\env\jdk22\jdk-22
SET PATH=%JAVA_HOME%\bin;%PATH%

echo 使用 Java 版本:
java -version

echo.
echo 正在打包项目...
cd /d "%~dp0"
call mvn clean package -DskipTests -Dmaven.test.skip=true -f pom.xml

echo.
echo 打包完成，正在启动应用...
java -jar target\knowledge-base-backend-1.0.0.jar --spring.profiles.active=dev
