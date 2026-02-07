@echo off
REM 企业知识库系统 - 后端启动脚本
REM 请确保 D:/software/env/jdk22/jdk-22 目录存在

SET JAVA_HOME=D:\software\env\jdk22\jdk-22
SET PATH=%JAVA_HOME%\bin;%PATH%

echo 使用 Java 版本:
java -version

echo.
echo 正在编译项目...
cd /d "%~dp0"
call mvn clean package -DskipTests -f pom.xml

echo.
echo 编译完成，正在启动应用...
java -jar target\knowledge-base-backend-1.0.0.jar --spring.profiles.active=dev
