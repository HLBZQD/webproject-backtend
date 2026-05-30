# common — 通用层

## 包说明
Spring Boot 项目的通用基础组件层，提供统一响应格式、自定义异常、全局异常处理等跨模块基础设施。

## 文件说明

### Result.java
- **作用**：统一 API 响应格式
- **设计**：泛型类，包含 `code`（状态码）、`message`（消息）、`data`（数据）
- **关键方法**：
  - `success(T data)` — 成功响应（code=200）
  - `error(int code, String message)` — 错误响应
- **使用**：所有 Controller 的返回值都通过 `Result` 包装

### PageResult.java
- **作用**：分页查询的统一响应格式
- **设计**：继承 `Result<T>`，额外包含 `total`、`page`、`size`、`pages`
- **使用**：所有列表/分页接口返回 `PageResult`

### BusinessException.java
- **作用**：业务逻辑异常，携带 HTTP 状态码
- **设计**：继承 `RuntimeException`，包含 `code` 和 `message`
- **使用**：
  - `new BusinessException(404, "Word not found")` → 404
  - `new BusinessException(409, "Username already exists")` → 409
  - `new BusinessException(401, "Invalid credentials")` → 401

### GlobalExceptionHandler.java
- **作用**：全局异常处理器，统一处理各类异常并返回标准格式
- **处理链**：
  - `BusinessException` → 按 code 返回对应 HTTP 状态码
  - `MethodArgumentNotValidException` → 400 参数校验失败
  - `HttpRequestMethodNotSupportedException` → 405 方法不允许
  - `Exception` → 500 内部服务器错误
