# dto — 数据传输对象

## 包说明
DTO（Data Transfer Object）包，分为请求体（request）和响应体（response），实现输入验证和输出脱敏。

## request/ — 请求体

| 文件 | 用途 | 验证注解 |
|------|------|---------|
| LoginRequest.java | 登录请求 | `@NotBlank` username, password |
| RegisterRequest.java | 注册请求 | `@NotBlank` @Size username, password; `@Email` email |
| WordSaveRequest.java | 单词新增/编辑 | `@NotBlank` @Size word, translation; `@Min(1) @Max(5)` difficultyLevel |
| PracticeRecordRequest.java | 练习记录提交 | `@NotNull` wordId; `@DecimalMin(0) @DecimalMax(100)` accuracy |

## response/ — 响应体

| 文件 | 用途 | 注意 |
|------|------|------|
| UserVO.java | 用户信息 | **不包含 password 字段**（安全） |
| WordVO.java | 单词信息 | 完整字段 |
| PracticeRecordVO.java | 练习记录 | 包含 JOIN 的 wordText 和 translation |
| PracticeStatsVO.java | 练习统计 | 聚合数据（总数、平均速度、平均准确率） |
