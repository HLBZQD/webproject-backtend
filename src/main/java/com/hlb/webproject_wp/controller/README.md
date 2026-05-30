# controller — 控制器层

## 包说明
Spring MVC REST 控制器，负责接收 HTTP 请求、调用 Service、返回 `Result` 格式响应。

## 文件说明

### AuthController.java — 认证接口
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/register` | 用户注册 | 无 |
| POST | `/api/auth/login` | 用户登录，返回 JWT | 无 |
- 使用 `@Valid` 自动触发请求体校验
- 登录响应格式：`{token, user}`

### WordController.java — 单词 CRUD
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/words?page=&size=` | 分页查询 | 需要 |
| GET | `/api/words/{id}` | 单个查询 | 需要 |
| POST | `/api/words` | 新增单词 | 需要 |
| PUT | `/api/words/{id}` | 更新单词 | 需要 |
| DELETE | `/api/words/{id}` | 删除单词 | 需要 |
| GET | `/api/words/search?keyword=&page=&size=` | 模糊搜索 | 需要 |

### PracticeRecordController.java — 练习记录
| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/practice/records` | 提交练习记录 | 需要 |
| GET | `/api/practice/records?userId=&page=&size=` | 查询记录 | 需要 |
| GET | `/api/practice/stats?userId=` | 用户统计 | 需要 |
- 通过 `SecurityContextUtil.getCurrentUserIdOrThrow()` 获取当前用户
