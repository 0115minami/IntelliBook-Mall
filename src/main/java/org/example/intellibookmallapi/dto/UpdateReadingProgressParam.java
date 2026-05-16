package org.example.intellibookmallapi.dto;

import lombok.Data;

/**
 * 更新阅读进度参数
 */
@Data
public class UpdateReadingProgressParam {
    
    /**
     * 图书ID
     */
    private Long bookId;
    
    /**
     * 文件格式（pdf/epub）
     */
    private String fileFormat;
    
    /**
     * 最后阅读位置（JSON格式）
     * 示例：{"page":150,"progress":45,"chapter":"第4章"}
     */
    private String lastPosition;
}
