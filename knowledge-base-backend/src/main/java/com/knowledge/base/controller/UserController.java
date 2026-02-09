package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.dto.UserDTO;
import com.knowledge.base.entity.User;
import com.knowledge.base.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<User> getCurrentUser() {
        return Result.success(null);
    }
    
    @Operation(summary = "查询所有用户")
    @GetMapping
    
    public Result<List<User>> listUsers() {
        List<User> users = userService.selectUsersWithRoles();
        return Result.success(users);
    }
    
    @Operation(summary = "查询用户详情")
    @GetMapping("/{id}")
    
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }
    
    @Operation(summary = "创建用户")
    @PostMapping
    
    public Result<Long> createUser(@Valid @RequestBody UserDTO userDTO) {
        Long userId = userService.createUser(userDTO);
        return Result.success(userId);
    }
    
    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        userService.updateUser(userDTO);
        return Result.success();
    }
    
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    @Operation(summary = "重置密码")
    @PutMapping("/{id}/password")
    
    public Result<Void> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return Result.success();
    }
    
    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return Result.success();
    }
    
    @Operation(summary = "分配角色")
    @PutMapping("/{id}/roles")
    
    public Result<Void> assignRoles(@PathVariable Long id, @RequestBody Long[] roleIds) {
        userService.assignRoles(id, roleIds);
        return Result.success();
    }
    
    @Operation(summary = "更新状态")
    @PutMapping("/{id}/status")
    
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }
    
}
