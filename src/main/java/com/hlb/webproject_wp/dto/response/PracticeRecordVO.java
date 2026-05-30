package com.hlb.webproject_wp.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PracticeRecordVO {
    private Long id;
    private Long userId;
    private Long wordId;
    private Double typingSpeedWpm;
    private BigDecimal accuracy;
    private Integer durationSeconds;
    private Integer mistakesCount;
    private Integer completed;
    private LocalDateTime practicedAt;
    private String wordText;
    private String translation;
}
