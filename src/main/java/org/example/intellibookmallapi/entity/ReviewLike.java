package org.example.intellibookmallapi.entity;

import java.time.LocalDateTime;

/**
 * 评价点赞实体类
 */
public class ReviewLike {
    
    private Long likeId;
    private Long userId;
    private Long reviewId;
    private LocalDateTime createTime;
    
    // Getters and Setters
    public Long getLikeId() {
        return likeId;
    }
    
    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
