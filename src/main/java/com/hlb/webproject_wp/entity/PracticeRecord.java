package com.hlb.webproject_wp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tb_practice_record")
public class PracticeRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long wordId;

    private Double typingSpeedWpm;

    private BigDecimal accuracy;

    private Integer durationSeconds;

    private Integer mistakesCount;

    private Integer completed;

    private LocalDateTime practicedAt;

    // Transient fields for join queries - not mapped to table
    @TableField(exist = false)
    private String wordText;

    @TableField(exist = false)
    private String translation;
}
