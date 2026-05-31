package com.hlb.webproject_wp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WordQueryRequest {

    @Size(max = 100, message = "Keyword too long")
    private String keyword;

    @Size(max = 100, message = "Prefix too long")
    private String prefix;

    @Size(max = 100, message = "Suffix too long")
    private String suffix;

    private Long idFrom;
    private Long idTo;

    @Min(value = 1, message = "Difficulty must be 1-5")
    @Max(value = 5, message = "Difficulty must be 1-5")
    private Integer difficultyLevel;

    @Size(max = 100, message = "Word category too long")
    private String wordCategory;

    /** 排序字段: word, translation, difficultyLevel, sound, wordCategory, createdAt, updatedAt */
    @Size(max = 30, message = "Sort field too long")
    private String sortField;

    /** 排序方向: asc 或 desc */
    @Pattern(regexp = "^(asc|desc)$", message = "Sort order must be 'asc' or 'desc'")
    private String sortOrder;

    @Min(value = 1, message = "Page must be >= 1")
    private int page = 1;

    @Min(value = 1, message = "Size must be >= 1")
    private int size = 10;
}
