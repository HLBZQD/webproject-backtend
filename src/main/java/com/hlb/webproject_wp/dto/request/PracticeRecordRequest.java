package com.hlb.webproject_wp.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PracticeRecordRequest {

    @NotNull(message = "Word ID is required")
    private Long wordId;

    private Double typingSpeedWpm;

    @DecimalMin(value = "0", message = "Accuracy must be 0-100")
    @DecimalMax(value = "100", message = "Accuracy must be 0-100")
    private BigDecimal accuracy;

    private Integer durationSeconds;

    private Integer mistakesCount;

    private Integer completed;
}
