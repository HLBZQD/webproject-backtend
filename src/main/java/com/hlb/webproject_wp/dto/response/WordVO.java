package com.hlb.webproject_wp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WordVO {
    private Long id;
    private String word;
    private String translation;
    private String phonetic;
    private Integer difficultyLevel;
    private String partOfSpeech;
    private String exampleSentence;
    private String wordCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
