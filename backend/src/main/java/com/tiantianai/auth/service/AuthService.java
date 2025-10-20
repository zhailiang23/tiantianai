package com.tiantianai.auth.service;

import com.tiantianai.auth.dto.LoginRequest;
import com.tiantianai.auth.dto.LoginResponse;
import com.tiantianai.shared.exception.BusinessException;
import com.tiantianai.shared.util.JwtUtil;
import com.tiantianai.user.model.User;
import com.tiantianai.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查账号是否启用
        if (!user.getEnabled()) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoles());

        log.info("用户 {} 登录成功", user.getUsername());

        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }

    /**
     * 获取权限码列表
     */
    @Transactional(readOnly = true)
    public String[] getAccessCodes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        return user.getRoles().toArray(new String[0]);
    }
}
