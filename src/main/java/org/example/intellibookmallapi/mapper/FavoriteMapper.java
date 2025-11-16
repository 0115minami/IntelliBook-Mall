package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.Favorite;
import org.example.intellibookmallapi.dto.FavoriteVO;

import java.util.List;

/**
 * 收藏Mapper接口
 */
@Mapper
public interface FavoriteMapper {
    
    /**
     * 添加收藏
     */
    int insert(Favorite favorite);
    
    /**
     * 根据用户ID和书籍ID查询收藏
     */
    Favorite selectByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
    
    /**
     * 查询用户的收藏列表（分页）
     */
    List<FavoriteVO> selectFavoriteListByUserId(@Param("userId") Long userId,
                                                 @Param("offset") Integer offset,
                                                 @Param("limit") Integer limit);
    
    /**
     * 统计用户的收藏数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 删除收藏
     */
    int deleteByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
    
    /**
     * 检查是否已收藏
     */
    int checkFavorite(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
