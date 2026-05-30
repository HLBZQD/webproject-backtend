package com.hlb.webproject_wp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WordSaveRequest {

    @NotBlank(message = "Word is required")
    @Size(max = 100, message = "Word too long")
    private String word;

    @NotBlank(message = "Translation is required")
    @Size(max = 500, message = "Translation too long")
    private String translation;

    @Size(max = 200, message = "Phonetic too long")
    private String phonetic;

    @Min(value = 1, message = "Difficulty must be 1-5")
    @Max(value = 5, message = "Difficulty must be 1-5")
    private Integer difficultyLevel;

    @Size(max = 50, message = "Part of speech too long")
    private String partOfSpeech;

    @Size(max = 1000, message = "Example sentence too long")
    private String exampleSentence;

    @Size(max = 100, message = "Word category too long")
    private String wordCategory;
}
