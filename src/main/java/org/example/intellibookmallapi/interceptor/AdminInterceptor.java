package org.example.intellibookmallapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.intellibookmallapi.annotation.RequireAdmin;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器
 * 验证用户是否具有管理员权限
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        // 只拦截Controller方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod method = (HandlerMethod) handler;
        
        // 检查方法是否有@RequireAdmin注解
        RequireAdmin annotation = method.getMethodAnnotation(RequireAdmin.class);
        
        if (annotation != null) {
            // 从request获取当前用户（由JwtInterceptor注入）
            User currentUser = (User) request.getAttribute("currentUser");
            
            if (currentUser == null) {
                throw new BusinessException("请先登录");
            }
            
            // 验证是否为管理员
            if (currentUser.getIsAdmin() == null || currentUser.getIsAdmin() != 1) {
                throw new BusinessException("需要管理员权限");
            }
        }
        
        return true;
    }
}
