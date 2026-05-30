package com.hlb.webproject_wp.controller;

import com.hlb.webproject_wp.common.Result;
import com.hlb.webproject_wp.dto.request.LoginRequest;
import com.hlb.webproject_wp.dto.request.RegisterRequest;
import com.hlb.webproject_wp.dto.response.UserVO;
import com.hlb.webproject_wp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest request) {
        UserVO user = userService.register(request);
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> result = userService.login(request);
        return Result.success(result);
    }
}
