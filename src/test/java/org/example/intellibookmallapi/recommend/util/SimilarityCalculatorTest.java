package org.example.intellibookmallapi.recommend.util;

import org.example.intellibookmallapi.recommend.model.UserBehaviorVector;
import org.example.intellibookmallapi.recommend.model.UserSimilarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SimilarityCalculator 单元测试
 * 验证相似度计算的正确性
 */
class SimilarityCalculatorTest {
    
    private SimilarityCalculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new SimilarityCalculator();
    }
    
    /**
     * 测试：两个完全相同的用户，相似度应为 1.0
     */
    @Test
    void testCalculateJaccardSimilarity_IdenticalUsers_ShouldReturn1() {
        // 准备测试数据
        UserBehaviorVector user1 = new UserBehaviorVector(1L);
        user1.getInterestCategories().add(1L);
        user1.getFavoriteBooks().add(101L);
        user1.getPurchasedBooks().add(201L);
        user1.getReviewedBooks().put(301L, 5);
        
        UserBehaviorVector user2 = new UserBehaviorVector(2L);
        user2.getInterestCategories().add(1L);
        user2.getFavoriteBooks().add(101L);
        user2.getPurchasedBooks().add(201L);
        user2.getReviewedBooks().put(301L, 5);
        
        // 执行测试
        Double similarity = calculator.calculateJaccardSimilarity(user1, user2);
        
        // 验证结果
        assertNotNull(similarity);
        assertEquals(1.0, similarity, 0.001, "完全相同的用户相似度应为 1.0");
    }
    
    /**
     * 测试：两个完全不同的用户，相似度应为 0.0
     */
    @Test
    void testCalculateJaccardSimilarity_DifferentUsers_ShouldReturn0() {
        // 准备测试数据
        UserBehaviorVector user1 = new UserBehaviorVector(1L);
        user1.getInterestCategories().add(1L);
        user1.getFavoriteBooks().add(101L);
        
        UserBehaviorVector user2 = new UserBehaviorVector(2L);
        user2.getInterestCategories().add(2L);
        user2.getFavoriteBooks().add(102L);
        
        // 执行测试
        Double similarity = calculator.calculateJaccardSimilarity(user1, user2);
        
        // 验证结果
        assertNotNull(similarity);
        assertEquals(0.0, similarity, 0.001, "完全不同的用户相似度应为 0.0");
    }
    
    /**
     * 测试：两个用户有部分重叠，相似度应在 (0, 1) 之间
     */
    @Test
    void testCalculateJaccardSimilarity_PartialOverlap_ShouldReturnBetween0And1() {
        // 准备测试数据
        UserBehaviorVector user1 = new UserBehaviorVector(1L);
        user1.getInterestCategories().add(1L);
        user1.getFavoriteBooks().add(101L);
        user1.getFavoriteBooks().add(102L);
        
        UserBehaviorVector user2 = new UserBehaviorVector(2L);
        user2.getInterestCategories().add(1L);
        user2.getFavoriteBooks().add(101L);
        user2.getFavoriteBooks().add(103L);
        
        // 执行测试
        Double similarity = calculator.calculateJaccardSimilarity(user1, user2);
        
        // 验证结果
        assertNotNull(similarity);
        assertTrue(similarity > 0.0 && similarity < 1.0, 
                "部分重叠的用户相似度应在 (0, 1) 之间，实际值: " + similarity);
    }
    
    /**
     * 测试：两个空用户（无行为数据），相似度应为 0.0
     */
    @Test
    void testCalculateJaccardSimilarity_EmptyUsers_ShouldReturn0() {
        // 准备测试数据
        UserBehaviorVector user1 = new UserBehaviorVector(1L);
        UserBehaviorVector user2 = new UserBehaviorVector(2L);
        
        // 执行测试
        Double similarity = calculator.calculateJaccardSimilarity(user1, user2);
        
        // 验证结果
        assertNotNull(similarity);
        assertEquals(0.0, similarity, 0.001, "空用户相似度应为 0.0");
    }
    
    /**
     * 测试：相似度值必须在 [0, 1] 区间内
     */
    @Test
    void testCalculateJaccardSimilarity_ShouldAlwaysReturnValidRange() {
        // 准备多组测试数据
        List<UserBehaviorVector> users = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            UserBehaviorVector user = new UserBehaviorVector((long) i);
            user.getInterestCategories().add((long) (i % 3 + 1));
            user.getFavoriteBooks().add((long) (100 + i % 5));
            user.getPurchasedBooks().add((long) (200 + i % 4));
            users.add(user);
        }
        
        // 执行测试：计算所有用户对之间的相似度
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                Double similarity = calculator.calculateJaccardSimilarity(users.get(i), users.get(j));
                
                // 验证结果
                assertNotNull(similarity);
                assertTrue(similarity >= 0.0 && similarity <= 1.0, 
                        "相似度必须在 [0, 1] 区间内，实际值: " + similarity);
            }
        }
    }
    
    /**
     * 测试：空参数应抛出异常
     */
    @Test
    void testCalculateJaccardSimilarity_NullUser_ShouldThrowException() {
        UserBehaviorVector user = new UserBehaviorVector(1L);
        
        assertThrows(IllegalArgumentException.class, 
                () -> calculator.calculateJaccardSimilarity(null, user),
                "user1 为空应抛出异常");
        
        assertThrows(IllegalArgumentException.class, 
                () -> calculator.calculateJaccardSimilarity(user, null),
                "user2 为空应抛出异常");
    }
    
    /**
     * 测试：加权行为集合构建
     */
    @Test
    void testBuildWeightedBehaviorSet_ShouldApplyCorrectWeights() {
        // 准备测试数据
        UserBehaviorVector user = new UserBehaviorVector(1L);
        user.getInterestCategories().add(1L);
        user.getFavoriteBooks().add(101L);
        user.getPurchasedBooks().add(201L);
        user.getReviewedBooks().put(301L, 5); // 高分评价
        user.getReviewedBooks().put(302L, 3); // 低分评价，应被过滤
        
        // 执行测试
        Set<String> weightedSet = calculator.buildWeightedBehaviorSet(user);
        
        // 验证结果
        assertNotNull(weightedSet);
        assertTrue(weightedSet.contains("interest_1"), "应包含兴趣标签");
        assertTrue(weightedSet.contains("favorite_101_1"), "应包含收藏（权重1）");
        assertTrue(weightedSet.contains("favorite_101_2"), "应包含收藏（权重2）");
        assertTrue(weightedSet.contains("purchase_201"), "应包含购买");
        assertTrue(weightedSet.contains("review_301"), "应包含高分评价");
        assertFalse(weightedSet.contains("review_302"), "不应包含低分评价");
    }
    
    /**
     * 测试：计算用户相似度列表
     */
    @Test
    void testCalculateUserSimilarity_ShouldReturnTopKSimilarUsers() {
        // 准备目标用户
        UserBehaviorVector targetUser = new UserBehaviorVector(1L);
        targetUser.getInterestCategories().add(1L);
        targetUser.getFavoriteBooks().add(101L);
        
        // 准备其他用户
        List<UserBehaviorVector> allUsers = new ArrayList<>();
        allUsers.add(targetUser); // 包含目标用户自身
        
        // 用户2：高相似度
        UserBehaviorVector user2 = new UserBehaviorVector(2L);
        user2.getInterestCategories().add(1L);
        user2.getFavoriteBooks().add(101L);
        allUsers.add(user2);
        
        // 用户3：中等相似度
        UserBehaviorVector user3 = new UserBehaviorVector(3L);
        user3.getInterestCategories().add(1L);
        allUsers.add(user3);
        
        // 用户4：低相似度
        UserBehaviorVector user4 = new UserBehaviorVector(4L);
        user4.getInterestCategories().add(2L);
        allUsers.add(user4);
        
        // 执行测试
        List<UserSimilarity> similarities = calculator.calculateUserSimilarity(
                1L, targetUser, allUsers, 10);
        
        // 验证结果
        assertNotNull(similarities);
        assertTrue(similarities.size() <= 10, "返回列表长度不应超过 topK");
        assertFalse(similarities.stream().anyMatch(s -> s.getUserId().equals(1L)), 
                "不应包含目标用户自身");
        
        // 验证降序排列
        for (int i = 0; i < similarities.size() - 1; i++) {
            assertTrue(similarities.get(i).getSimilarity() >= similarities.get(i + 1).getSimilarity(),
                    "相似度列表应按降序排列");
        }
        
        // 验证所有相似度值有效
        assertTrue(similarities.stream().allMatch(UserSimilarity::isValid),
                "所有相似度值应有效");
    }
    
    /**
     * 测试：UserBehaviorVector 的 hasAnyBehavior 方法
     */
    @Test
    void testUserBehaviorVector_HasAnyBehavior() {
        // 空用户
        UserBehaviorVector emptyUser = new UserBehaviorVector(1L);
        assertFalse(emptyUser.hasAnyBehavior(), "空用户应返回 false");
        
        // 有兴趣的用户
        UserBehaviorVector userWithInterest = new UserBehaviorVector(2L);
        userWithInterest.getInterestCategories().add(1L);
        assertTrue(userWithInterest.hasAnyBehavior(), "有兴趣的用户应返回 true");
        
        // 有收藏的用户
        UserBehaviorVector userWithFavorite = new UserBehaviorVector(3L);
        userWithFavorite.getFavoriteBooks().add(101L);
        assertTrue(userWithFavorite.hasAnyBehavior(), "有收藏的用户应返回 true");
    }
    
    /**
     * 测试：UserSimilarity 的排序功能
     */
    @Test
    void testUserSimilarity_Sorting() {
        List<UserSimilarity> similarities = new ArrayList<>();
        similarities.add(new UserSimilarity(1L, 0.3));
        similarities.add(new UserSimilarity(2L, 0.8));
        similarities.add(new UserSimilarity(3L, 0.5));
        
        Collections.sort(similarities);
        
        assertEquals(2L, similarities.get(0).getUserId(), "最高相似度应排在第一位");
        assertEquals(3L, similarities.get(1).getUserId(), "中等相似度应排在第二位");
        assertEquals(1L, similarities.get(2).getUserId(), "最低相似度应排在第三位");
    }
}
