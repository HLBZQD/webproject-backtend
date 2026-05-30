-- WordType Database Schema
-- English Vocabulary Memorization & Typing Practice Platform

CREATE DATABASE IF NOT EXISTS wordtype DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wordtype;

-- User table
DROP TABLE IF EXISTS tb_practice_record;
DROP TABLE IF EXISTS tb_word;
DROP TABLE IF EXISTS tb_user;

CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    nickname VARCHAR(50),
    avatar_url VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '0-active, 1-deleted',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User table';

-- Word table
CREATE TABLE tb_word (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(100) NOT NULL,
    translation VARCHAR(500) NOT NULL,
    phonetic VARCHAR(200),
    difficulty_level TINYINT NOT NULL DEFAULT 1 COMMENT '1-5, easy to hard',
    sound VARCHAR(500) NOT NULL,
    example_sentence VARCHAR(1000),
    word_category VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_word (word),
    INDEX idx_difficulty (difficulty_level),
    INDEX idx_category (word_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='English vocabulary word table';

-- Practice record table
CREATE TABLE tb_practice_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    word_id BIGINT NOT NULL,
    typing_speed_wpm DOUBLE COMMENT 'Words per minute',
    accuracy DECIMAL(5,2) COMMENT 'Accuracy percentage 0-100',
    duration_seconds INT COMMENT 'Practice duration in seconds',
    mistakes_count INT DEFAULT 0,
    completed TINYINT NOT NULL DEFAULT 1 COMMENT '0-incomplete, 1-completed',
    practiced_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_word_id (word_id),
    INDEX idx_practiced_at (practiced_at),
    CONSTRAINT fk_practice_user FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_practice_word FOREIGN KEY (word_id) REFERENCES tb_word(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Typing practice record table';
