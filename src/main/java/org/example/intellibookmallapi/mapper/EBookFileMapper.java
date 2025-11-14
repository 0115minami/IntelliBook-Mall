package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.EBookFile;

import java.util.List;

/**
 * 电子书文件Mapper接口
 */
@Mapper
public interface EBookFileMapper {
    
    /**
     * 根据书籍ID查询所有文件
     */
    List<EBookFile> selectByBookId(@Param("bookId") Long bookId);
    
    /**
     * 根据文件ID查询
     */
    EBookFile selectByPrimaryKey(@Param("fileId") Long fileId);
    
    /**
     * 根据书籍ID和格式查询
     */
    EBookFile selectByBookIdAndFormat(@Param("bookId") Long bookId, 
                                      @Param("format") String format);
}
