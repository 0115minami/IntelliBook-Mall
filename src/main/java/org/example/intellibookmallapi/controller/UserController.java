package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.UserLoginParam;
import org.example.intellibookmallapi.dto.UserRegisterParam;
import org.example.intellibookmallapi.dto.UserVO;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.service.UserService;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterParam param) {
        String message = userService.register(param);
        return ResultGenerator.genSuccessResult(message);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginParam param) {
        String token = userService.login(param.getUsername(), param.getPassword());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        
        return ResultGenerator.genSuccessResult(data);
    }
    
    /**
     * 获取当前用户信息
     * 需要Token验证
     */
    @GetMapping("/info")
    public Result getUserInfo(@TokenToUser User user) {
        // user 由拦截器自动注入
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setEmail(user.getEmail());
        userVO.setIsAdmin(user.getIsAdmin());
        
        return ResultGenerator.genSuccessResult(userVO);
    }
    
    /**
     * 更新用户信息
     * 需要Token验证
     */
    @PutMapping("/info")
    public Result updateUserInfo(@TokenToUser User currentUser, @RequestBody User updateUser) {
        // 只允许更新当前用户自己的信息
        updateUser.setUserId(currentUser.getUserId());
        
        // 不允许通过此接口修改密码
        updateUser.setPassword(null);
        
        Boolean success = userService.updateUserInfo(updateUser);
        if (success) {
            return ResultGenerator.genSuccessResult("更新成功");
        }
        return ResultGenerator.genFailResult("更新失败");
    }
    
    /**
     * 用户登出
     * 将Token加入黑名单，使其失效
     */
    @PostMapping("/logout")
    public Result logout(@TokenToUser User user, @RequestHeader("token") String token) {
        String message = userService.logout(token);
        return ResultGenerator.genSuccessResult(message);
    }
}
