# Word Type — 键盘单词练习系统（后端）

Spring Boot 4 + MyBatis-Plus + JWT 构建的 RESTful API 服务，提供用户认证、单词管理、练习记录存储与统计分析。

## 技术栈

- **框架**: Spring Boot 4.0.6
- **语言**: Java 21
- **ORM**: MyBatis-Plus 3.5.16
- **安全**: Spring Security 7 + JWT (jjwt 0.12.6) + BCrypt
- **数据库**: MySQL 8.0（测试用 H2）
- **构建**: Maven
- **校验**: Jakarta Validation
- **工具**: Lombok

## 快速开始

```bash
# 配置数据库
# 编辑 src/main/resources/application.yml 中的 datasource 配置

# 启动服务（默认 http://localhost:8080）
./mvnw spring-boot:run

# 运行测试
./mvnw test

# 打包
./mvnw package -DskipTests
```

## API 端点

### 认证（公开）
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 → 返回 JWT |

### 单词（需登录）
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/words` | 分页动态查询（11 参数） |
| GET | `/api/words/{id}` | 查询单个 |
| POST | `/api/words` | 新增 |
| PUT | `/api/words/{id}` | 更新（ADMIN） |
| DELETE | `/api/words/{id}` | 删除（ADMIN） |

### 练习（需登录）
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/practice/records` | 提交练习记录 |
| GET | `/api/practice/records` | 分页查询记录（LEFT JOIN） |
| GET | `/api/practice/stats` | 统计（COUNT/AVG/SUM） |

### 用户管理（ADMIN）
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/admin/users` | 用户列表 |
| POST | `/api/admin/users` | 创建用户 |
| PUT | `/api/admin/users/{id}` | 更新用户 |
| DELETE | `/api/admin/users/{id}` | 软删除 |
| PUT | `/api/admin/users/{id}/restore` | 恢复 |
| DELETE | `/api/admin/users/{id}/hard` | 物理删除 |

## 数据库

三张表：`tb_user`（用户）、`tb_word`（单词）、`tb_practice_record`（练习记录）
外键关联 + 逻辑删除 + 级联删除。

## 项目结构

```
src/main/java/com/hlb/webproject_wp/
├── common/        # Result/PageResult/异常处理/常量
├── config/        # Security/CORS/MyBatis-Plus/自动填充
├── controller/    # REST 控制器
├── dto/           # 请求/响应 DTO
├── entity/        # 数据库实体
├── mapper/        # MyBatis Mapper + XML
├── security/      # JWT 工具 + 认证过滤器
└── service/       # 业务逻辑（接口 + 实现）
```
