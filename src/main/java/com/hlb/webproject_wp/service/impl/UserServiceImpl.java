package com.hlb.webproject_wp.service.impl;

import com.hlb.webproject_wp.common.BusinessException;
import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.dto.request.CreateUserDTO;
import com.hlb.webproject_wp.dto.request.LoginRequest;
import com.hlb.webproject_wp.dto.request.RegisterRequest;
import com.hlb.webproject_wp.dto.request.UpdateUserDTO;
import com.hlb.webproject_wp.dto.response.AdminUserVO;
import com.hlb.webproject_wp.dto.response.UserVO;
import com.hlb.webproject_wp.entity.User;
import com.hlb.webproject_wp.mapper.UserMapper;
import com.hlb.webproject_wp.security.JwtUtil;
import com.hlb.webproject_wp.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
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

    // ── Admin user management ──

    @Override
    public PageResult<List<AdminUserVO>> listUsers(int page, int size) {
        long offset = (long) (page - 1) * size;
        long total = userMapper.selectCount(new LambdaQueryWrapper<>());

        List<User> users = userMapper.selectList(
            new LambdaQueryWrapper<User>().last("LIMIT " + offset + ", " + size));

        List<AdminUserVO> voList = users.stream()
                .map(this::toAdminUserVO)
                .collect(Collectors.toList());

        return PageResult.success(voList, total, page, size);
    }

    @Override
    public AdminUserVO createUser(CreateUserDTO dto) {
        User existing = userMapper.selectByUsername(dto.getUsername());
        if (existing != null) {
            throw new BusinessException(409, "Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setRole(dto.getRole() != null ? dto.getRole() : "user");
        userMapper.insert(user);
        log.info("Admin created user: id={}, username={}, role={}", user.getId(), user.getUsername(), user.getRole());
        return toAdminUserVO(user);
    }

    @Override
    public AdminUserVO updateUser(Long id, UpdateUserDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        userMapper.updateById(user);
        log.info("Admin updated user: id={}, role={}", user.getId(), user.getRole());
        return toAdminUserVO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "User not found");
        }
        userMapper.deleteById(id);
        log.info("Admin deleted user: id={}", id);
    }

    private AdminUserVO toAdminUserVO(User user) {
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
