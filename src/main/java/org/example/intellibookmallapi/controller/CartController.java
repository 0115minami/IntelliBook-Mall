package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.CartVO;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.service.CartService;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车Controller
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * 添加到购物车
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping("/add/{bookId}")
    public Result addToCart(@PathVariable("bookId") Long bookId, 
                           @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = cartService.addToCart(userId, bookId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }
    }
    
    /**
     * 查询购物车列表
     * @param user 当前登录用户
     * @return 购物车列表
     */
    @GetMapping("/list")
    public Result getCartList(@TokenToUser User user) {
        Long userId = user.getUserId();
        List<CartVO> cartList = cartService.getCartList(userId);
        
        // 返回购物车列表和总数
        Map<String, Object> data = new HashMap<>();
        data.put("list", cartList);
        data.put("total", cartList.size());
        
        return ResultGenerator.genSuccessResult(data);
    }
    
    /**
     * 从购物车中移除商品
     * @param cartId 购物车ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @DeleteMapping("/remove/{cartId}")
    public Result removeFromCart(@PathVariable("cartId") Long cartId,
                                 @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = cartService.removeFromCart(userId, cartId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("移除成功");
        } else {
            return ResultGenerator.genFailResult("移除失败");
        }
    }
    
    /**
     * 清空购物车
     * @param user 当前登录用户
     * @return 操作结果
     */
    @DeleteMapping("/clear")
    public Result clearCart(@TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = cartService.clearCart(userId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("清空成功");
        } else {
            return ResultGenerator.genFailResult("清空失败");
        }
    }
    
    /**
     * 获取购物车商品数量
     * @param user 当前登录用户
     * @return 商品数量
     */
    @GetMapping("/count")
    public Result getCartCount(@TokenToUser User user) {
        Long userId = user.getUserId();
        int count = cartService.getCartCount(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        
        return ResultGenerator.genSuccessResult(data);
    }
}
