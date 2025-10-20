package com.tiantianai.auth.controller;

import com.tiantianai.auth.dto.LoginRequest;
import com.tiantianai.auth.dto.LoginResponse;
import com.tiantianai.auth.service.AuthService;
import com.tiantianai.shared.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT 是无状态的，登出由前端清除 token 即可
        // 如果需要实现 token 黑名单，可以在这里添加逻辑
        return Result.success();
    }

    /**
     * 获取权限码
     */
    @GetMapping("/codes")
    public Result<String[]> getAccessCodes(Authentication authentication) {
        // 从认证信息中获取用户 ID
        Long userId = Long.parseLong(authentication.getName());
        String[] codes = authService.getAccessCodes(userId);
        return Result.success(codes);
    }

    /**
     * 刷新 Token
     * 暂时返回成功，后续可以实现 refresh token 机制
     */
    @PostMapping("/refresh")
    public Result<String> refreshToken() {
        return Result.success("Token refreshed");
    }
}
