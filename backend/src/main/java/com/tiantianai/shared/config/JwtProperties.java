package com.tiantianai.shared.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 签名密钥
     */
    private String secret = "tiantianai-secret-key-please-change-in-production-environment";

    /**
     * JWT 过期时间（毫秒）
     * 默认 24 小时
     */
    private Long expiration = 24 * 60 * 60 * 1000L;

    /**
     * Token 前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Token 请求头名称
     */
    private String headerName = "Authorization";
}
