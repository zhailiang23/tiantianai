package com.tiantianai.user.service;

import com.tiantianai.shared.exception.BusinessException;
import com.tiantianai.user.dto.UserInfoResponse;
import com.tiantianai.user.model.User;
import com.tiantianai.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 获取用户信息
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .homePath(user.getHomePath())
                .roles(user.getRoles())
                .build();
    }
}
