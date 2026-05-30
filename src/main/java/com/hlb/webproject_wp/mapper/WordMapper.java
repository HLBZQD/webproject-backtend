package com.hlb.webproject_wp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlb.webproject_wp.entity.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WordMapper extends BaseMapper<Word> {

    Page<Word> searchByKeyword(Page<Word> page, @Param("keyword") String keyword);
}
