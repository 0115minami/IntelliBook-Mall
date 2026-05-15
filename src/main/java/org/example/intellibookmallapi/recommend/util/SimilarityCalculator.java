package org.example.intellibookmallapi.recommend.util;

import org.example.intellibookmallapi.recommend.model.UserBehaviorVector;
import org.example.intellibookmallapi.recommend.model.UserSimilarity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 相似度计算工具类
 * 提供用户相似度计算功能，基于 Jaccard 相似度算法
 */
@Component
public class SimilarityCalculator {
    
    /**
     * 计算两个用户的 Jaccard 相似度（加权）
     * 
     * 前置条件：
     * - user1 和 user2 均非空
     * - 两个用户的行为向量已正确初始化
     * 
     * 后置条件：
     * - 返回值在 [0, 1] 区间内
     * - 当两个用户完全相同时，返回 1.0
     * - 当两个用户完全不同时，返回 0.0
     * 
     * @param user1 用户1的行为向量
     * @param user2 用户2的行为向量
     * @return Jaccard 相似度值，范围 [0, 1]
     */
    public Double calculateJaccardSimilarity(UserBehaviorVector user1, UserBehaviorVector user2) {
        // 前置条件检查
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("用户行为向量不能为空");
        }
        
        // 步骤 1: 构建加权行为集合
        Set<String> set1 = buildWeightedBehaviorSet(user1);
        Set<String> set2 = buildWeightedBehaviorSet(user2);
        
        // 步骤 2: 计算交集大小
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        int intersectionSize = intersection.size();
        
        // 步骤 3: 计算并集大小
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        int unionSize = union.size();
        
        // 步骤 4: 计算 Jaccard 相似度
        if (unionSize == 0) {
            return 0.0;  // 避免除零，两个用户都没有行为数据
        }
        
        double similarity = (double) intersectionSize / unionSize;
        
        // 后置条件检查
        assert similarity >= 0.0 && similarity <= 1.0 : "相似度值必须在 [0, 1] 区间内";
        
        return similarity;
    }
    
    /**
     * 构建加权行为集合（用于 Jaccard 计算）
     * 通过重复添加元素来模拟权重
     * 
     * 权重映射：
     * - 兴趣标签（权重 0.25）→ 重复1次
     * - 收藏（权重 0.35）→ 重复2次
     * - 购买（权重 0.25）→ 重复1次
     * - 评价（权重 0.15）→ 重复1次（只考虑高分评价 4-5星）
     * 
     * @param user 用户行为向量
     * @return 加权行为集合
     */
    public Set<String> buildWeightedBehaviorSet(UserBehaviorVector user) {
        Set<String> weightedSet = new HashSet<>();
        
        // 兴趣标签（权重 0.25 → 重复1次）
        if (user.getInterestCategories() != null) {
            for (Long categoryId : user.getInterestCategories()) {
                weightedSet.add("interest_" + categoryId);
            }
        }
        
        // 收藏（权重 0.35 → 重复2次，权重最高）
        if (user.getFavoriteBooks() != null) {
            for (Long bookId : user.getFavoriteBooks()) {
                weightedSet.add("favorite_" + bookId + "_1");
                weightedSet.add("favorite_" + bookId + "_2");
            }
        }
        
        // 购买（权重 0.25 → 重复1次）
        if (user.getPurchasedBooks() != null) {
            for (Long bookId : user.getPurchasedBooks()) {
                weightedSet.add("purchase_" + bookId);
            }
        }
        
        // 评价（权重 0.15 → 重复1次，只考虑高分评价 4-5星）
        if (user.getReviewedBooks() != null) {
            for (Map.Entry<Long, Integer> entry : user.getReviewedBooks().entrySet()) {
                if (entry.getValue() != null && entry.getValue() >= 4) {
                    weightedSet.add("review_" + entry.getKey());
                }
            }
        }
        
        return weightedSet;
    }
    
    /**
     * 计算目标用户与所有其他用户的相似度
     * 
     * 前置条件：
     * - targetUserId 非空且存在于系统中
     * - allUsers 包含所有用户的行为向量
     * - topK 为正整数
     * 
     * 后置条件：
     * - 返回列表按相似度降序排列
     * - 返回列表长度 ≤ topK
     * - 所有相似度值在 [0, 1] 区间内
     * - 不包含目标用户自身
     * 
     * @param targetUserId 目标用户ID
     * @param targetUser 目标用户的行为向量
     * @param allUsers 所有用户的行为向量列表
     * @param topK 返回前K个最相似用户
     * @return 相似用户列表（按相似度降序）
     */
    public List<UserSimilarity> calculateUserSimilarity(
            Long targetUserId, 
            UserBehaviorVector targetUser, 
            List<UserBehaviorVector> allUsers, 
            Integer topK) {
        
        // 前置条件检查
        if (targetUserId == null) {
            throw new IllegalArgumentException("目标用户ID不能为空");
        }
        if (targetUser == null) {
            throw new IllegalArgumentException("目标用户行为向量不能为空");
        }
        if (allUsers == null || allUsers.isEmpty()) {
            return new ArrayList<>();
        }
        if (topK == null || topK <= 0) {
            throw new IllegalArgumentException("topK 必须为正整数");
        }
        
        List<UserSimilarity> similarities = new ArrayList<>();
        
        // 遍历所有用户，计算相似度
        for (UserBehaviorVector otherUser : allUsers) {
            // 跳过目标用户自身
            if (otherUser.getUserId().equals(targetUserId)) {
                continue;
            }
            
            // 跳过没有行为数据的用户
            if (!otherUser.hasAnyBehavior()) {
                continue;
            }
            
            // 计算相似度
            Double similarity = calculateJaccardSimilarity(targetUser, otherUser);
            
            // 循环不变式：已计算的相似度值均在 [0, 1] 区间内
            assert similarity >= 0.0 && similarity <= 1.0 : "相似度值必须在 [0, 1] 区间内";
            
            // 只保留相似度大于 0 的用户
            if (similarity > 0.0) {
                similarities.add(new UserSimilarity(otherUser.getUserId(), similarity));
            }
        }
        
        // 按相似度降序排序并取前 topK 个
        List<UserSimilarity> topSimilarUsers = similarities.stream()
                .sorted() // UserSimilarity 实现了 Comparable，降序排列
                .limit(topK)
                .collect(Collectors.toList());
        
        // 后置条件检查
        assert topSimilarUsers.size() <= topK : "返回列表长度不能超过 topK";
        assert topSimilarUsers.stream().allMatch(UserSimilarity::isValid) : "所有相似度值必须有效";
        
        return topSimilarUsers;
    }
    
    /**
     * 基于用户的协同过滤推荐（User-CF）
     * 根据相似用户的购买记录生成推荐图书ID列表
     * 
     * 前置条件：
     * - targetUserId 非空且有效
     * - targetUser 已正确构建
     * - allUsers 包含所有用户的行为向量
     * - limit > 0
     * 
     * 后置条件：
     * - 返回列表长度 ≤ limit
     * - 图书按推荐分数降序排列
     * - 不包含目标用户已购买的图书
     * 
     * @param targetUserId 目标用户ID
     * @param targetUser 目标用户的行为向量
     * @param allUsers 所有用户的行为向量列表
     * @param limit 推荐数量限制
     * @return 推荐图书ID列表（按分数降序）
     */
    public List<Long> getUserBasedCFRecommendations(
            Long targetUserId,
            UserBehaviorVector targetUser,
            List<UserBehaviorVector> allUsers,
            Integer limit) {
        
        // 前置条件检查
        if (targetUserId == null || targetUser == null || limit == null || limit <= 0) {
            return new ArrayList<>();
        }
        
        // 步骤 1: 计算与所有用户的相似度（取前50个相似用户）
        List<UserSimilarity> similarUsers = calculateUserSimilarity(targetUserId, targetUser, allUsers, 50);
        
        if (similarUsers.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 步骤 2: 收集相似用户购买过的图书，计算加权分数
        Map<Long, Double> bookScores = new HashMap<>();
        Set<Long> targetUserPurchasedBooks = targetUser.getPurchasedBooks();
        
        for (UserSimilarity simUser : similarUsers) {
            // 循环不变式：所有已处理用户的相似度 ∈ [0, 1]
            assert simUser.getSimilarity() >= 0 && simUser.getSimilarity() <= 1;
            
            // 查找相似用户的行为向量
            UserBehaviorVector similarUserVector = allUsers.stream()
                    .filter(u -> u.getUserId().equals(simUser.getUserId()))
                    .findFirst()
                    .orElse(null);
            
            if (similarUserVector == null) {
                continue;
            }
            
            Set<Long> purchasedBooks = similarUserVector.getPurchasedBooks();
            
            for (Long bookId : purchasedBooks) {
                // 跳过目标用户已购买的图书
                if (targetUserPurchasedBooks.contains(bookId)) {
                    continue;
                }
                
                // 累加加权分数：相似度 × 用户权重
                bookScores.merge(bookId, simUser.getSimilarity(), Double::sum);
            }
        }
        
        // 步骤 3: 按分数降序排序并取前 limit 个
        List<Long> rankedBookIds = bookScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        
        // 后置条件检查
        assert rankedBookIds.size() <= limit;
        
        return rankedBookIds;
    }
}
