package com.hlb.webproject_wp.service.impl;

import com.hlb.webproject_wp.common.BusinessException;
import com.hlb.webproject_wp.dto.request.LoginRequest;
import com.hlb.webproject_wp.dto.request.RegisterRequest;
import com.hlb.webproject_wp.dto.response.UserVO;
import com.hlb.webproject_wp.entity.User;
import com.hlb.webproject_wp.mapper.UserMapper;
import com.hlb.webproject_wp.security.JwtUtil;
import com.hlb.webproject_wp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserVO register(RegisterRequest request) {
        User existing = userMapper.selectByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException(409, "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setRole("user");
        userMapper.insert(user);

        log.info("User registered: id={}, username={}", user.getId(), user.getUsername());
        return toUserVO(user);
    }

    @Override
    public Map<String, Object> login(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(401, "Invalid username or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        log.info("User logged in: id={}, username={}", user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", toUserVO(user));
        return result;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    private UserVO toUserVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
