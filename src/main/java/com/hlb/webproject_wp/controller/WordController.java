package com.hlb.webproject_wp.controller;

import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.common.Result;
import com.hlb.webproject_wp.dto.request.WordSaveRequest;
import com.hlb.webproject_wp.dto.response.WordVO;
import com.hlb.webproject_wp.service.WordService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping
    public Result<PageResult<List<WordVO>>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<List<WordVO>> result = wordService.page(page, size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<WordVO> getById(@PathVariable Long id) {
        WordVO word = wordService.getById(id);
        return Result.success(word);
    }

    @PostMapping
    public Result<WordVO> create(@Valid @RequestBody WordSaveRequest request) {
        WordVO word = wordService.save(request);
        return Result.success(word);
    }

    @PutMapping("/{id}")
    public Result<WordVO> update(@PathVariable Long id, @Valid @RequestBody WordSaveRequest request) {
        WordVO word = wordService.update(id, request);
        return Result.success(word);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        wordService.delete(id);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<PageResult<List<WordVO>>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<List<WordVO>> result = wordService.search(keyword, page, size);
        return Result.success(result);
    }
}
