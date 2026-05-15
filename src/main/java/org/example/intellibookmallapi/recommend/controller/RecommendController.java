package org.example.intellibookmallapi.recommend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.recommend.service.RecommendService;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐控制器
 * 提供个性化图书推荐接口
 */
@Slf4j
@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {
    
    private final RecommendService recommendService;
    
    /**
     * 获取个性化推荐
     * 
     * @param user 当前登录用户（通过 @TokenToUser 注解自动注入）
     * @param limit 推荐数量限制（默认10，范围 [1, 100]）
     * @return 推荐图书列表
     */
    @GetMapping
    public Result<List<EBook>> getRecommendations(
            @TokenToUser User user,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        // 参数校验
        if (limit == null || limit < 1) {
            return ResultGenerator.genFailResult("推荐数量必须大于0");
        }
        if (limit > 100) {
            return ResultGenerator.genFailResult("推荐数量不能超过100");
        }
        
        try {
            log.info("用户 {} 请求推荐，数量: {}", user.getUserId(), limit);
            
            // 调用推荐服务
            List<EBook> recommendations = recommendService.getRecommendations(user.getUserId(), limit);
            
            log.info("为用户 {} 生成了 {} 条推荐", user.getUserId(), recommendations.size());
            
            return ResultGenerator.genSuccessResult(recommendations);
            
        } catch (Exception e) {
            log.error("生成推荐时发生错误，用户ID: {}", user.getUserId(), e);
            return ResultGenerator.genFailResult("生成推荐失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取冷启动推荐（热门图书）
     * 不需要登录即可访问
     * 
     * @param limit 推荐数量限制（默认10，范围 [1, 100]）
     * @return 热门图书列表
     */
    @GetMapping("/hot")
    public Result<List<EBook>> getHotRecommendations(
            @RequestParam(defaultValue = "10") Integer limit) {
        
        // 参数校验
        if (limit == null || limit < 1) {
            return ResultGenerator.genFailResult("推荐数量必须大于0");
        }
        if (limit > 100) {
            return ResultGenerator.genFailResult("推荐数量不能超过100");
        }
        
        try {
            log.info("请求热门推荐，数量: {}", limit);
            
            // 调用冷启动推荐服务
            List<EBook> recommendations = recommendService.getColdStartRecommendations(limit);
            
            log.info("生成了 {} 条热门推荐", recommendations.size());
            
            return ResultGenerator.genSuccessResult(recommendations);
            
        } catch (Exception e) {
            log.error("生成热门推荐时发生错误", e);
            return ResultGenerator.genFailResult("生成推荐失败：" + e.getMessage());
        }
    }
}
