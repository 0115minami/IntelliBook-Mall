package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.dto.EBookSearchParam;
import org.example.intellibookmallapi.entity.EBook;

import java.util.List;

/**
 * 电子书Mapper接口
 */
@Mapper
public interface EBookMapper {
    
    /**
     * 根据ID查询电子书
     */
    EBook selectByPrimaryKey(@Param("bookId") Long bookId);
    
    /**
     * 查询电子书列表（分页）
     */
    List<EBook> selectList(@Param("offset") Integer offset, 
                           @Param("limit") Integer limit);
    
    /**
     * 查询电子书总数
     */
    Integer countAll();
    
    /**
     * 根据分类查询电子书列表
     */
    List<EBook> selectByCategory(@Param("categoryId") Long categoryId,
                                  @Param("offset") Integer offset,
                                  @Param("limit") Integer limit);
    
    /**
     * 根据分类统计电子书数量
     */
    Integer countByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * 搜索电子书
     */
    List<EBook> searchEBooks(@Param("param") EBookSearchParam param);
    
    /**
     * 搜索电子书总数
     */
    Integer countSearchResults(@Param("param") EBookSearchParam param);
    
    /**
     * 根据ISBN查询
     */
    EBook selectByIsbn(@Param("isbn") String isbn);
    
    /**
     * 查询热门电子书（按评分和评分人数）
     */
    List<EBook> selectHotBooks(@Param("limit") Integer limit);
    
    /**
     * 查询最新上架电子书
     */
    List<EBook> selectNewBooks(@Param("limit") Integer limit);
    
    /**
     * 统计搜索结果总数（Long类型）
     */
    Long countSearchEBooks(@Param("param") EBookSearchParam searchParam);
    
    /**
     * 查询热门电子书（按评分和评价数排序）
     */
    List<EBook> selectPopularEBooks(@Param("limit") Integer limit);
    
    /**
     * 查询最新电子书（按创建时间排序）
     */
    List<EBook> selectLatestEBooks(@Param("limit") Integer limit);
    
    /**
     * 根据分类查询电子书
     */
    List<EBook> selectEBooksByCategory(@Param("categoryId") Long categoryId, 
                                       @Param("offset") Integer offset, 
                                       @Param("limit") Integer limit);
    
    /**
     * 统计分类下的电子书数量
     */
    Long countEBooksByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * 根据标签查询相关电子书
     */
    List<EBook> selectEBooksByTag(@Param("tag") String tag, 
                                  @Param("excludeBookId") Long excludeBookId, 
                                  @Param("limit") Integer limit);
}
