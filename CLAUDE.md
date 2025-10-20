# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

TianTianAI 是一个全栈 AI 应用项目,采用前后端分离架构:
- **后端**: Spring Boot 3.4.1 + Spring Data JPA + MySQL
- **前端**: Vue 3 + Vite + TypeScript (基于 Vue Vben Admin 5.x)

## 架构说明

### 单体仓库结构
```
tiantianai/
├── backend/           # Spring Boot 后端服务
├── frontend/          # Vue 3 前端 (pnpm monorepo)
├── .claude/           # Claude Code PM 项目管理系统
│   ├── commands/      # 项目管理命令
│   ├── epics/         # Epic 工作空间 (已在 .gitignore)
│   ├── prds/          # 产品需求文档
│   └── context/       # 项目上下文
└── ccpm/             # CCPM 脚本
```

### 后端架构 (backend/)
- **技术栈**: Java 17, Spring Boot 3.4.1, Spring Data JPA, MySQL 8.0+, Lombok
- **包结构**:
  - `com.tiantianai.common.*` - 通用工具类和响应包装 (Result<T>)
  - `com.tiantianai.config.*` - Spring 配置类
  - `com.tiantianai.controller.*` - REST API 控制器
  - `com.tiantianai.dto.*` - 数据传输对象
  - `com.tiantianai.exception.*` - 全局异常处理 (GlobalExceptionHandler)
  - `com.tiantianai.model.*` - JPA 实体类
  - `com.tiantianai.repository.*` - Spring Data JPA 仓储接口
  - `com.tiantianai.service.*` - 业务逻辑层
- **配置文件**: `application.yml` (主配置), `application-dev.yml` (开发), `application-prod.yml` (生产)
- **API 规范**: 所有 API 返回统一使用 `Result<T>` 包装,遵循 RESTful 设计

### 前端架构 (frontend/)
- **技术栈**: Vue 3, Vite, TypeScript, pnpm monorepo, Turbo
- **Monorepo 结构**:
  - `apps/web-antd/` - 主应用 (基于 Ant Design Vue)
  - `packages/*` - 共享包 (utils, stores, types, constants, locales, styles, icons, preferences)
  - `internal/*` - 内部工具和配置 (tailwind-config, tsconfig, vite-config, lint-configs)
  - `scripts/*` - 构建和开发脚本
  - `docs/` - 文档站点
- **包管理**: 使用 pnpm workspace 和 catalog 管理依赖
- **构建工具**: Turbo 进行并行构建和缓存
- **代码规范**: ESLint, Prettier, Stylelint, Commitlint

## 常用命令

### 后端开发命令
```bash
cd backend

# 清理并编译 (重要: 编译前必须先 clean)
mvn clean compile

# 运行测试
mvn test

# 运行应用 (开发环境)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 打包
mvn clean package

# 运行打包后的应用
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### 前端开发命令
```bash
cd frontend

# 安装依赖 (首次或更新依赖时)
pnpm install

# 开发模式运行主应用
pnpm dev:antd

# 或使用交互式选择器
pnpm dev

# 构建主应用
pnpm build:antd

# 构建所有应用
pnpm build

# 代码检查
pnpm lint              # 运行 linter
pnpm format            # 格式化代码
pnpm check             # 完整检查 (循环依赖、类型、拼写)
pnpm check:type        # 仅类型检查

# 运行单元测试
pnpm test:unit

# 预览构建结果
pnpm preview

# 清理所有构建产物
pnpm clean
```

### Docker 中间件管理
项目依赖的中间件 (MySQL, Redis, ES 等) 使用 Docker 部署:
```bash
# MySQL (后端数据库)
docker run -d --name tiantianai-mysql \
  -e MYSQL_ROOT_PASSWORD=yourpassword \
  -e MYSQL_DATABASE=tiantianai \
  -p 3306:3306 \
  mysql:8.0 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

# 连接 MySQL 验证
docker exec -it tiantianai-mysql mysql -uroot -p
```

### 项目管理命令 (CCPM)
使用 Claude Code PM 系统管理开发流程:
```bash
# 查看帮助
/pm:help

# 创建新功能 PRD
/pm:prd-new feature-name

# 将 PRD 转换为技术实现方案
/pm:prd-parse feature-name

# 分解并同步到 GitHub
/pm:epic-oneshot feature-name

# 开始实现任务
/pm:issue-start <issue-number>

# 查看项目状态
/pm:status

# 获取下一个优先任务
/pm:next
```

## 开发工作流

### 新功能开发流程
1. **创建 PRD**: `/pm:prd-new feature-name` - 头脑风暴并创建产品需求文档
2. **技术规划**: `/pm:prd-parse feature-name` - 将 PRD 转换为技术实现方案
3. **任务分解**: `/pm:epic-decompose feature-name` 或使用 `/pm:epic-oneshot` 一步完成
4. **同步 GitHub**: 任务自动创建为 GitHub Issues
5. **并行开发**: 使用 `/pm:issue-start <number>` 启动专门的 agent 并行处理多个任务
6. **进度同步**: `/pm:issue-sync <number>` 更新进度到 GitHub

### Git 提交规范
遵循 Conventional Commits 规范:
- `feat`: 新功能
- `fix`: Bug 修复
- `style`: 代码格式调整
- `perf`: 性能优化
- `refactor`: 代码重构
- `test`: 测试相关
- `docs`: 文档更新
- `chore`: 依赖更新/配置修改

注意: Git 提交时不包含 AI 工具信息,等待用户明确要求后再推送

## 关键技术决策

### 后端
- **统一响应格式**: 所有 API 使用 `Result<T>` 封装,包含 code, message, data 字段
- **全局异常处理**: `GlobalExceptionHandler` 统一捕获并转换异常为标准响应
- **Lombok 简化代码**: 使用 @Data, @Builder 等注解减少样板代码
- **JPA 命名策略**: 遵循 Spring Data JPA 命名约定

### 前端
- **Monorepo 管理**: 使用 pnpm workspace 实现代码共享和依赖管理
- **类型安全**: 全项目 TypeScript 强类型,使用共享 types 包
- **国际化**: 内置 i18n 支持,locales 包管理翻译
- **主题系统**: 支持多主题切换,基于 Tailwind CSS
- **权限系统**: 基于路由的动态权限生成方案

### 开发环境
- **后端端口**: 8080
- **前端端口**: 5173 (dev server)
- **数据库**: MySQL 8.0+, UTF-8MB4 编码
- **Node 版本**: >=20.10.0
- **pnpm 版本**: >=9.12.0
- **Java 版本**: 17

## 特殊注意事项

### 用户偏好设置 (来自 ~/.claude/CLAUDE.md)
- 使用中文与用户沟通
- Python 环境使用 uv 管理
- 执行 git 命令时忽略代理设置
- Maven 编译前必须先执行 clean
- 中间件使用 Docker 部署
- 编码前使用 context7 工具搜索相关文档
- 不主动提交推送代码,等待用户指令

### Context7 集成
在实现功能前,使用 context7 工具查询相关库的最新文档:
```bash
# 示例: 查询 Spring Boot 文档
mcp__context7__resolve-library-id "spring boot"
mcp__context7__get-library-docs "/spring-projects/spring-boot"
```

### 并行开发优化
- CCPM 支持多个 agent 并行工作在同一个 epic 的不同任务上
- 使用 Git worktrees 实现隔离的并行开发环境
- 主对话线程保持简洁,实现细节由子 agent 处理
- 任务标记 `parallel: true` 可启用无冲突并发开发

## 浏览器支持

前端支持现代浏览器 (Chrome 80+, Edge, Firefox, Safari 最新两个版本),不支持 IE

## 资源链接

- **GitHub 仓库**: https://github.com/zhailiang23/tiantianai.git
- **Vue Vben Admin 文档**: https://doc.vben.pro/
- **CCPM 文档**: 见项目根目录 README.md
- 后端包结构说明: com.tiantianai下面应该是按业务组织的业务模块,如 user, auth. 业务包下面是 model, controller, service 包. 多个业务公用的东西放在 com.tiantianai.shared 
包里,包括 config, util等