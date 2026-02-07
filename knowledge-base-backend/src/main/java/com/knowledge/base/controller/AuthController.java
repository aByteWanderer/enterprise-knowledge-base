package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.dto.LoginRequest;
import com.knowledge.base.service.AuthService;
import com.knowledge.base.vo.LoginVO;
import com.knowledge.base.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginVO loginVO = authService.login(loginRequest);
        return Result.success(loginVO);
    }
    
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }
    
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getCurrentUserInfo() {
        UserInfoVO userInfo = authService.getCurrentUserInfo();
        return Result.success(userInfo);
    }
    
    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(@RequestParam String refreshToken) {
        LoginVO loginVO = authService.refreshToken(refreshToken);
        return Result.success(loginVO);
    }
    
}
