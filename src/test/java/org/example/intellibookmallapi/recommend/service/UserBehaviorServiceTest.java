package org.example.intellibookmallapi.recommend.service;

import org.example.intellibookmallapi.entity.Review;
import org.example.intellibookmallapi.mapper.FavoriteMapper;
import org.example.intellibookmallapi.mapper.OrderItemMapper;
import org.example.intellibookmallapi.mapper.ReviewMapper;
import org.example.intellibookmallapi.mapper.UserInterestMapper;
import org.example.intellibookmallapi.recommend.model.UserBehaviorVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * UserBehaviorService 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户行为服务测试")
class UserBehaviorServiceTest {
    
    @Mock
    private UserInterestMapper userInterestMapper;
    
    @Mock
    private FavoriteMapper favoriteMapper;
    
    @Mock
    private OrderItemMapper orderItemMapper;
    
    @Mock
    private ReviewMapper reviewMapper;
    
    @InjectMocks
    private UserBehaviorService userBehaviorService;
    
    private Long testUserId;
    
    @BeforeEach
    void setUp() {
        testUserId = 1L;
    }
    
    @Test
    @DisplayName("构建用户行为向量 - 正常情况")
    void testBuildUserBehaviorVector_Success() {
        // 准备测试数据
        List<Long> categoryIds = Arrays.asList(1L, 2L, 3L);
        List<Long> favoriteBookIds = Arrays.asList(10L, 20L, 30L);
        List<Long> purchasedBookIds = Arrays.asList(40L, 50L);
        
        Review review1 = new Review();
        review1.setBookId(60L);
        review1.setRating(5);
        
        Review review2 = new Review();
        review2.setBookId(70L);
        review2.setRating(4);
        
        List<Review> reviews = Arrays.asList(review1, review2);
        
        // 配置 Mock 行为
        when(userInterestMapper.selectCategoryIdsByUserId(testUserId)).thenReturn(categoryIds);
        when(favoriteMapper.selectBookIdsByUserId(testUserId)).thenReturn(favoriteBookIds);
        when(orderItemMapper.selectPurchasedBookIdsByUserId(testUserId)).thenReturn(purchasedBookIds);
        when(reviewMapper.selectReviewedBooksWithRatingByUserId(testUserId)).thenReturn(reviews);
        
        // 执行测试
        UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(testUserId);
        
        // 验证结果
        assertNotNull(vector);
        assertEquals(testUserId, vector.getUserId());
        assertEquals(3, vector.getInterestCategories().size());
        assertEquals(3, vector.getFavoriteBooks().size());
        assertEquals(2, vector.getPurchasedBooks().size());
        assertEquals(2, vector.getReviewedBooks().size());
        assertTrue(vector.hasAnyBehavior());
        assertEquals(10, vector.getTotalBehaviorCount());
        
        // 验证 Mapper 方法被调用
        verify(userInterestMapper, times(1)).selectCategoryIdsByUserId(testUserId);
        verify(favoriteMapper, times(1)).selectBookIdsByUserId(testUserId);
        verify(orderItemMapper, times(1)).selectPurchasedBookIdsByUserId(testUserId);
        verify(reviewMapper, times(1)).selectReviewedBooksWithRatingByUserId(testUserId);
    }
    
    @Test
    @DisplayName("构建用户行为向量 - 用户ID为空")
    void testBuildUserBehaviorVector_NullUserId() {
        // 执行测试
        UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(null);
        
        // 验证结果
        assertNotNull(vector);
        assertNull(vector.getUserId());
        assertFalse(vector.hasAnyBehavior());
        assertEquals(0, vector.getTotalBehaviorCount());
        
        // 验证 Mapper 方法未被调用
        verify(userInterestMapper, never()).selectCategoryIdsByUserId(anyLong());
        verify(favoriteMapper, never()).selectBookIdsByUserId(anyLong());
        verify(orderItemMapper, never()).selectPurchasedBookIdsByUserId(anyLong());
        verify(reviewMapper, never()).selectReviewedBooksWithRatingByUserId(anyLong());
    }
    
    @Test
    @DisplayName("构建用户行为向量 - 新用户无数据")
    void testBuildUserBehaviorVector_NewUser() {
        // 配置 Mock 行为 - 返回空列表
        when(userInterestMapper.selectCategoryIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(favoriteMapper.selectBookIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(orderItemMapper.selectPurchasedBookIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(reviewMapper.selectReviewedBooksWithRatingByUserId(testUserId)).thenReturn(Arrays.asList());
        
        // 执行测试
        UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(testUserId);
        
        // 验证结果
        assertNotNull(vector);
        assertEquals(testUserId, vector.getUserId());
        assertFalse(vector.hasAnyBehavior());
        assertEquals(0, vector.getTotalBehaviorCount());
    }
    
    @Test
    @DisplayName("判断是否为新用户 - 有行为数据")
    void testIsNewUser_HasBehavior() {
        // 配置 Mock 行为
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        when(userInterestMapper.selectCategoryIdsByUserId(testUserId)).thenReturn(categoryIds);
        when(favoriteMapper.selectBookIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(orderItemMapper.selectPurchasedBookIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(reviewMapper.selectReviewedBooksWithRatingByUserId(testUserId)).thenReturn(Arrays.asList());
        
        // 执行测试
        boolean isNew = userBehaviorService.isNewUser(testUserId);
        
        // 验证结果
        assertFalse(isNew);
    }
    
    @Test
    @DisplayName("判断是否为新用户 - 无行为数据")
    void testIsNewUser_NoBehavior() {
        // 配置 Mock 行为 - 返回空列表
        when(userInterestMapper.selectCategoryIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(favoriteMapper.selectBookIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(orderItemMapper.selectPurchasedBookIdsByUserId(testUserId)).thenReturn(Arrays.asList());
        when(reviewMapper.selectReviewedBooksWithRatingByUserId(testUserId)).thenReturn(Arrays.asList());
        
        // 执行测试
        boolean isNew = userBehaviorService.isNewUser(testUserId);
        
        // 验证结果
        assertTrue(isNew);
    }
    
    @Test
    @DisplayName("判断是否为新用户 - 用户ID为空")
    void testIsNewUser_NullUserId() {
        // 执行测试
        boolean isNew = userBehaviorService.isNewUser(null);
        
        // 验证结果
        assertTrue(isNew);
    }
    
    @Test
    @DisplayName("批量构建用户行为向量 - 正常情况")
    void testBuildUserBehaviorVectors_Success() {
        // 准备测试数据
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        
        // 配置 Mock 行为
        when(userInterestMapper.selectCategoryIdsByUserId(anyLong())).thenReturn(Arrays.asList(1L));
        when(favoriteMapper.selectBookIdsByUserId(anyLong())).thenReturn(Arrays.asList(10L));
        when(orderItemMapper.selectPurchasedBookIdsByUserId(anyLong())).thenReturn(Arrays.asList(20L));
        when(reviewMapper.selectReviewedBooksWithRatingByUserId(anyLong())).thenReturn(Arrays.asList());
        
        // 执行测试
        Map<Long, UserBehaviorVector> vectors = userBehaviorService.buildUserBehaviorVectors(userIds);
        
        // 验证结果
        assertNotNull(vectors);
        assertEquals(3, vectors.size());
        assertTrue(vectors.containsKey(1L));
        assertTrue(vectors.containsKey(2L));
        assertTrue(vectors.containsKey(3L));
        
        // 验证每个用户的向量
        for (Long userId : userIds) {
            UserBehaviorVector vector = vectors.get(userId);
            assertNotNull(vector);
            assertEquals(userId, vector.getUserId());
            assertTrue(vector.hasAnyBehavior());
        }
    }
    
    @Test
    @DisplayName("批量构建用户行为向量 - 空列表")
    void testBuildUserBehaviorVectors_EmptyList() {
        // 执行测试
        Map<Long, UserBehaviorVector> vectors = userBehaviorService.buildUserBehaviorVectors(Arrays.asList());
        
        // 验证结果
        assertNotNull(vectors);
        assertTrue(vectors.isEmpty());
    }
    
    @Test
    @DisplayName("批量构建用户行为向量 - null列表")
    void testBuildUserBehaviorVectors_NullList() {
        // 执行测试
        Map<Long, UserBehaviorVector> vectors = userBehaviorService.buildUserBehaviorVectors(null);
        
        // 验证结果
        assertNotNull(vectors);
        assertTrue(vectors.isEmpty());
    }
    
    @Test
    @DisplayName("构建用户行为向量 - 异常处理")
    void testBuildUserBehaviorVector_ExceptionHandling() {
        // 配置 Mock 行为 - 抛出异常
        when(userInterestMapper.selectCategoryIdsByUserId(testUserId))
            .thenThrow(new RuntimeException("Database error"));
        
        // 执行测试
        UserBehaviorVector vector = userBehaviorService.buildUserBehaviorVector(testUserId);
        
        // 验证结果 - 应该返回空的行为向量而不是抛出异常
        assertNotNull(vector);
        assertEquals(testUserId, vector.getUserId());
        assertFalse(vector.hasAnyBehavior());
    }
}
