-- H2-compatible schema for integration tests
-- Derived from main schema.sql with MySQL-specific syntax removed

DROP TABLE IF EXISTS tb_practice_record;
DROP TABLE IF EXISTS tb_word;
DROP TABLE IF EXISTS tb_user;

-- User table
CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    nickname VARCHAR(50),
    avatar_url VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_username ON tb_user (username);
CREATE INDEX idx_email ON tb_user (email);

-- Word table
CREATE TABLE tb_word (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(100) NOT NULL,
    translation VARCHAR(500) NOT NULL,
    phonetic VARCHAR(200),
    difficulty_level TINYINT NOT NULL DEFAULT 1,
    part_of_speech VARCHAR(50),
    example_sentence VARCHAR(1000),
    word_category VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_word ON tb_word (word);
CREATE INDEX idx_difficulty ON tb_word (difficulty_level);
CREATE INDEX idx_category ON tb_word (word_category);

-- Practice record table
CREATE TABLE tb_practice_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    word_id BIGINT NOT NULL,
    typing_speed_wpm DOUBLE,
    accuracy DECIMAL(5,2),
    duration_seconds INT,
    mistakes_count INT DEFAULT 0,
    completed TINYINT NOT NULL DEFAULT 1,
    practiced_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_practice_user FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_practice_word FOREIGN KEY (word_id) REFERENCES tb_word(id) ON DELETE CASCADE
);
CREATE INDEX idx_pr_user_id ON tb_practice_record (user_id);
CREATE INDEX idx_pr_word_id ON tb_practice_record (word_id);
CREATE INDEX idx_pr_practiced_at ON tb_practice_record (practiced_at);
