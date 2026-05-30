# security — 安全认证层

## 包说明
JWT（JSON Web Token）认证模块，实现 Token 生成/验证、请求拦截、上下文传递。

## 文件说明

### JwtUtil.java
- **作用**：JWT 工具类，负责 Token 的生成、验证和解析
- **关键方法**：
  - `generateToken(userId, username)` — 生成 JWT，包含用户 ID 和用户名，有效期由配置文件控制
  - `validateToken(token)` — 验证 Token 是否有效（签名、过期时间）
  - `getUserIdFromToken(token)` — 从 Token 中提取用户 ID
  - `getUsernameFromToken(token)` — 从 Token 中提取用户名
- **配置来源**：`application.yml` 中的 `jwt.secret` 和 `jwt.expiration`
- **密钥**：使用 HMAC-SHA256 算法（jjwt 0.12.6）

### JwtAuthenticationFilter.java
- **作用**：JWT 认证过滤器（`OncePerRequestFilter`），每个请求执行一次
- **流程**：
  1. 检查请求路径是否在排除列表（`/api/auth/**`、`/error`）→ 是则放行
  2. 从 `Authorization: Bearer <token>` 头提取 Token
  3. 验证 Token 有效性
  4. 有效 → 设置 `SecurityContextHolder`（Spring Security 认证上下文）
  5. 放行请求

### SecurityContextUtil.java
- **作用**：线程安全的当前用户信息访问工具
- **关键方法**：
  - `getCurrentUserId()` — 获取当前登录用户 ID
  - `getCurrentUserIdOrThrow()` — 获取用户 ID，未登录则抛出 401 异常
- **实现原理**：从 `RequestContextHolder` 获取请求属性
