package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.CartVO;

import java.util.List;

/**
 * 购物车Service接口
 */
public interface CartService {
    
    /**
     * 添加到购物车
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @return 是否成功
     */
    boolean addToCart(Long userId, Long bookId);
    
    /**
     * 查询用户的购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<CartVO> getCartList(Long userId);
    
    /**
     * 从购物车中移除商品
     * @param userId 用户ID
     * @param cartId 购物车ID
     * @return 是否成功
     */
    boolean removeFromCart(Long userId, Long cartId);
    
    /**
     * 清空购物车
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean clearCart(Long userId);
    
    /**
     * 获取购物车商品数量
     * @param userId 用户ID
     * @return 商品数量
     */
    int getCartCount(Long userId);
}
