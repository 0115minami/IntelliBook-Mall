package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.EBookSearchParam;
import org.example.intellibookmallapi.dto.EBookVO;
import org.example.intellibookmallapi.util.PageResult;

import java.util.List;

/**
 * 电子书业务接口
 */
public interface EBookService {
    
    /**
     * 根据ID查询电子书详情
     * @param bookId 书籍ID
     * @return 电子书详情
     */
    EBookVO getEBookById(Long bookId);
    
    /**
     * 搜索电子书（分页）
     * @param searchParam 搜索参数
     * @return 分页结果
     */
    PageResult<EBookVO> searchEBooks(EBookSearchParam searchParam);
    
    /**
     * 获取热门电子书
     * @param limit 限制数量（默认10）
     * @return 热门电子书列表
     */
    List<EBookVO> getPopularEBooks(Integer limit);
    
    /**
     * 获取最新电子书
     * @param limit 限制数量（默认10）
     * @return 最新电子书列表
     */
    List<EBookVO> getLatestEBooks(Integer limit);
    
    /**
     * 根据分类查询电子书（分页）
     * @param categoryId 分类ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    PageResult<EBookVO> getEBooksByCategory(Long categoryId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取相关推荐电子书
     * @param bookId 当前书籍ID
     * @param limit 限制数量（默认5）
     * @return 相关电子书列表
     */
    List<EBookVO> getRelatedEBooks(Long bookId, Integer limit);
}
