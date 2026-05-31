package com.hlb.webproject_wp.service;

import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.dto.request.CreateUserDTO;
import com.hlb.webproject_wp.dto.request.LoginRequest;
import com.hlb.webproject_wp.dto.request.RegisterRequest;
import com.hlb.webproject_wp.dto.request.UpdateUserDTO;
import com.hlb.webproject_wp.dto.response.AdminUserVO;
import com.hlb.webproject_wp.dto.response.UserVO;
import com.hlb.webproject_wp.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserVO register(RegisterRequest request);

    Map<String, Object> login(LoginRequest request);

    User findByUsername(String username);

    // ── 管理员用户管理 ──
    PageResult<List<AdminUserVO>> listUsers(int page, int size, String keyword, String role, Boolean showDeleted, String sortField, String sortOrder);
    AdminUserVO createUser(CreateUserDTO dto);
    AdminUserVO updateUser(Long id, UpdateUserDTO dto);
    void deleteUser(Long id);
    void restoreUser(Long id);
    void hardDeleteUser(Long id);
}
