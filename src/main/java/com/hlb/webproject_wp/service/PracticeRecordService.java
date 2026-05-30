package com.hlb.webproject_wp.service;

import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.dto.request.PracticeRecordRequest;
import com.hlb.webproject_wp.dto.response.PracticeRecordVO;
import com.hlb.webproject_wp.dto.response.PracticeStatsVO;

import java.util.List;

public interface PracticeRecordService {

    PracticeRecordVO submit(PracticeRecordRequest request, Long userId);

    PageResult<List<PracticeRecordVO>> getUserRecords(Long userId, int pageNum, int pageSize);

    PracticeStatsVO getUserStats(Long userId);
}
