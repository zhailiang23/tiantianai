# Project Context

## Purpose

TianTianAI 是一个全栈 AI 应用项目，采用前后端分离架构，提供基于 Vue 3 的现代化 Web 界面和基于 Spring Boot 的企业级后端服务。

## Tech Stack

### 后端技术栈
- **Java 17** - 编程语言
- **Spring Boot 3.4.1** - 核心框架
  - Spring Security 6.4.2 - 安全认证
  - Spring Data JPA - 数据持久化
  - Spring Web - REST API
- **MySQL 8.0+** - 关系型数据库（UTF-8MB4 编码）
- **JJWT 0.12.6** - JWT 令牌生成和验证
- **Lombok** - 简化 Java 代码
- **Maven** - 依赖管理和构建工具

### 前端技术栈
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **TypeScript** - 类型安全
- **pnpm monorepo** - 包管理（workspace + catalog）
- **Turbo** - Monorepo 构建工具
- **Vue Vben Admin 5.x** - 管理后台基础框架
- **Ant Design Vue** - UI 组件库
- **ESLint + Prettier + Stylelint** - 代码质量工具

### 中间件和工具
- **Docker** - 容器化部署（MySQL, Redis, ES 等中间件）
- **Git** - 版本控制
- **GitHub** - 代码托管和 Issue 管理
- **uv** - Python 环境管理

## Project Conventions

### Code Style

#### 后端（Java/Spring Boot）
- 使用 **Lombok** 注解减少样板代码：`@Data`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`
- 遵循 **DDD (Domain-Driven Design)** 风格的包组织结构
- 包命名规范：
  - 业务模块：`com.tiantianai.{module}.*` (如 `com.tiantianai.auth.*`, `com.tiantianai.user.*`)
  - 共享组件：`com.tiantianai.shared.*` (包括 config, util, security, exception, common)
- 类命名规范：
  - Controller: `{Entity}Controller.java`
  - Service: `{Entity}Service.java`
  - Repository: `{Entity}Repository.java`
  - DTO: `{Operation}Request.java`, `{Operation}Response.java`
  - Entity: `{Entity}.java`
- **Maven 编译前必须先执行 clean**: `mvn clean compile`

#### 前端（Vue 3/TypeScript）
- 严格的 **TypeScript** 类型检查，禁用 any 类型
- 使用 **ESLint** + **Prettier** 统一代码风格
- 使用 **pnpm workspace** 管理 monorepo 结构
- 目录命名：
  - 应用：`apps/{app-name}/`
  - 共享包：`packages/{package-name}/`
  - 内部工具：`internal/{tool-name}/`
- 组件命名：PascalCase
- 文件命名：kebab-case (`.ts`, `.vue`)

### Architecture Patterns

#### 后端架构
- **业务模块化**: 按业务领域（auth, user 等）组织代码，每个模块包含 controller, service, dto, model, repository
- **统一响应格式**: 所有 REST API 返回 `Result<T>` 包装，包含 `code`, `message`, `data` 字段
- **全局异常处理**: `@RestControllerAdvice` 统一捕获并转换异常为标准 `Result<T>` 响应
- **无状态 JWT 认证**:
  - `JwtAuthenticationFilter` 拦截请求，从 `Authorization: Bearer {token}` 头提取并验证 JWT
  - JWT payload 包含 `userId`, `username`, `roles`
  - Controller 通过 `Authentication.getName()` 获取 `userId`
- **CORS 配置**: SecurityConfig 中配置允许的源（5173, 5666, 3000, 8080）和方法（GET, POST, PUT, DELETE, OPTIONS）
- **Spring Data JPA**: 使用接口方法命名约定（如 `findByUsername`）自动生成查询

#### 前端架构
- **Monorepo 结构**:
  - `apps/web-antd/` - 主应用
  - `packages/*` - 共享包（utils, stores, types, constants, locales, styles, icons, preferences）
  - `internal/*` - 内部工具和配置
- **类型安全**: 全项目 TypeScript，共享 types 包
- **状态管理**: Pinia stores
- **国际化**: i18n 内置支持，locales 包管理翻译
- **主题系统**: Tailwind CSS 多主题
- **权限系统**: 基于路由的动态权限
- **API 代理**: Vite proxy 转发 `/api/*` 到后端 `http://localhost:8080`

### Testing Strategy

#### 后端测试
- **单元测试**: JUnit 5
- **运行测试**: `mvn test`
- **测试数据**: DataInitializer 初始化测试用户（vben/123456, admin/123456, jack/123456）

#### 前端测试
- **单元测试**: Vitest
- **运行测试**: `pnpm test:unit`
- **类型检查**: `pnpm check:type`
- **完整检查**: `pnpm check` (循环依赖、类型、拼写)

### Git Workflow

#### 分支策略
- `main` - 主分支
- `master` - 当前开发分支
- 功能分支 - 按需创建功能分支进行开发

#### 提交规范
遵循 **Conventional Commits** 规范：
- `feat`: 新功能
- `fix`: Bug 修复
- `style`: 代码格式调整
- `perf`: 性能优化
- `refactor`: 代码重构
- `test`: 测试相关
- `docs`: 文档更新
- `chore`: 依赖更新/配置修改

#### 提交注意事项
- **不包含 AI 工具信息**（如 "Generated with Claude Code"）
- **执行 git 命令时忽略代理**: 使用 `-c http.proxy="" -c https.proxy=""`
- **等待用户明确要求后再推送代码**

## Domain Context

### 认证与授权
- **用户角色**: `admin`, `user` 等，存储在 `user_roles` 表
- **JWT Token**:
  - 有效期：24小时（可配置）
  - 密钥：配置在 `application.yml` 的 `jwt.secret`
  - 包含信息：userId, username, roles
- **登录流程**:
  1. POST `/auth/login` 验证用户名密码
  2. 返回 `accessToken`
  3. 前端在请求头携带 `Authorization: Bearer {token}`
  4. 后端 `JwtAuthenticationFilter` 验证并设置 `SecurityContext`

### 用户管理
- **测试用户**:
  - `vben/123456` - admin + user 角色
  - `admin/123456` - admin 角色
  - `jack/123456` - user 角色
- **密码加密**: BCrypt

## Important Constraints

### 技术约束
- **Node 版本**: >=20.10.0
- **pnpm 版本**: >=9.12.0
- **Java 版本**: 17
- **MySQL 版本**: 8.0+, UTF-8MB4 编码
- **浏览器支持**: Chrome 80+, Edge, Firefox, Safari 最新两个版本（不支持 IE）

### 开发约束
- **中间件使用 Docker 部署**: 所有中间件（MySQL, Redis, ES 等）必须通过 Docker 运行
- **Python 环境使用 uv 管理**: 确保所有 Python 脚本在 uv 虚拟环境中执行
- **Maven 编译前必须 clean**: `mvn clean compile`
- **使用 zsh 作为终端**: 不要用 bash
- **编码前使用 context7 工具**: 搜索相关库的最新文档
- **用中文与用户沟通**

### 安全约束
- **CORS 配置**: 后端必须配置允许的前端端口（5173, 5666, 3000, 8080）
- **OPTIONS 请求许可**: 必须 permitAll() 以支持 CORS 预检
- **敏感信息不提交**: 密钥、密码等配置在 `.env` 文件，不入版本控制

## External Dependencies

### 数据库
- **MySQL 8.0** (Docker)
  - 端口: 3307
  - 数据库名: `tiantianai`
  - 字符集: utf8mb4_unicode_ci
  - Docker 命令:
    ```bash
    docker run -d --name tiantianai-mysql \
      -e MYSQL_ROOT_PASSWORD=yourpassword \
      -e MYSQL_DATABASE=tiantianai \
      -p 3307:3306 \
      mysql:8.0 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
    ```

### 未来计划中的中间件
- **Redis** - 缓存和会话存储 (Docker)
- **Elasticsearch** - 全文搜索 (Docker)

### 外部服务
- **GitHub**: 代码托管和协作
  - 仓库地址: https://github.com/zhailiang23/tiantianai.git

### 文档资源
- **Vue Vben Admin**: https://doc.vben.pro/
- **Spring Boot**: 通过 context7 工具查询最新文档

## Development Ports

- **后端服务**: 8080
- **前端开发服务器**: 5666 (配置在 `.env.development`)
- **MySQL**: 3307 (映射到容器内 3306)
- **备用前端端口**: 5173, 3000

## API Endpoints

### 认证相关
- `POST /auth/login` - 用户登录
- `POST /auth/logout` - 用户登出
- `POST /auth/refresh` - 刷新 Token
- `GET /auth/codes` - 获取权限码列表

### 用户相关
- `GET /user/info` - 获取用户信息

所有 API 需要在请求头携带 `Authorization: Bearer {token}`，除了 `/auth/login` 和 `/auth/refresh`。
