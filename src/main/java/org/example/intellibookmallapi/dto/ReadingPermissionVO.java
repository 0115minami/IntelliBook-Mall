package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 阅读权限VO
 * 用于检查用户是否有权限阅读某本书
 */
@Data
public class ReadingPermissionVO {
    
    /**
     * 是否已购买
     */
    private Boolean hasPurchased;
    
    /**
     * 是否可以阅读
     */
    private Boolean canRead;
    
    /**
     * 可用文件格式列表
     */
    private List<String> availableFormats;
    
    /**
     * 最后阅读时间
     */
    private Date lastReadTime;
    
    /**
     * 最后阅读格式
     */
    private String lastReadFormat;
    
    /**
     * 最后阅读位置（JSON格式）
     */
    private String lastPosition;
}
