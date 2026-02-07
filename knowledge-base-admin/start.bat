@echo off
REM 企业知识库系统 - 前端启动脚本

echo 正在安装依赖...
cd /d "%~dp0"
call npm install

echo.
echo 依赖安装完成，正在启动开发服务器...
echo 前端访问地址: http://localhost:3000
echo 后端API地址: http://localhost:8080/api
echo.
call npm run dev
