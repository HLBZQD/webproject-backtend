package com.hlb.webproject_wp.service;

import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.dto.request.WordQueryRequest;
import com.hlb.webproject_wp.dto.request.WordSaveRequest;
import com.hlb.webproject_wp.dto.response.WordVO;

import java.util.List;

public interface WordService {

    PageResult<List<WordVO>> query(WordQueryRequest request);

    WordVO getById(Long id);

    WordVO save(WordSaveRequest request);

    WordVO update(Long id, WordSaveRequest request);

    void delete(Long id);
}
