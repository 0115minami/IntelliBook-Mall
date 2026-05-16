package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.BookshelfVO;
import org.example.intellibookmallapi.dto.ReadingPermissionVO;
import org.example.intellibookmallapi.dto.UpdateReadingProgressParam;
import org.example.intellibookmallapi.util.PageResult;

import java.nio.file.Path;

/**
 * 书架Service接口
 */
public interface BookshelfService {
    
    /**
     * 获取用户的书架列表（已购买的图书）
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param sortBy 排序方式（recent/purchase/title）
     * @return 书架图书列表
     */
    PageResult<BookshelfVO> getBookshelfList(Long userId, Integer page, Integer pageSize, String sortBy);
    
    /**
     * 检查用户对某本书的阅读权限
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 阅读权限信息
     */
    ReadingPermissionVO checkReadingPermission(Long userId, Long bookId);
    
    /**
     * 更新阅读进度
     * @param userId 用户ID
     * @param param 阅读进度参数
     * @return 是否成功
     */
    Boolean updateReadingProgress(Long userId, UpdateReadingProgressParam param);
    
    /**
     * 获取图书文件路径（用于在线阅读）
     * @param userId 用户ID
     * @param bookId 图书ID
     * @param format 文件格式（pdf/epub/mobi）
     * @return 文件路径，如果用户未购买或文件不存在则返回null
     */
    Path getBookFilePath(Long userId, Long bookId, String format);
}
