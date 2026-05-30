# config — 配置层

## 包说明
Spring Boot 配置类包，使用 Java Config 方式替代 XML 配置，管理 MyBatis-Plus、CORS、Spring Security 等组件。

## 文件说明

### MyBatisPlusConfig.java
- **作用**：配置 MyBatis-Plus 插件
- **内容**：
  - `@MapperScan("com.hlb.webproject_wp.mapper")` — 扫描 Mapper 接口
  - `MybatisPlusInterceptor` Bean — 注册拦截器
- **注意**：MyBatis-Plus 3.5.16 中 `PaginationInnerInterceptor` 已移除，分页由框架自动处理

### WebConfig.java
- **作用**：Spring MVC 配置（CORS 跨域）
- **内容**：
  - 允许 `/api/**` 路径的跨域请求
  - 允许所有来源、GET/POST/PUT/DELETE/OPTIONS 方法
  - 允许携带 Cookie（`allowCredentials(true)`）

### SecurityConfig.java
- **作用**：Spring Security 安全配置（核心）
- **内容**：
  - `SecurityFilterChain` — 仅在非 test 环境下生效（`@Profile("!test")`）
  - CSRF 保护关闭（REST API 无状态）
  - 会话管理设为 `STATELESS`（无状态 JWT 认证）
  - `/api/auth/**` — 公开访问（登录/注册）
  - 其他所有路径 — 需认证
  - JWT 过滤器注册在 UsernamePasswordAuthenticationFilter 之前
  - `PasswordEncoder` Bean — BCrypt 密码加密

### MyMetaObjectHandler.java
- **作用**：MyBatis-Plus 自动填充处理器
- **内容**：
  - `insertFill` — 插入时自动填充 `createdAt`、`updatedAt` 为当前时间
  - `updateFill` — 更新时自动填充 `updatedAt` 为当前时间
- **必要性**：Entity 中 `@TableField(fill=FieldFill.INSERT)` 依赖此处理器
