package com.tiantianai.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名（登录账号）
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(nullable = false)
    private String password;

    /**
     * 真实姓名
     */
    @Column(length = 50)
    private String realName;

    /**
     * 用户首页路径
     */
    @Column(length = 200)
    private String homePath;

    /**
     * 用户角色（权限码集合）
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    /**
     * 账号是否启用
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
