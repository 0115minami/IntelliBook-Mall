package org.example.intellibookmallapi.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.intellibookmallapi.dto.CreateReviewParam;
import org.example.intellibookmallapi.dto.ReviewVO;
import org.example.intellibookmallapi.entity.Review;
import org.example.intellibookmallapi.entity.ReviewLike;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.EBookMapper;
import org.example.intellibookmallapi.mapper.ReviewLikeMapper;
import org.example.intellibookmallapi.mapper.ReviewMapper;
import org.example.intellibookmallapi.service.ReviewService;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评价Service实现类
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    @Autowired
    private ReviewLikeMapper reviewLikeMapper;
    
    @Autowired
    private EBookMapper eBookMapper;
    
    @Override
    @Transactional
    public boolean createReview(Long userId, CreateReviewParam param) {
        // 验证参数
        if (param.getBookId() == null) {
            throw new BusinessException("书籍ID不能为空");
        }
        if (param.getRating() == null || param.getRating() < 1 || param.getRating() > 5) {
            throw new BusinessException("评分必须在1-5之间");
        }
        
        // 验证书籍是否存在
        if (eBookMapper.selectByPrimaryKey(param.getBookId()) == null) {
            throw new BusinessException("书籍不存在");
        }
        
        // 检查是否已评价
        Review existingReview = reviewMapper.selectByUserIdAndBookId(userId, param.getBookId());
        if (existingReview != null) {
            throw new BusinessException("您已经评价过该书籍");
        }
        
        // 创建评价
        Review review = new Review();
        review.setUserId(userId);
        review.setBookId(param.getBookId());
        review.setRating(param.getRating());
        review.setContent(param.getContent());
        
        int result = reviewMapper.insert(review);
        
        // 更新书籍的平均评分和评价数量
        if (result > 0) {
            updateBookRating(param.getBookId());
        }
        
        return result > 0;
    }
    
    @Override
    public PageResult<ReviewVO> getBookReviews(Long bookId, Long currentUserId, Integer page, Integer pageSize) {
        // 验证书籍是否存在
        if (eBookMapper.selectByPrimaryKey(bookId) == null) {
            throw new BusinessException("书籍不存在");
        }
        
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ReviewVO> list = reviewMapper.selectByBookId(bookId, currentUserId);
        PageInfo<ReviewVO> pageInfo = new PageInfo<>(list);
        
        // 构建分页结果
        PageResult<ReviewVO> pageResult = new PageResult<>();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalCount(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setList(list);
        
        return pageResult;
    }
    
    @Override
    public PageResult<ReviewVO> getUserReviews(Long userId, Integer page, Integer pageSize) {
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ReviewVO> list = reviewMapper.selectByUserId(userId);
        PageInfo<ReviewVO> pageInfo = new PageInfo<>(list);
        
        // 构建分页结果
        PageResult<ReviewVO> pageResult = new PageResult<>();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalCount(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setList(list);
        
        return pageResult;
    }
    
    @Override
    @Transactional
    public boolean updateReview(Long userId, Long reviewId, CreateReviewParam param) {
        // 验证参数
        if (param.getRating() == null || param.getRating() < 1 || param.getRating() > 5) {
            throw new BusinessException("评分必须在1-5之间");
        }
        
        // 查询评价
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        
        // 验证权限
        if (!review.getUserId().equals(userId)) {
            throw new BusinessException("无权修改他人的评价");
        }
        
        // 更新评价
        review.setRating(param.getRating());
        review.setContent(param.getContent());
        
        int result = reviewMapper.updateByPrimaryKey(review);
        
        // 更新书籍的平均评分
        if (result > 0) {
            updateBookRating(review.getBookId());
        }
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteReview(Long userId, Long reviewId) {
        // 查询评价
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        
        // 验证权限
        if (!review.getUserId().equals(userId)) {
            throw new BusinessException("无权删除他人的评价");
        }
        
        // 删除评价
        int result = reviewMapper.deleteByPrimaryKey(reviewId);
        
        // 更新书籍的平均评分和评价数量
        if (result > 0) {
            updateBookRating(review.getBookId());
        }
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean likeReview(Long userId, Long reviewId) {
        // 查询评价
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        
        // 检查是否已点赞
        ReviewLike existingLike = reviewLikeMapper.selectByUserIdAndReviewId(userId, reviewId);
        if (existingLike != null) {
            throw new BusinessException("您已经点赞过该评价");
        }
        
        // 创建点赞记录
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.setUserId(userId);
        reviewLike.setReviewId(reviewId);
        
        int result = reviewLikeMapper.insert(reviewLike);
        
        // 增加点赞数
        if (result > 0) {
            reviewMapper.increaseLikeCount(reviewId);
        }
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean unlikeReview(Long userId, Long reviewId) {
        // 查询评价
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }
        
        // 检查是否已点赞
        ReviewLike existingLike = reviewLikeMapper.selectByUserIdAndReviewId(userId, reviewId);
        if (existingLike == null) {
            throw new BusinessException("您还未点赞该评价");
        }
        
        // 删除点赞记录
        int result = reviewLikeMapper.deleteByUserIdAndReviewId(userId, reviewId);
        
        // 减少点赞数
        if (result > 0) {
            reviewMapper.decreaseLikeCount(reviewId);
        }
        
        return result > 0;
    }
    
    @Override
    public boolean hasReviewed(Long userId, Long bookId) {
        Review review = reviewMapper.selectByUserIdAndBookId(userId, bookId);
        return review != null;
    }
    
    /**
     * 更新书籍的平均评分和评价数量
     */
    private void updateBookRating(Long bookId) {
        // 计算平均评分
        Double avgRating = reviewMapper.calculateAverageRating(bookId);
        if (avgRating == null) {
            avgRating = 0.0;
        }
        
        // 统计评价数量
        int ratingCount = reviewMapper.countByBookId(bookId);
        
        // 更新书籍信息
        eBookMapper.updateRating(bookId, avgRating, ratingCount);
    }
}
