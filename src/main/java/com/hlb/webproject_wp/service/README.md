# service — 业务逻辑层

## 包说明
Service 层封装业务逻辑，Controller 通过调用 Service 方法完成业务操作。使用接口-实现分离模式。

## service/ — 接口

| 接口 | 核心方法 |
|------|---------|
| UserService.java | `register()`, `login()` — 返回 token+用户 |
| WordService.java | `page()`, `getById()`, `save()`, `update()`, `delete()`, `search()` |
| PracticeRecordService.java | `submit()`, `getUserRecords()`, `getUserStats()` |

## service/impl/ — 实现

### UserServiceImpl.java
- `register()` — 检查用户名唯一性 → BCrypt 加密密码 → 插入用户 → 返回 UserVO
- `login()` — 查询用户 → BCrypt 验证密码 → 生成 JWT → 返回 token+用户

### WordServiceImpl.java
- 标准 CRUD：分页查询、单个查询、新增、更新（BeanUtils 复制属性）、删除
- `search()` — 使用 `LambdaQueryWrapper.like()` 对 word 和 translation 两个字段做模糊匹配

### PracticeRecordServiceImpl.java
- `submit()` — 验证 wordId 存在 → 插入记录 → 返回带单词信息的 VO
- `getUserRecords()` — 通过 `selectPageWithWord` 一次 JOIN 查询获取记录+单词信息
- `getUserStats()` — 聚合查询（总练习次数、平均速度、平均准确率、总时长）
