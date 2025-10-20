package com.tiantianai.shared.security;

import com.tiantianai.shared.config.JwtProperties;
import com.tiantianai.shared.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从请求头中获取 JWT Token
            String token = getTokenFromRequest(request);

            // 验证 Token
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                // 从 Token 中提取用户信息
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                Set<String> roles = jwtUtil.getRolesFromToken(token);

                // 转换角色为权限
                var authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                // 创建认证对象
                // 使用 userId 作为 principal，方便后续获取
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId.toString(), // 使用 userId 字符串作为 principal
                                null,
                                authorities
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置到安全上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("JWT 认证成功，用户: {}", username);
            }
        } catch (Exception e) {
            log.error("JWT 认证失败: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取 Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeaderName());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }
}
