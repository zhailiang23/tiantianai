package com.tiantianai.shared.config;

import com.tiantianai.user.model.User;
import com.tiantianai.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 数据初始化
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 创建测试用户
        initTestUsers();
    }

    private void initTestUsers() {
        // 检查是否已有用户
        if (userRepository.count() > 0) {
            log.info("用户数据已存在，跳过初始化");
            return;
        }

        // 创建管理员用户
        User admin = User.builder()
                .username("vben")
                .password(passwordEncoder.encode("123456"))
                .realName("Vben Admin")
                .homePath("/dashboard")
                .roles(Set.of("admin", "user"))
                .enabled(true)
                .build();

        // 创建普通管理员
        User manager = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .realName("Administrator")
                .homePath("/dashboard")
                .roles(Set.of("admin"))
                .enabled(true)
                .build();

        // 创建普通用户
        User user = User.builder()
                .username("jack")
                .password(passwordEncoder.encode("123456"))
                .realName("Jack")
                .homePath("/dashboard")
                .roles(Set.of("user"))
                .enabled(true)
                .build();

        userRepository.save(admin);
        userRepository.save(manager);
        userRepository.save(user);

        log.info("测试用户初始化完成");
        log.info("用户名: vben, 密码: 123456, 角色: admin, user");
        log.info("用户名: admin, 密码: 123456, 角色: admin");
        log.info("用户名: jack, 密码: 123456, 角色: user");
    }
}
