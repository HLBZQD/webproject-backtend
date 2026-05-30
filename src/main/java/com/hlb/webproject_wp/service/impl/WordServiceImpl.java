package com.hlb.webproject_wp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlb.webproject_wp.common.BusinessException;
import com.hlb.webproject_wp.common.PageResult;
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
    public PageResult<List<WordVO>> page(int pageNum, int pageSize) {
        Page<Word> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Word> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Word::getWord);

        Page<Word> result = wordMapper.selectPage(page, wrapper);

        List<WordVO> voList = result.getRecords().stream()
                .map(this::toWordVO)
                .collect(Collectors.toList());

        return PageResult.success(voList, result.getTotal(), result.getCurrent(), result.getSize());
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

    @Override
    public PageResult<List<WordVO>> search(String keyword, int pageNum, int pageSize) {
        if (!StringUtils.hasText(keyword)) {
            return page(pageNum, pageSize);
        }

        Page<Word> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Word> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Word::getWord, keyword)
                .or()
                .like(Word::getTranslation, keyword)
                .orderByAsc(Word::getWord);

        Page<Word> result = wordMapper.selectPage(page, wrapper);

        List<WordVO> voList = result.getRecords().stream()
                .map(this::toWordVO)
                .collect(Collectors.toList());

        return PageResult.success(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    private WordVO toWordVO(Word word) {
        WordVO vo = new WordVO();
        BeanUtils.copyProperties(word, vo);
        return vo;
    }
}
