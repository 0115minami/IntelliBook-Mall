package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.dto.BookshelfVO;
import org.example.intellibookmallapi.entity.ReadingProgress;

import java.util.List;

/**
 * 书架Mapper
 */
@Mapper
public interface BookshelfMapper {
    
    /**
     * 查询用户已购买的图书列表（分页）
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 每页数量
     * @param sortBy 排序方式（recent/purchase/title）
     * @return 书架图书列表
     */
    List<BookshelfVO> selectPurchasedBooks(@Param("userId") Long userId,
                                           @Param("offset") Integer offset,
                                           @Param("limit") Integer limit,
                                           @Param("sortBy") String sortBy);
    
    /**
     * 统计用户已购买的图书总数
     * @param userId 用户ID
     * @return 图书总数
     */
    Integer countPurchasedBooks(@Param("userId") Long userId);
    
    /**
     * 检查用户是否已购买某本书
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 是否已购买
     */
    Boolean checkPurchased(@Param("userId") Long userId, @Param("bookId") Long bookId);
    
    /**
     * 查询用户对某本书的阅读进度
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 阅读进度列表（可能有多个格式）
     */
    List<ReadingProgress> selectReadingProgress(@Param("userId") Long userId, @Param("bookId") Long bookId);
    
    /**
     * 插入或更新阅读进度
     * @param progress 阅读进度
     * @return 影响行数
     */
    Integer upsertReadingProgress(ReadingProgress progress);
    
    /**
     * 查询图书的可用文件格式
     * @param bookId 图书ID
     * @return 文件格式列表
     */
    List<String> selectAvailableFormats(@Param("bookId") Long bookId);
}
