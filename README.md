# 企业知识库管理系统

企业级知识库管理系统，基于 Spring Boot 3 + Vue 3 构建。

## 技术栈

### 后端
- **Java 22** + Spring Boot 3.2.5
- **MyBatis Plus 3.5.5** - ORM 框架
- **Spring Security 6** + **JWT** - 认证鉴权
- **MySQL 8.0** - 数据库
- **Redis 7.x** - 缓存
- **RabbitMQ 3.x** - 消息队列
- **Knife4j** - API 文档

### 前端
- **Vue 3.4** + TypeScript
- **Element Plus 2.5** - UI 组件库
- **Pinia** - 状态管理
- **Vue Router 4** - 路由
- **Axios** - HTTP 客户端
- **WangEditor** - 富文本编辑器
- **ECharts** - 图表

## 项目结构

```
enterprise-knowledge-base/
├── knowledge-base-backend/          # 后端项目
│   ├── src/main/java/com/knowledge/base/
│   │   ├── config/                  # 配置类
│   │   ├── controller/              # API 控制器
│   │   ├── service/                 # 业务逻辑
│   │   ├── mapper/                  # MyBatis Plus Mapper
│   │   ├── entity/                  # 实体类
│   │   ├── dto/                     # 数据传输对象
│   │   ├── vo/                      # 视图对象
│   │   ├── security/                # JWT + Spring Security
│   │   ├── common/                  # 通用工具类
│   │   ├── exception/               # 异常处理
│   │   └── event/                   # 事件处理
│   └── src/main/resources/          # 配置文件
│
├── knowledge-base-admin/            # 前端项目
│   ├── src/
│   │   ├── api/                     # API 接口
│   │   ├── views/                   # 页面组件
│   │   ├── components/              # 公共组件
│   │   ├── router/                  # 路由配置
│   │   ├── stores/                  # Pinia 状态管理
│   │   └── utils/                   # 工具类
│   └── public/                      # 静态资源
│
├── docker/                          # Docker 配置
│   ├── db/                          # 数据库脚本
│   └── nginx.conf                   # Nginx 配置
│
└── docker-compose.yml               # Docker Compose
```

## 功能模块

### 用户管理
- ✅ 用户 CRUD
- ✅ 角色分配
- ✅ 权限控制（RBAC）

### 内容管理
- ✅ 知识分类（树形结构）
- ✅ 文章管理（CRUD + 版本控制）
- ✅ 标签管理
- ✅ 附件管理（图片压缩/缩略图）

### 审核流程
- ✅ 草稿 → 待审核 → 已通过/已拒绝 → 已发布
- ✅ 审核记录追踪

### 系统功能
- ✅ JWT Token 认证
- ✅ Redis 缓存
- ✅ RabbitMQ 消息队列
- ✅ API 文档（Knife4j）

## 快速开始

### 环境要求
- Java 22+
- Node.js 18+
- MySQL 8.0+
- Redis 7.x+
- RabbitMQ 3.x+

### 本地开发

#### 1. 启动基础设施（Docker）
```bash
docker-compose up -d mysql redis rabbitmq
```

#### 2. 初始化数据库
```bash
# 执行数据库脚本
mysql -u root -proot123 knowledge_base < docker/db/init.sql
```

#### 3. 启动后端
```bash
cd knowledge-base-backend

# 使用 Java 22
export JAVA_HOME=D:\software\env\jdk22\jdk-22
export PATH=$JAVA_HOME/bin:$PATH

# 编译并运行
./mvnw spring-boot:run
```

#### 4. 启动前端
```bash
cd knowledge-base-admin

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

#### 5. 访问系统
- 前端: http://localhost:3000
- 后端 API: http://localhost:8080/api
- API 文档: http://localhost:8080/doc.html
- RabbitMQ 管理: http://localhost:15672 (guest/guest)

### 默认账号
- 用户名: admin
- 密码: 123456

### Docker 部署

#### 开发环境
```bash
docker-compose up -d
```

#### 生产环境
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## API 文档

启动项目后访问: http://localhost:8080/doc.html

## License

MIT License
