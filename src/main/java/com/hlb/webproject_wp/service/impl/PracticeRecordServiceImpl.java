package com.hlb.webproject_wp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hlb.webproject_wp.common.BusinessException;
import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.dto.request.PracticeRecordRequest;
import com.hlb.webproject_wp.dto.response.PracticeRecordVO;
import com.hlb.webproject_wp.dto.response.PracticeStatsVO;
import com.hlb.webproject_wp.entity.PracticeRecord;
import com.hlb.webproject_wp.entity.User;
import com.hlb.webproject_wp.entity.Word;
import com.hlb.webproject_wp.mapper.PracticeRecordMapper;
import com.hlb.webproject_wp.mapper.UserMapper;
import com.hlb.webproject_wp.mapper.WordMapper;
import com.hlb.webproject_wp.mapper.PracticeRecordMapper.PracticeStats;
import com.hlb.webproject_wp.service.PracticeRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PracticeRecordServiceImpl implements PracticeRecordService {

    private final PracticeRecordMapper practiceRecordMapper;
    private final WordMapper wordMapper;
    private final UserMapper userMapper;

    @Override
    public PracticeRecordVO submit(PracticeRecordRequest request, Long userId) {
        // Validate word exists
        Word word = wordMapper.selectById(request.getWordId());
        if (word == null) {
            throw new BusinessException(404, "Word not found");
        }

        // Validate user exists
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }

        PracticeRecord record = new PracticeRecord();
        record.setUserId(userId);
        record.setWordId(request.getWordId());
        record.setTypingSpeedWpm(request.getTypingSpeedWpm());
        record.setAccuracy(request.getAccuracy());
        record.setDurationSeconds(request.getDurationSeconds());
        record.setMistakesCount(request.getMistakesCount() != null ? request.getMistakesCount() : 0);
        record.setCompleted(request.getCompleted() != null ? request.getCompleted() : 1);
        record.setPracticedAt(LocalDateTime.now());

        practiceRecordMapper.insert(record);
        log.info("Practice record submitted: userId={}, wordId={}, id={}", userId, request.getWordId(), record.getId());

        PracticeRecordVO vo = new PracticeRecordVO();
        BeanUtils.copyProperties(record, vo);
        vo.setWordText(word.getWord());
        vo.setTranslation(word.getTranslation());
        return vo;
    }

    @Override
    public PageResult<List<PracticeRecordVO>> getUserRecords(Long userId, int pageNum, int pageSize) {
        long total = practiceRecordMapper.countByUserId(userId);
        long offset = (long) (pageNum - 1) * pageSize;

        List<PracticeRecord> records = practiceRecordMapper.selectPageWithWord(userId, offset, pageSize);

        List<PracticeRecordVO> voList = records.stream()
                .map(record -> {
                    PracticeRecordVO vo = new PracticeRecordVO();
                    BeanUtils.copyProperties(record, vo);
                    vo.setWordText(record.getWordText());
                    vo.setTranslation(record.getTranslation());
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResult.success(voList, total, pageNum, pageSize);
    }

    @Override
    public PracticeStatsVO getUserStats(Long userId) {
        PracticeStats stats = practiceRecordMapper.selectStats(userId);
        PracticeStatsVO vo = new PracticeStatsVO();

        if (stats != null) {
            vo.setTotalPractices(stats.getTotalPractices());
            vo.setAvgSpeedWpm(stats.getAvgSpeedWpm());
            if (stats.getAvgAccuracy() != null) {
                vo.setAvgAccuracy(stats.getAvgAccuracy().setScale(2, RoundingMode.HALF_UP));
            } else {
                vo.setAvgAccuracy(BigDecimal.ZERO);
            }
            vo.setTotalDurationSeconds(stats.getTotalDurationSeconds());
        }

        return vo;
    }
}
