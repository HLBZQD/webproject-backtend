package com.hlb.webproject_wp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlb.webproject_wp.entity.PracticeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface PracticeRecordMapper extends BaseMapper<PracticeRecord> {

    Page<PracticeRecord> selectPageWithWord(Page<PracticeRecord> page, @Param("userId") Long userId);

    PracticeStats selectStats(@Param("userId") Long userId);

    class PracticeStats {
        private long totalPractices;
        private double avgSpeedWpm;
        private BigDecimal avgAccuracy;
        private long totalDurationSeconds;

        public long getTotalPractices() { return totalPractices; }
        public void setTotalPractices(long totalPractices) { this.totalPractices = totalPractices; }
        public double getAvgSpeedWpm() { return avgSpeedWpm; }
        public void setAvgSpeedWpm(double avgSpeedWpm) { this.avgSpeedWpm = avgSpeedWpm; }
        public BigDecimal getAvgAccuracy() { return avgAccuracy; }
        public void setAvgAccuracy(BigDecimal avgAccuracy) { this.avgAccuracy = avgAccuracy; }
        public long getTotalDurationSeconds() { return totalDurationSeconds; }
        public void setTotalDurationSeconds(long totalDurationSeconds) { this.totalDurationSeconds = totalDurationSeconds; }
    }
}
