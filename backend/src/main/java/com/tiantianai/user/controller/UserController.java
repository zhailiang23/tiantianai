package com.tiantianai.user.controller;

import com.tiantianai.shared.common.Result;
import com.tiantianai.user.dto.UserInfoResponse;
import com.tiantianai.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo(Authentication authentication) {
        // 从认证信息中获取用户 ID
        Long userId = Long.parseLong(authentication.getName());
        UserInfoResponse userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }
}
