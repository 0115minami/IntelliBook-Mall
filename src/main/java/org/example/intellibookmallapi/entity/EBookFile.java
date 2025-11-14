package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 电子书文件实体类
 */
@Data
public class EBookFile {
    
    /**
     * 文件ID
     */
    private Long fileId;
    
    /**
     * 书籍ID
     */
    private Long bookId;
    
    /**
     * 文件格式（PDF/EPUB/MOBI等）
     */
    private String fileFormat;
    
    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
