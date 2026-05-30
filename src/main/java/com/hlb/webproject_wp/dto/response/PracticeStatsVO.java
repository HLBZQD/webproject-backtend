package com.hlb.webproject_wp.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PracticeStatsVO {
    private long totalPractices;
    private double avgSpeedWpm;
    private BigDecimal avgAccuracy;
    private long totalDurationSeconds;
}
