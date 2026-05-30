# mapper — 数据访问层

## 包说明
MyBatis-Plus Mapper 接口，负责与数据库交互。继承 `BaseMapper<T>` 后自动获得 CRUD 方法（insert、deleteById、updateById、selectById、selectList、selectPage 等）。

## 文件说明

### UserMapper.java
- **继承**：`BaseMapper<User>`
- **自定义方法**：
  - `selectByUsername(String username)` — 按用户名查询（`@Select` 注解）
  - SQL: `SELECT * FROM tb_user WHERE username = #{username} AND deleted = 0`

### WordMapper.java
- **继承**：`BaseMapper<Word>`
- **自定义方法**：
  - `searchByKeyword(Page, String)` — 模糊搜索（XML 实现，见 `resources/mapper/WordMapper.xml`）

### PracticeRecordMapper.java
- **继承**：`BaseMapper<PracticeRecord>`
- **自定义方法**：
  - `selectPageWithWord(Page, Long)` — LEFT JOIN tb_word 的分页查询（XML 实现）
  - `selectStats(Long)` — 用户统计聚合（COUNT、AVG、SUM）（XML 实现）
- **内部类**：`PracticeStats` — 统计数据载体
