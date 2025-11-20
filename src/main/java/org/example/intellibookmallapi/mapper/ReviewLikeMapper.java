package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.ReviewLike;

/**
 * 评价点赞Mapper接口
 */
public interface ReviewLikeMapper {
    
    /**
     * 插入点赞记录
     */
    int insert(ReviewLike reviewLike);
    
    /**
     * 查询用户是否已点赞
     */
    ReviewLike selectByUserIdAndReviewId(@Param("userId") Long userId, 
                                         @Param("reviewId") Long reviewId);
    
    /**
     * 删除点赞记录
     */
    int deleteByUserIdAndReviewId(@Param("userId") Long userId, 
                                   @Param("reviewId") Long reviewId);
}
