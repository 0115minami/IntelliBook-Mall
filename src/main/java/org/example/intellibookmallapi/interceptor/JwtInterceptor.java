package org.example.intellibookmallapi.interceptor;

import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.UserMapper;
import org.example.intellibookmallapi.util.JwtUtil;
import org.example.intellibookmallapi.util.TokenBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 * 拦截需要Token验证的请求
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TokenBlacklist tokenBlacklist;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 只拦截Controller方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        // 从Header获取Token
        // 支持两种格式：
        // 1. token: {token}
        // 2. Authorization: Bearer {token}
        String token = request.getHeader("token");
        
        if (token == null || token.isEmpty()) {
            // 尝试从Authorization头获取
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // 移除 "Bearer " 前缀
            }
        }
        
        if (token == null || token.isEmpty()) {
            throw new BusinessException("请先登录");
        }
        
        // 检查Token是否在黑名单中（已登出）
        if (tokenBlacklist.isBlacklisted(token)) {
            throw new BusinessException("Token已失效，请重新登录");
        }
        
        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException("Token无效或已过期，请重新登录");
        }
        
        // 从Token中解析用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        // 查询用户信息
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 将用户信息和Token注入到Request，供Controller使用
        request.setAttribute("currentUser", user);
        request.setAttribute("currentToken", token);
        
        return true;
    }
}
