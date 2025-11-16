package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.FavoriteVO;
import org.example.intellibookmallapi.util.PageResult;

/**
 * 收藏Service接口
 */
public interface FavoriteService {
    
    /**
     * 添加收藏
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @return 是否成功
     */
    boolean addFavorite(Long userId, Long bookId);
    
    /**
     * 取消收藏
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @return 是否成功
     */
    boolean removeFavorite(Long userId, Long bookId);
    
    /**
     * 查询收藏列表（分页）
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 收藏列表
     */
    PageResult<FavoriteVO> getFavoriteList(Long userId, Integer page, Integer pageSize);
    
    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param bookId 书籍ID
     * @return 是否已收藏
     */
    boolean checkFavorite(Long userId, Long bookId);
}
