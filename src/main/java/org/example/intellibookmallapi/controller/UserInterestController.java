package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.AddInterestParam;
import org.example.intellibookmallapi.dto.BatchAddInterestParam;
import org.example.intellibookmallapi.dto.UserInterestVO;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.entity.UserInterest;
import org.example.intellibookmallapi.service.UserInterestService;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户兴趣Controller
 */
@RestController
@RequestMapping("/api/user/interests")
public class UserInterestController {
    
    @Autowired
    private UserInterestService userInterestService;
    
    @Autowired
    private org.example.intellibookmallapi.service.CategoryService categoryService;
    
    /**
     * 获取用户兴趣列表
     * @param user 当前登录用户
     * @return 兴趣列表
     */
    @GetMapping
    public Result getUserInterests(@TokenToUser User user) {
        Long userId = user.getUserId();
        List<UserInterest> interests = userInterestService.getUserInterests(userId);
        
        // 转换为VO
        List<UserInterestVO> voList = new ArrayList<>();
        for (UserInterest interest : interests) {
            UserInterestVO vo = new UserInterestVO();
            BeanUtils.copyProperties(interest, vo);
            voList.add(vo);
        }
        
        return ResultGenerator.genSuccessResult(voList);
    }
    
    /**
     * 添加兴趣
     * @param param 添加参数
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping
    public Result addInterest(@RequestBody AddInterestParam param,
                             @TokenToUser User user) {
        if (param.getCategoryId() == null) {
            return ResultGenerator.genFailResult("分类ID不能为空");
        }
        
        Long userId = user.getUserId();
        boolean success = userInterestService.addInterest(userId, param.getCategoryId());
        
        if (success) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }
    }
    
    /**
     * 批量添加兴趣
     * @param param 批量添加参数
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping("/batch")
    public Result batchAddInterests(@RequestBody BatchAddInterestParam param,
                                   @TokenToUser User user) {
        if (param.getCategoryIds() == null || param.getCategoryIds().isEmpty()) {
            return ResultGenerator.genFailResult("分类ID列表不能为空");
        }
        
        Long userId = user.getUserId();
        int count = userInterestService.batchAddInterests(userId, param.getCategoryIds());
        
        Map<String, Object> data = new HashMap<>();
        data.put("addedCount", count);
        
        return ResultGenerator.genSuccessResult(data);
    }
    
    /**
     * 删除兴趣
     * @param categoryId 分类ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @DeleteMapping("/{categoryId}")
    public Result deleteInterest(@PathVariable("categoryId") Long categoryId,
                                 @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = userInterestService.deleteInterest(userId, categoryId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("删除成功");
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    
    /**
     * 检查兴趣状态
     * @param categoryId 分类ID
     * @param user 当前登录用户
     * @return 是否已添加
     */
    @GetMapping("/check/{categoryId}")
    public Result checkInterest(@PathVariable("categoryId") Long categoryId,
                               @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean hasInterest = userInterestService.checkInterest(userId, categoryId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("hasInterest", hasInterest);
        
        return ResultGenerator.genSuccessResult(data);
    }
    
    /**
     * 获取推荐的兴趣分类（用于注册后引导）
     * @return 推荐分类树（一级分类+子分类）
     */
    @GetMapping("/recommended-categories")
    public Result getRecommendedCategories() {
        // 获取完整的分类树结构
        List<org.example.intellibookmallapi.entity.Category> categoryTree = categoryService.getCategoryTree();
        
        return ResultGenerator.genSuccessResult(categoryTree);
    }
}
