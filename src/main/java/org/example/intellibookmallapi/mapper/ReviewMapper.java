package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.dto.ReviewVO;
import org.example.intellibookmallapi.entity.Review;

import java.util.List;

/**
 * 评价Mapper接口
 */
public interface ReviewMapper {
    
    /**
     * 插入评价
     */
    int insert(Review review);
    
    /**
     * 根据ID查询评价
     */
    Review selectByPrimaryKey(Long reviewId);
    
    /**
     * 根据用户ID和书籍ID查询评价
     */
    Review selectByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
    
    /**
     * 查询书籍的评价列表（分页）
     */
    List<ReviewVO> selectByBookId(@Param("bookId") Long bookId, 
                                   @Param("currentUserId") Long currentUserId);
    
    /**
     * 查询用户的评价列表（分页）
     */
    List<ReviewVO> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 统计书籍的评价数量
     */
    int countByBookId(Long bookId);
    
    /**
     * 更新评价
     */
    int updateByPrimaryKey(Review review);
    
    /**
     * 删除评价（软删除）
     */
    int deleteByPrimaryKey(Long reviewId);
    
    /**
     * 增加点赞数
     */
    int increaseLikeCount(Long reviewId);
    
    /**
     * 减少点赞数
     */
    int decreaseLikeCount(Long reviewId);
    
    /**
     * 计算书籍的平均评分
     */
    Double calculateAverageRating(Long bookId);
}
