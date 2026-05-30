package com.hlb.webproject_wp.service;

import com.hlb.webproject_wp.dto.request.LoginRequest;
import com.hlb.webproject_wp.dto.request.RegisterRequest;
import com.hlb.webproject_wp.dto.response.UserVO;
import com.hlb.webproject_wp.entity.User;

import java.util.Map;

public interface UserService {

    UserVO register(RegisterRequest request);

    Map<String, Object> login(LoginRequest request);

    User findByUsername(String username);
}
