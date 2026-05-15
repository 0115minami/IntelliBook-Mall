package org.example.intellibookmallapi.recommend.model;

import lombok.Data;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用户行为向量（用于相似度计算）
 * 整合用户兴趣标签、收藏、购买、评价四个维度的行为数据
 */
@Data
public class UserBehaviorVector {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 兴趣分类集合
     */
    private Set<Long> interestCategories;
    
    /**
     * 收藏图书集合
     */
    private Set<Long> favoriteBooks;
    
    /**
     * 购买图书集合
     */
    private Set<Long> purchasedBooks;
    
    /**
     * 评价图书及评分（bookId -> rating）
     */
    private Map<Long, Integer> reviewedBooks;
    
    /**
     * 权重配置 - 兴趣标签权重
     */
    public static final double INTEREST_WEIGHT = 0.25;
    
    /**
     * 权重配置 - 收藏权重
     */
    public static final double FAVORITE_WEIGHT = 0.35;
    
    /**
     * 权重配置 - 购买权重
     */
    public static final double PURCHASE_WEIGHT = 0.25;
    
    /**
     * 权重配置 - 评价权重
     */
    public static final double REVIEW_WEIGHT = 0.15;
    
    /**
     * 默认构造函数，初始化空集合
     */
    public UserBehaviorVector() {
        this.interestCategories = new HashSet<>();
        this.favoriteBooks = new HashSet<>();
        this.purchasedBooks = new HashSet<>();
        this.reviewedBooks = new java.util.HashMap<>();
    }
    
    /**
     * 带用户ID的构造函数
     */
    public UserBehaviorVector(Long userId) {
        this();
        this.userId = userId;
    }
    
    /**
     * 判断用户是否有任何行为数据
     * @return true 如果用户至少有一项行为数据
     */
    public boolean hasAnyBehavior() {
        return !interestCategories.isEmpty() 
            || !favoriteBooks.isEmpty() 
            || !purchasedBooks.isEmpty() 
            || !reviewedBooks.isEmpty();
    }
    
    /**
     * 获取所有行为数据的总数
     * @return 行为数据总数
     */
    public int getTotalBehaviorCount() {
        return interestCategories.size() 
            + favoriteBooks.size() 
            + purchasedBooks.size() 
            + reviewedBooks.size();
    }
}
