package com.hlb.webproject_wp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hlb.webproject_wp.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM tb_user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(String username);

    @Select("SELECT * FROM tb_user")
    List<User> selectAllUsers();

    @Update("UPDATE tb_user SET deleted = 0 WHERE id = #{id}")
    int restoreById(@Param("id") Long id);

    @Delete("DELETE FROM tb_user WHERE id = #{id}")
    int hardDeleteById(@Param("id") Long id);
}
