package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.FavoriteVO;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.service.FavoriteService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏Controller
 */
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 添加收藏
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping("/add/{bookId}")
    public Result addFavorite(@PathVariable("bookId") Long bookId,
                             @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = favoriteService.addFavorite(userId, bookId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("收藏成功");
        } else {
            return ResultGenerator.genFailResult("收藏失败");
        }
    }
    
    /**
     * 取消收藏
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @DeleteMapping("/remove/{bookId}")
    public Result removeFavorite(@PathVariable("bookId") Long bookId,
                                 @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = favoriteService.removeFavorite(userId, bookId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("取消收藏成功");
        } else {
            return ResultGenerator.genFailResult("取消收藏失败");
        }
    }
    
    /**
     * 查询收藏列表（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param user 当前登录用户
     * @return 收藏列表
     */
    @GetMapping("/list")
    public Result getFavoriteList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  @TokenToUser User user) {
        Long userId = user.getUserId();
        PageResult<FavoriteVO> pageResult = favoriteService.getFavoriteList(userId, page, pageSize);
        
        return ResultGenerator.genSuccessResult(pageResult);
    }
    
    /**
     * 检查是否已收藏
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 是否已收藏
     */
    @GetMapping("/check/{bookId}")
    public Result checkFavorite(@PathVariable("bookId") Long bookId,
                               @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean isFavorite = favoriteService.checkFavorite(userId, bookId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("isFavorite", isFavorite);
        
        return ResultGenerator.genSuccessResult(data);
    }
}
