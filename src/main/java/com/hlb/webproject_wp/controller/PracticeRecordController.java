package com.hlb.webproject_wp.controller;

import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.common.Result;
import com.hlb.webproject_wp.dto.request.PracticeRecordRequest;
import com.hlb.webproject_wp.dto.response.PracticeRecordVO;
import com.hlb.webproject_wp.dto.response.PracticeStatsVO;
import com.hlb.webproject_wp.security.SecurityContextUtil;
import com.hlb.webproject_wp.service.PracticeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeRecordController {

    private final PracticeRecordService practiceRecordService;

    @PostMapping("/records")
    public Result<PracticeRecordVO> submit(@Valid @RequestBody PracticeRecordRequest request) {
        Long userId = SecurityContextUtil.getCurrentUserIdOrThrow();
        PracticeRecordVO record = practiceRecordService.submit(request, userId);
        return Result.success(record);
    }

    @GetMapping("/records")
    public Result<PageResult<List<PracticeRecordVO>>> getUserRecords(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 若无 userId 参数，使用当前登录用户
        if (userId == null) {
            userId = SecurityContextUtil.getCurrentUserIdOrThrow();
        }
        PageResult<List<PracticeRecordVO>> result = practiceRecordService.getUserRecords(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/stats")
    public Result<PracticeStatsVO> getUserStats(
            @RequestParam(required = false) Long userId) {
        if (userId == null) {
            userId = SecurityContextUtil.getCurrentUserIdOrThrow();
        }
        PracticeStatsVO stats = practiceRecordService.getUserStats(userId);
        return Result.success(stats);
    }
}
