package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.CreateReviewParam;
import org.example.intellibookmallapi.dto.ReviewVO;
import org.example.intellibookmallapi.util.PageResult;

/**
 * 评价Service接口
 */
public interface ReviewService {
    
    /**
     * 创建评价
     * @param userId 用户ID
     * @param param 评价参数
     * @return 是否成功
     */
    boolean createReview(Long userId, CreateReviewParam param);
    
    /**
     * 查询书籍的评价列表（分页）
     * @param bookId 书籍ID
     * @param currentUserId 当前用户ID（用于判断是否已点赞，可为null）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 评价列表
     */
    PageResult<ReviewVO> getBookReviews(Long bookId, Long currentUserId, Integer page, Integer pageSize);
    
    /**
     * 查询用户的评价列表（分页）
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 评价列表
     */
    PageResult<ReviewVO> getUserReviews(Long userId, Integer page, Integer pageSize);
    
    /**
     * 更新评价
     * @param userId 用户ID
     * @param reviewId 评价ID
     * @param param 评价参数
     * @return 是否成功
     */
    boolean updateReview(Long userId, Long reviewId, CreateReviewParam param);
    
    /**
     * 删除评价
     * @param userId 用户ID
     * @param reviewId 评价ID
     * @return 是否成功
     */
    boolean deleteReview(Long userId, Long reviewId);
    
    /**
     * 点赞评价
     * @param userId 用户ID
     * @param reviewId 评价ID
     * @return 是否成功
     */
    boolean likeReview(Long userId, Long reviewId);
    
    /**
     * 取消点赞
     * @param userId 用户ID
     * @param reviewId 评价ID
     * @return 是否成功
     */
    boolean unlikeReview(Long userId, Long reviewId);
    
    /**
     * 检查用户是否已评价该书籍
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @return 是否已评价
     */
    boolean hasReviewed(Long userId, Long bookId);
}
