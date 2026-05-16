package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 阅读进度实体类
 */
@Data
public class ReadingProgress {
    
    /**
     * 进度ID
     */
    private Long progressId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 图书ID
     */
    private Long bookId;
    
    /**
     * 文件格式
     */
    private String fileFormat;
    
    /**
     * 最后阅读位置
     */
    private String lastPosition;
    
    /**
     * 最后阅读时间
     */
    private Date lastReadTime;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}
