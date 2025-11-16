package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.Cart;
import org.example.intellibookmallapi.dto.CartVO;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartMapper {
    
    /**
     * 添加到购物车
     */
    int insert(Cart cart);
    
    /**
     * 根据用户ID和书籍ID查询购物车项
     */
    Cart selectByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
    
    /**
     * 查询用户的购物车列表（包含书籍详细信息）
     */
    List<CartVO> selectCartListByUserId(@Param("userId") Long userId);
    
    /**
     * 根据购物车ID删除
     */
    int deleteByCartId(@Param("cartId") Long cartId);
    
    /**
     * 根据用户ID和购物车ID删除（确保用户只能删除自己的购物车项）
     */
    int deleteByUserIdAndCartId(@Param("userId") Long userId, @Param("cartId") Long cartId);
    
    /**
     * 清空用户的购物车
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户购物车中的商品数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 恢复已删除的购物车项（软删除恢复）
     */
    int restoreByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
