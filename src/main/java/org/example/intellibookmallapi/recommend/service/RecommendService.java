package org.example.intellibookmallapi.recommend.service;

import org.example.intellibookmallapi.entity.EBook;

import java.util.List;

/**
 * 推荐服务接口
 * 提供个性化图书推荐功能
 */
public interface RecommendService {
    
    /**
     * 获取用户个性化推荐
     * 
     * 前置条件：
     * - userId 非空且存在于系统中
     * - limit 为正整数，范围 [1, 100]
     * 
     * 后置条件：
     * - 返回的图书列表长度 ≤ limit
     * - 所有返回的图书状态为上架（status = 1）且未删除（isDeleted = 0）
     * - 如果用户有历史行为数据，返回协同过滤推荐；否则返回冷启动推荐
     * - 推荐结果不包含用户已购买的图书
     * 
     * @param userId 用户ID
     * @param limit 推荐数量限制
     * @return 推荐图书列表
     */
    List<EBook> getRecommendations(Long userId, Integer limit);
    
    /**
     * 获取基于协同过滤的推荐（User-CF + Item-CF）
     * 
     * @param userId 用户ID
     * @param limit 推荐数量限制
     * @return 推荐图书列表
     */
    List<EBook> getCollaborativeFilteringRecommendations(Long userId, Integer limit);
    
    /**
     * 获取冷启动推荐（热度推荐）
     * 
     * @param limit 推荐数量限制
     * @return 热门图书列表
     */
    List<EBook> getColdStartRecommendations(Integer limit);
}
