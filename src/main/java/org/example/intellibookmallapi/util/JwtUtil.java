package org.example.intellibookmallapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和验证JWT Token
 */
@Component
public class JwtUtil {
    
    // 密钥（生产环境应该放在配置文件中）
    private static final String SECRET = "intellibook-mall-secret-key-for-jwt-token-generation-2024-very-long-secret";
    
    // Token有效期：7天（毫秒）
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;
    
    // 生成密钥
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    
    /**
     * 生成JWT Token
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT Token字符串
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 从Token中解析用户ID
     * @param token JWT Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        return (Long) userIdObj;
    }
    
    /**
     * 从Token中解析用户名
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 验证Token是否有效
     * @param token JWT Token
     * @return true-有效 false-无效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取Token的过期时间戳
     * @param token JWT Token
     * @return 过期时间戳（毫秒）
     */
    public long getExpirationTime(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().getTime();
    }
    
    /**
     * 解析Token
     * @param token JWT Token
     * @return Claims对象
     */
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
