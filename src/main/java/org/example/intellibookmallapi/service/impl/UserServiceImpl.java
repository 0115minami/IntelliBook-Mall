package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.UserRegisterParam;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.UserMapper;
import org.example.intellibookmallapi.service.UserService;
import org.example.intellibookmallapi.util.JwtUtil;
import org.example.intellibookmallapi.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    @Transactional
    public String register(UserRegisterParam param) {
        // 验证用户名是否已存在
        User existUser = userMapper.selectByUsername(param.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 验证参数
        if (param.getUsername() == null || param.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (param.getPassword() == null || param.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        if (param.getPassword().length() < 6) {
            throw new BusinessException("密码长度不能少于6位");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(param.getUsername());
        user.setPassword(MD5Util.MD5Encode(param.getPassword(), "UTF-8"));
        user.setEmail(param.getEmail());
        user.setNickname(param.getNickname() != null ? param.getNickname() : param.getUsername());
        user.setIsAdmin(0);
        user.setCreateTime(new Date());
        
        int result = userMapper.insert(user);
        if (result > 0) {
            return "注册成功";
        }
        throw new BusinessException("注册失败");
    }
    
    @Override
    public String login(String username, String password) {
        // 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证密码
        String encryptedPassword = MD5Util.MD5Encode(password, "UTF-8");
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new BusinessException("密码错误");
        }
        
        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());
        
        return token;
    }
    
    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }
    
    @Override
    @Transactional
    public Boolean updateUserInfo(User user) {
        if (user.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        
        User existUser = userMapper.selectByPrimaryKey(user.getUserId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        int result = userMapper.updateByPrimaryKeySelective(user);
        return result > 0;
    }
    
    @Autowired
    private org.example.intellibookmallapi.util.TokenBlacklist tokenBlacklist;
    
    @Override
    public String logout(String token) {
        // 获取Token的过期时间
        long expirationTime = jwtUtil.getExpirationTime(token);
        
        // 将Token加入黑名单
        tokenBlacklist.addToBlacklist(token, expirationTime);
        
        return "登出成功";
    }
}
