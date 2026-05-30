package com.hlb.webproject_wp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_word")
public class Word {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String word;

    private String translation;

    private String phonetic;

    private Integer difficultyLevel;

    private String sound;

    private String exampleSentence;

    private String wordCategory;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
