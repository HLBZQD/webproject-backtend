# entity — 实体层

## 包说明
MyBatis-Plus 实体类，与数据库表一一映射。

## 文件说明

### User.java
- **对应表**：`tb_user`
- **字段**：id, username(唯一), password, email, nickname, avatarUrl, role(默认"user"), deleted(逻辑删除), createdAt, updatedAt
- **关键注解**：
  - `@TableLogic` — deleted 字段标记为逻辑删除
  - `@TableField(fill=FieldFill.INSERT)` — createdAt 插入时自动填充
  - `@TableField(fill=FieldFill.INSERT_UPDATE)` — updatedAt 插入和更新时自动填充

### Word.java
- **对应表**：`tb_word`
- **字段**：id, word, translation, phonetic, difficultyLevel(1-5), partOfSpeech, exampleSentence, wordCategory, createdAt, updatedAt
- **用途**：存储英语单词及其释义、音标、词性、例句等

### PracticeRecord.java
- **对应表**：`tb_practice_record`
- **字段**：id, userId(FK), wordId(FK), typingSpeedWpm, accuracy, durationSeconds, mistakesCount, completed, practicedAt
- **关联字段**（非数据库）：
  - `wordText` — `@TableField(exist=false)`，通过 JOIN 查询填充
  - `translation` — `@TableField(exist=false)`，通过 JOIN 查询填充
- **外键关系**：
  - `userId` → `tb_user.id`
  - `wordId` → `tb_word.id`
