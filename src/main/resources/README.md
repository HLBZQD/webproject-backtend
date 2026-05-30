# resources — 配置文件

## 文件说明

### application.yml — 主配置文件
Spring Boot 核心配置，包含服务器端口、数据库连接、MyBatis-Plus、JWT 密钥等。
关键配置项已在文件中用 YAML 注释标注。

### db/schema.sql — 数据库建表脚本
MySQL DDL 脚本，创建 3 张业务表：
- tb_user — 用户表（10 字段）
- tb_word — 单词表（10 字段）
- tb_practice_record — 练习记录表（9 字段，含外键约束）

### mapper/ — MyBatis XML 映射文件
| 文件 | 作用 |
|------|------|
| WordMapper.xml | `searchByKeyword` — 单词模糊搜索（LIKE 匹配 word 和 translation） |
| PracticeRecordMapper.xml | `selectPageWithWord` — LEFT JOIN 查询（记录+单词信息）; `selectStats` — 聚合统计 |
