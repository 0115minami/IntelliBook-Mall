package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.CreateReviewParam;
import org.example.intellibookmallapi.dto.ReviewVO;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.service.ReviewService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 评价Controller
 */
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 创建评价
     * @param param 评价参数
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping("/create")
    public Result<String> createReview(@RequestBody CreateReviewParam param,
                                       @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = reviewService.createReview(userId, param);
        
        if (success) {
            return ResultGenerator.genSuccessResult("评价成功");
        } else {
            return ResultGenerator.genFailResult("评价失败");
        }
    }
    
    /**
     * 查询书籍的评价列表（分页）
     * @param bookId 书籍ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param user 当前登录用户（可选，用于判断是否已点赞）
     * @return 评价列表
     */
    @GetMapping("/book/{bookId}")
    public Result<PageResult<ReviewVO>> getBookReviews(
            @PathVariable("bookId") Long bookId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @TokenToUser(required = false) User user) {
        
        Long currentUserId = user != null ? user.getUserId() : null;
        PageResult<ReviewVO> pageResult = reviewService.getBookReviews(bookId, currentUserId, page, pageSize);
        
        return ResultGenerator.genSuccessResult(pageResult);
    }
    
    /**
     * 查询用户的评价列表（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param user 当前登录用户
     * @return 评价列表
     */
    @GetMapping("/my")
    public Result<PageResult<ReviewVO>> getMyReviews(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @TokenToUser User user) {
        
        Long userId = user.getUserId();
        PageResult<ReviewVO> pageResult = reviewService.getUserReviews(userId, page, pageSize);
        
        return ResultGenerator.genSuccessResult(pageResult);
    }
    
    /**
     * 更新评价
     * @param reviewId 评价ID
     * @param param 评价参数
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PutMapping("/update/{reviewId}")
    public Result<String> updateReview(@PathVariable("reviewId") Long reviewId,
                                       @RequestBody CreateReviewParam param,
                                       @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = reviewService.updateReview(userId, reviewId, param);
        
        if (success) {
            return ResultGenerator.genSuccessResult("更新成功");
        } else {
            return ResultGenerator.genFailResult("更新失败");
        }
    }
    
    /**
     * 删除评价
     * @param reviewId 评价ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @DeleteMapping("/delete/{reviewId}")
    public Result<String> deleteReview(@PathVariable("reviewId") Long reviewId,
                                       @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = reviewService.deleteReview(userId, reviewId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("删除成功");
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    
    /**
     * 点赞评价
     * @param reviewId 评价ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping("/like/{reviewId}")
    public Result<String> likeReview(@PathVariable("reviewId") Long reviewId,
                                     @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = reviewService.likeReview(userId, reviewId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("点赞成功");
        } else {
            return ResultGenerator.genFailResult("点赞失败");
        }
    }
    
    /**
     * 取消点赞
     * @param reviewId 评价ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @DeleteMapping("/unlike/{reviewId}")
    public Result<String> unlikeReview(@PathVariable("reviewId") Long reviewId,
                                       @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = reviewService.unlikeReview(userId, reviewId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("取消点赞成功");
        } else {
            return ResultGenerator.genFailResult("取消点赞失败");
        }
    }
    
    /**
     * 检查用户是否已评价该书籍
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 是否已评价
     */
    @GetMapping("/check/{bookId}")
    public Result<Map<String, Boolean>> checkReviewed(@PathVariable("bookId") Long bookId,
                                                       @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean hasReviewed = reviewService.hasReviewed(userId, bookId);
        
        Map<String, Boolean> data = new HashMap<>();
        data.put("hasReviewed", hasReviewed);
        
        return ResultGenerator.genSuccessResult(data);
    }
}
