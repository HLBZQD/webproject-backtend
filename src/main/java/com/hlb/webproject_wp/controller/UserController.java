package com.hlb.webproject_wp.controller;

import com.hlb.webproject_wp.common.PageResult;
import com.hlb.webproject_wp.common.Result;
import com.hlb.webproject_wp.dto.request.CreateUserDTO;
import com.hlb.webproject_wp.dto.request.UpdateUserDTO;
import com.hlb.webproject_wp.dto.response.AdminUserVO;
import com.hlb.webproject_wp.service.UserService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Result<PageResult<List<AdminUserVO>>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean showDeleted,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder) {
        PageResult<List<AdminUserVO>> result = userService.listUsers(page, size, keyword, role, showDeleted, sortField, sortOrder);
        return Result.success(result);
    }

    @PostMapping
    public Result<AdminUserVO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        AdminUserVO user = userService.createUser(dto);
        return Result.success(user);
    }

    @PutMapping("/{id}")
    public Result<AdminUserVO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
        AdminUserVO user = userService.updateUser(id, dto);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PutMapping("/{id}/restore")
    public Result<Void> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return Result.success();
    }

    @DeleteMapping("/{id}/hard")
    public Result<Void> hardDeleteUser(@PathVariable Long id) {
        userService.hardDeleteUser(id);
        return Result.success();
    }
}
