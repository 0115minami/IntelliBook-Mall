package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.CartVO;
import org.example.intellibookmallapi.entity.Cart;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.CartMapper;
import org.example.intellibookmallapi.mapper.EBookMapper;
import org.example.intellibookmallapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车Service实现类
 */
@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private EBookMapper eBookMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addToCart(Long userId, Long bookId) {
        // 1. 验证书籍是否存在且可购买
        EBook book = eBookMapper.selectByPrimaryKey(bookId);
        if (book == null) {
            throw new BusinessException("书籍不存在");
        }
        if (book.getStatus() == null || book.getStatus() == 0) {
            throw new BusinessException("书籍暂不可购买");
        }
        
        // 2. 检查是否已在购物车中
        Cart existingCart = cartMapper.selectByUserIdAndBookId(userId, bookId);
        if (existingCart != null) {
            // 如果已存在但被软删除，则恢复
            if (existingCart.getIsDeleted() == 1) {
                int restored = cartMapper.restoreByUserIdAndBookId(userId, bookId);
                return restored > 0;
            } else {
                throw new BusinessException("该书籍已在购物车中");
            }
        }
        
        // 3. 添加到购物车
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setBookId(bookId);
        
        int result = cartMapper.insert(cart);
        return result > 0;
    }
    
    @Override
    public List<CartVO> getCartList(Long userId) {
        return cartMapper.selectCartListByUserId(userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFromCart(Long userId, Long cartId) {
        int result = cartMapper.deleteByUserIdAndCartId(userId, cartId);
        if (result == 0) {
            throw new BusinessException("购物车项不存在或无权删除");
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCart(Long userId) {
        int result = cartMapper.deleteByUserId(userId);
        return result >= 0; // 即使购物车为空也返回成功
    }
    
    @Override
    public int getCartCount(Long userId) {
        return cartMapper.countByUserId(userId);
    }
}
