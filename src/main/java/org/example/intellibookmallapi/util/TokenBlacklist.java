package org.example.intellibookmallapi.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token黑名单管理
 * 用于存储已登出的Token，防止登出后继续使用
 * 
 * 注意：这是内存实现，适合单机部署和毕业设计
 * 生产环境建议使用Redis实现分布式黑名单
 */
@Component
public class TokenBlacklist {
    
    /**
     * 黑名单存储
     * Key: Token字符串
     * Value: 过期时间戳
     */
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();
    
    /**
     * 将Token加入黑名单
     * @param token JWT Token
     * @param expirationTime Token过期时间戳（毫秒）
     */
    public void addToBlacklist(String token, long expirationTime) {
        blacklist.put(token, expirationTime);
        
        // 清理已过期的Token（简单实现）
        cleanExpiredTokens();
    }
    
    /**
     * 检查Token是否在黑名单中
     * @param token JWT Token
     * @return true-在黑名单中（已登出），false-不在黑名单中
     */
    public boolean isBlacklisted(String token) {
        if (!blacklist.containsKey(token)) {
            return false;
        }
        
        // 检查是否已过期
        Long expirationTime = blacklist.get(token);
        if (expirationTime == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime > expirationTime) {
            // Token已过期，从黑名单中移除
            blacklist.remove(token);
            return false;
        }
        
        return true;
    }
    
    /**
     * 清理已过期的Token
     * 定期清理可以释放内存
     */
    private void cleanExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        blacklist.entrySet().removeIf(entry -> currentTime > entry.getValue());
    }
    
    /**
     * 获取黑名单大小（用于监控）
     */
    public int size() {
        return blacklist.size();
    }
    
    /**
     * 清空黑名单（用于测试）
     */
    public void clear() {
        blacklist.clear();
    }
}
