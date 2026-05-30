package com.hlb.webproject_wp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hlb.webproject_wp.common.BusinessException;
import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.dto.request.WordQueryRequest;
import com.hlb.webproject_wp.dto.request.WordSaveRequest;
import com.hlb.webproject_wp.dto.response.WordVO;
import com.hlb.webproject_wp.entity.Word;
import com.hlb.webproject_wp.mapper.WordMapper;
import com.hlb.webproject_wp.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordMapper wordMapper;

    @Override
    public PageResult<List<WordVO>> query(WordQueryRequest request) {
        LambdaQueryWrapper<Word> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w ->
                w.like(Word::getWord, request.getKeyword())
                 .or()
                 .like(Word::getTranslation, request.getKeyword()));
        }

        if (request.getDifficultyLevel() != null) {
            wrapper.eq(Word::getDifficultyLevel, request.getDifficultyLevel());
        }
        if (StringUtils.hasText(request.getWordCategory())) {
            wrapper.eq(Word::getWordCategory, request.getWordCategory());
        }

        applySort(wrapper, request.getSortField(), request.getSortOrder());

        // Manual pagination — PaginationInnerInterceptor is not available in
        // MyBatis-Plus 3.5.16 + Spring Boot 4 starter, so we handle it here.
        long total = wordMapper.selectCount(wrapper);
        int pageNum = request.getPage();
        int pageSize = request.getSize();
        long offset = (long) (pageNum - 1) * pageSize;

        // Clone wrapper for the data query with LIMIT appended
        wrapper.last("LIMIT " + offset + ", " + pageSize);
        List<Word> records = wordMapper.selectList(wrapper);

        List<WordVO> voList = records.stream()
                .map(this::toWordVO)
                .collect(Collectors.toList());

        return PageResult.success(voList, total, pageNum, pageSize);
    }

    private void applySort(LambdaQueryWrapper<Word> wrapper, String sortField, String sortOrder) {
        boolean isAsc = !"desc".equalsIgnoreCase(sortOrder);

        if (!StringUtils.hasText(sortField)) {
            wrapper.orderByAsc(Word::getWord);
            return;
        }

        switch (sortField) {
            case "word":
                wrapper.orderBy(true, isAsc, Word::getWord);
                break;
            case "translation":
                wrapper.orderBy(true, isAsc, Word::getTranslation);
                break;
            case "difficultyLevel":
                wrapper.orderBy(true, isAsc, Word::getDifficultyLevel);
                break;
            case "sound":
                wrapper.orderBy(true, isAsc, Word::getSound);
                break;
            case "wordCategory":
                wrapper.orderBy(true, isAsc, Word::getWordCategory);
                break;
            case "createdAt":
                wrapper.orderBy(true, isAsc, Word::getCreatedAt);
                break;
            case "updatedAt":
                wrapper.orderBy(true, isAsc, Word::getUpdatedAt);
                break;
            default:
                wrapper.orderByAsc(Word::getWord);
        }
    }

    @Override
    public WordVO getById(Long id) {
        Word word = wordMapper.selectById(id);
        if (word == null) {
            throw new BusinessException(404, "Word not found");
        }
        return toWordVO(word);
    }

    @Override
    public WordVO save(WordSaveRequest request) {
        Word word = new Word();
        BeanUtils.copyProperties(request, word, "id");
        wordMapper.insert(word);
        log.info("Word created: id={}, word={}", word.getId(), word.getWord());
        return toWordVO(word);
    }

    @Override
    public WordVO update(Long id, WordSaveRequest request) {
        Word word = wordMapper.selectById(id);
        if (word == null) {
            throw new BusinessException(404, "Word not found");
        }

        BeanUtils.copyProperties(request, word, "id");
        word.setId(id);
        wordMapper.updateById(word);
        log.info("Word updated: id={}, word={}", word.getId(), word.getWord());
        return toWordVO(word);
    }

    @Override
    public void delete(Long id) {
        boolean deleted = wordMapper.deleteById(id) > 0;
        if (!deleted) {
            throw new BusinessException(404, "Word not found");
        }
        log.info("Word deleted: id={}", id);
    }

    private WordVO toWordVO(Word word) {
        WordVO vo = new WordVO();
        BeanUtils.copyProperties(word, vo);
        return vo;
    }
}
