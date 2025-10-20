# TianTianAI Backend

基于 Spring Boot 3.4.1 + Spring Data JPA 的后端项目

## 技术栈

- Java 17
- Spring Boot 3.4.1
- Spring Data JPA
- MySQL
- Lombok
- Maven

## 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tiantianai/
│   │   │           ├── TiantianaiApplication.java  # 主应用类
│   │   │           ├── common/                     # 通用类
│   │   │           ├── config/                     # 配置类
│   │   │           ├── controller/                 # 控制器
│   │   │           ├── dto/                        # 数据传输对象
│   │   │           ├── exception/                  # 异常处理
│   │   │           ├── model/                      # 实体类
│   │   │           ├── repository/                 # 数据访问层
│   │   │           └── service/                    # 业务逻辑层
│   │   └── resources/
│   │       ├── application.yml                     # 主配置文件
│   │       ├── application-dev.yml                 # 开发环境配置
│   │       └── application-prod.yml                # 生产环境配置
│   └── test/                                       # 测试代码
└── pom.xml                                         # Maven 配置
```

## 快速开始

### 前置条件

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 配置数据库

1. 创建数据库：
```sql
CREATE DATABASE tiantianai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `src/main/resources/application-dev.yml` 中的数据库连接信息

### 运行项目

```bash
# 开发环境
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 或者
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### 测试接口

```bash
curl http://localhost:8080/api/health
```

## API 文档

启动项目后访问：
- 健康检查：http://localhost:8080/api/health

## 开发规范

- 所有 API 返回统一使用 `Result<T>` 包装
- 使用 Lombok 简化代码
- 遵循 RESTful API 设计规范
- 统一异常处理

## License

Copyright © 2025 TianTianAI
