# Library 图书馆管理系统

基于 Spring Boot 的图书馆管理系统。

## 🚀 技术栈

- **后端框架**: Spring Boot 3.5.13
- **数据库**: MySQL
- **ORM**: MyBatis 3.0.5
- **缓存**: Redis
- **认证**: JWT (io.jsonwebtoken 0.12.5)
- **工具类**: Hutool 5.8.16
- **JDK**: 17
- **构建工具**: Maven

## 📋 功能模块

### 1. 用户管理
- 用户注册
- 用户登录（JWT 认证）
- 用户信息增删改查
- Redis 缓存优化

### 2. 图书管理
- 图书列表查询（支持关键词搜索）
- 图书详情查询
- 图书信息新增
- 图书信息修改
- 图书信息删除

### 3. 借阅管理
- 借书（事务控制）
- 还书
- 借阅记录查询
- 借阅记录删除

## 🛠️ 快速开始

### 环境要求
- JDK 17+
- MySQL 5.7+
- Redis 3.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone git@github.com:wuhuwuhuhahahaha/Library.git
cd Library
```

2. **配置数据库**

编辑 `src/main/resources/application.properties`：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sky
spring.datasource.username=root
spring.datasource.password=your_password
```

3. **配置 Redis**

编辑 `src/main/resources/application.properties`：
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
```

4. **导入数据库表结构**

执行 `src/main/resources/borrow_table.sql` 创建数据表

5. **启动项目**
```bash
mvn clean install
mvn spring-boot:run
```

项目启动后访问：http://localhost:8080

## 📖 API 接口

### 认证接口
- `POST /api/login` - 用户登录
- `POST /api/success` - 用户注册

### 图书接口
- `GET /api/book` - 获取图书列表
- `GET /api/book/{id}` - 获取图书详情
- `POST /api/book` - 新增图书
- `PUT /api/book` - 修改图书
- `DELETE /api/book/{id}` - 删除图书

### 借阅接口
- `POST /api/borrow` - 借书（需要 JWT Token）
- `PUT /api/borrow/return` - 还书

### 用户接口
- `GET /api/user` - 获取用户列表
- `GET /api/user/{id}` - 获取用户详情
- `PUT /api/user` - 修改用户
- `DELETE /api/user/{id}` - 删除用户

## 🔐 JWT 认证

登录成功后会返回 JWT Token，后续请求需要在 Header 中携带：
```
Authorization: Bearer <token>
```

## 📁 项目结构

```
Library/
├── src/
│   ├── main/
│   │   ├── java/cn/whpu/library/
│   │   │   ├── controller/     # 控制器层
│   │   │   ├── service/        # 服务层
│   │   │   ├── mapper/         # 数据访问层
│   │   │   ├── entity/         # 实体类
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── utils/          # 工具类
│   │   │   ├── config/         # 配置类
│   │   │   └── LibraryApplication.java
│   │   └── resources/
│   │       ├── application.properties  # 配置文件
│   │       └── borrow_table.sql        # SQL 脚本
│   └── test/
└── pom.xml                      # Maven 配置
```

## 📝 核心特性

- **JWT 认证**: 安全的用户认证机制
- **Redis 缓存**: 用户数据缓存，提升查询性能
- **事务管理**: 借书操作保证数据一致性
- **RESTful API**: 规范的接口设计
- **统一响应格式**: ApiResponse 统一封装

## 👨‍ 开发说明

### 添加新的 API
1. 在 `controller` 目录创建控制器
2. 在 `service` 目录创建业务逻辑
3. 在 `mapper` 目录创建数据访问接口
4. 在 `entity` 目录创建实体类

### 缓存使用
```java
@Autowired
private RedisCacheService redisCache;

// 写入缓存
redisCache.set("key", value, 300); // 5 分钟过期

// 读取缓存
Object value = redisCache.get("key");

// 删除缓存
redisCache.delete("key");
```

## 📧 联系方式

- GitHub: https://github.com/wuhuwuhuhahahaha/Library

## 📄 许可证

MIT License
