package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.UserRegisterParam;
import org.example.intellibookmallapi.entity.User;

/**
 * 用户Service接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param param 注册参数
     * @return 注册结果消息
     */
    String register(UserRegisterParam param);
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return JWT Token
     */
    String login(String username, String password);
    
    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否成功
     */
    Boolean updateUserInfo(User user);
    
    /**
     * 用户登出
     * @param token JWT Token
     * @return 登出结果消息
     */
    String logout(String token);
}
