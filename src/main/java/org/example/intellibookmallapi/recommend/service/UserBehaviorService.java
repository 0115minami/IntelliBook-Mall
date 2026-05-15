package org.example.intellibookmallapi.recommend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.intellibookmallapi.entity.Review;
import org.example.intellibookmallapi.mapper.FavoriteMapper;
import org.example.intellibookmallapi.mapper.OrderItemMapper;
import org.example.intellibookmallapi.mapper.ReviewMapper;
import org.example.intellibookmallapi.mapper.UserInterestMapper;
import org.example.intellibookmallapi.recommend.model.UserBehaviorVector;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户行为数据服务
 * 负责查询和构建用户行为向量，用于推荐系统的相似度计算
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserBehaviorService {
    
    private final UserInterestMapper userInterestMapper;
    private final FavoriteMapper favoriteMapper;
    private final OrderItemMapper orderItemMapper;
    private final ReviewMapper reviewMapper;
    
    /**
     * 构建用户行为向量
     * 整合用户的兴趣分类、收藏、购买、评价四个维度的数据
     * 
     * @param userId 用户ID
     * @return 用户行为向量
     */
    public UserBehaviorVector buildUserBehaviorVector(Long userId) {
        if (userId == null) {
            log.warn("用户ID为空，无法构建行为向量");
            return new UserBehaviorVector();
        }
        
        log.debug("开始构建用户 {} 的行为向量", userId);
        
        // 创建用户行为向量对象
        UserBehaviorVector vector = new UserBehaviorVector(userId);
        
        try {
            // 1. 查询用户的兴趣分类ID
            List<Long> categoryIds = userInterestMapper.selectCategoryIdsByUserId(userId);
            Set<Long> interestCategories = new HashSet<>(categoryIds);
            vector.setInterestCategories(interestCategories);
            log.debug("用户 {} 的兴趣分类数量: {}", userId, interestCategories.size());
            
            // 2. 查询用户收藏的图书ID
            List<Long> favoriteBookIds = favoriteMapper.selectBookIdsByUserId(userId);
            Set<Long> favoriteBooks = new HashSet<>(favoriteBookIds);
            vector.setFavoriteBooks(favoriteBooks);
            log.debug("用户 {} 的收藏图书数量: {}", userId, favoriteBooks.size());
            
            // 3. 查询用户购买的图书ID（只查询已支付订单）
            List<Long> purchasedBookIds = orderItemMapper.selectPurchasedBookIdsByUserId(userId);
            Set<Long> purchasedBooks = new HashSet<>(purchasedBookIds);
            vector.setPurchasedBooks(purchasedBooks);
            log.debug("用户 {} 的购买图书数量: {}", userId, purchasedBooks.size());
            
            // 4. 查询用户评价过的图书及评分
            List<Review> reviews = reviewMapper.selectReviewedBooksWithRatingByUserId(userId);
            Map<Long, Integer> reviewedBooks = new HashMap<>();
            for (Review review : reviews) {
                reviewedBooks.put(review.getBookId(), review.getRating());
            }
            vector.setReviewedBooks(reviewedBooks);
            log.debug("用户 {} 的评价图书数量: {}", userId, reviewedBooks.size());
            
            log.info("成功构建用户 {} 的行为向量，总行为数: {}", userId, vector.getTotalBehaviorCount());
            
        } catch (Exception e) {
            log.error("构建用户 {} 的行为向量时发生错误", userId, e);
            // 返回空的行为向量，避免影响后续流程
            return new UserBehaviorVector(userId);
        }
        
        return vector;
    }
    
    /**
     * 判断用户是否为新用户（无任何行为数据）
     * 
     * @param userId 用户ID
     * @return true 如果用户没有任何行为数据
     */
    public boolean isNewUser(Long userId) {
        if (userId == null) {
            return true;
        }
        
        UserBehaviorVector vector = buildUserBehaviorVector(userId);
        return !vector.hasAnyBehavior();
    }
    
    /**
     * 批量构建多个用户的行为向量
     * 
     * @param userIds 用户ID列表
     * @return 用户ID到行为向量的映射
     */
    public Map<Long, UserBehaviorVector> buildUserBehaviorVectors(List<Long> userIds) {
        Map<Long, UserBehaviorVector> vectors = new HashMap<>();
        
        if (userIds == null || userIds.isEmpty()) {
            return vectors;
        }
        
        for (Long userId : userIds) {
            UserBehaviorVector vector = buildUserBehaviorVector(userId);
            vectors.put(userId, vector);
        }
        
        log.info("批量构建了 {} 个用户的行为向量", vectors.size());
        return vectors;
    }
}
