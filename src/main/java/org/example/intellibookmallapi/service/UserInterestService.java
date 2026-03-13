package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.entity.UserInterest;

import java.util.List;

/**
 * 用户兴趣Service接口
 */
public interface UserInterestService {
    
    /**
     * 添加用户兴趣
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean addInterest(Long userId, Long categoryId);
    
    /**
     * 批量添加用户兴趣
     * @param userId 用户ID
     * @param categoryIds 分类ID列表
     * @return 成功添加的数量
     */
    int batchAddInterests(Long userId, List<Long> categoryIds);
    
    /**
     * 删除用户兴趣
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean deleteInterest(Long userId, Long categoryId);
    
    /**
     * 获取用户的所有兴趣列表
     * @param userId 用户ID
     * @return 兴趣列表
     */
    List<UserInterest> getUserInterests(Long userId);
    
    /**
     * 检查用户是否已添加某个分类为兴趣
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @return 是否已添加
     */
    boolean checkInterest(Long userId, Long categoryId);
    
    /**
     * 获取用户兴趣数量
     * @param userId 用户ID
     * @return 兴趣数量
     */
    int getInterestCount(Long userId);
}
