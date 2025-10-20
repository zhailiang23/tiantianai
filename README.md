# TianTianAI

一个全栈 AI 应用项目，采用前后端分离架构。

## 技术栈

### 后端
- Java 17
- Spring Boot 3.4.1
- Spring Security 6.4.2 (JWT 认证)
- Spring Data JPA
- MySQL 8.0+
- Maven

### 前端
- Vue 3
- TypeScript
- Vite
- pnpm monorepo
- Vue Vben Admin 5.x
- Ant Design Vue

## 快速开始

### 前置要求

- Java 17+
- Node.js 20.10.0+
- pnpm 9.12.0+
- Docker (用于运行 MySQL)

### 启动数据库

```bash
docker run -d --name tiantianai-mysql \
  -e MYSQL_ROOT_PASSWORD=yourpassword \
  -e MYSQL_DATABASE=tiantianai \
  -p 3307:3306 \
  mysql:8.0 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

### 启动后端

```bash
cd backend
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

后端服务将在 http://localhost:8080 启动。

### 启动前端

```bash
cd frontend
pnpm install
pnpm dev:antd
```

前端服务将在 http://localhost:5666 启动。

## 测试账号

- `vben / 123456` - admin + user 角色
- `admin / 123456` - admin 角色
- `jack / 123456` - user 角色

## 项目文档

详细的项目规范和约定请查看：
- [OpenSpec 项目规范](openspec/project.md)
- [Claude Code 指南](CLAUDE.md)

## 开发规范

### Git 提交规范

遵循 Conventional Commits：
- `feat`: 新功能
- `fix`: Bug 修复
- `style`: 代码格式调整
- `perf`: 性能优化
- `refactor`: 代码重构
- `test`: 测试相关
- `docs`: 文档更新
- `chore`: 依赖更新/配置修改

### 后端包结构

```
com.tiantianai
├── shared/          # 共享组件
│   ├── config/      # 配置类
│   ├── util/        # 工具类
│   ├── security/    # 安全组件
│   ├── exception/   # 异常处理
│   └── common/      # 通用类
├── auth/            # 认证模块
├── user/            # 用户模块
└── ...              # 其他业务模块
```

每个业务模块包含：
- controller - REST API 控制器
- service - 业务逻辑
- repository - 数据访问
- model - 实体类
- dto - 数据传输对象

## API 文档

### 认证接口

- `POST /auth/login` - 用户登录
- `POST /auth/logout` - 用户登出
- `POST /auth/refresh` - 刷新 Token
- `GET /auth/codes` - 获取权限码

### 用户接口

- `GET /user/info` - 获取用户信息

除登录和刷新接口外，其他接口需要在请求头携带：
```
Authorization: Bearer {token}
```

## 许可证

MIT
