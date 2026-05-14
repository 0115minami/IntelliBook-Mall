package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.*;
import org.example.intellibookmallapi.util.PageResult;

import java.util.List;

/**
 * 管理员图书管理服务接口
 */
public interface AdminEBookService {
    
    /**
     * 查询图书列表（分页）
     * 
     * @param searchParam 搜索参数
     * @return 分页结果
     */
    PageResult<AdminEBookVO> searchEBooks(AdminEBookSearchParam searchParam);
    
    /**
     * 获取图书详情
     * 
     * @param bookId 图书ID
     * @return 图书详情
     */
    AdminEBookVO getEBookById(Long bookId);
    
    /**
     * 创建图书
     * 
     * @param param 创建参数
     * @return 图书ID
     */
    Long createEBook(AdminEBookCreateParam param);
    
    /**
     * 更新图书
     * 
     * @param bookId 图书ID
     * @param param 更新参数
     */
    void updateEBook(Long bookId, AdminEBookUpdateParam param);
    
    /**
     * 更新图书状态（上下架）
     * 
     * @param bookId 图书ID
     * @param status 状态（1-上架, 0-下架）
     */
    void updateEBookStatus(Long bookId, Integer status);
    
    /**
     * 批量更新图书状态
     * 
     * @param bookIds 图书ID列表
     * @param status 状态
     * @return 操作结果
     */
    BatchOperationResult batchUpdateStatus(List<Long> bookIds, Integer status);
    
    /**
     * 删除图书（逻辑删除）
     * 
     * @param bookId 图书ID
     */
    void deleteEBook(Long bookId);
    
    /**
     * 批量删除图书
     * 
     * @param bookIds 图书ID列表
     * @return 操作结果
     */
    BatchOperationResult batchDeleteEBooks(List<Long> bookIds);
    
    /**
     * 删除电子书文件
     * 
     * @param bookId 图书ID
     * @param fileId 文件ID
     */
    void deleteEBookFile(Long bookId, Long fileId);
    
    /**
     * 添加电子书文件
     * 
     * @param bookId 图书ID
     * @param fileFormat 文件格式
     * @param file 文件
     * @return 文件ID
     */
    Long addEBookFile(Long bookId, String fileFormat, org.springframework.web.multipart.MultipartFile file);
}
