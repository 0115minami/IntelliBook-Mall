package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.dto.AdminEBookSearchParam;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.entity.EBookFile;

import java.util.List;

/**
 * 管理员电子书Mapper接口
 */
@Mapper
public interface AdminEBookMapper {
    
    /**
     * 根据ID查询电子书（包含已删除的）
     */
    EBook selectByPrimaryKeyForAdmin(@Param("bookId") Long bookId);
    
    /**
     * 搜索电子书列表（管理员视图）
     */
    List<EBook> searchEBooksForAdmin(@Param("param") AdminEBookSearchParam param);
    
    /**
     * 统计搜索结果总数（管理员视图）
     */
    Long countSearchEBooksForAdmin(@Param("param") AdminEBookSearchParam param);
    
    /**
     * 插入电子书
     */
    int insertEBook(EBook ebook);
    
    /**
     * 更新电子书
     */
    int updateEBook(EBook ebook);
    
    /**
     * 更新电子书状态
     */
    int updateEBookStatus(@Param("bookId") Long bookId, @Param("status") Integer status);
    
    /**
     * 批量更新电子书状态
     */
    int batchUpdateEBookStatus(@Param("bookIds") List<Long> bookIds, @Param("status") Integer status);
    
    /**
     * 逻辑删除电子书
     */
    int deleteEBook(@Param("bookId") Long bookId);
    
    /**
     * 批量逻辑删除电子书
     */
    int batchDeleteEBooks(@Param("bookIds") List<Long> bookIds);
    
    /**
     * 根据ISBN查询电子书（包含已删除的）
     */
    EBook selectByIsbnForAdmin(@Param("isbn") String isbn);
    
    /**
     * 插入电子书文件
     */
    int insertEBookFile(EBookFile ebookFile);
    
    /**
     * 根据文件ID查询电子书文件
     */
    EBookFile selectEBookFileById(@Param("fileId") Long fileId);
    
    /**
     * 删除电子书文件
     */
    int deleteEBookFile(@Param("fileId") Long fileId);
    
    /**
     * 根据书籍ID和格式查询文件
     */
    EBookFile selectEBookFileByBookIdAndFormat(@Param("bookId") Long bookId, @Param("format") String format);
}
