package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 用户兴趣VO
 */
@Data
public class UserInterestVO {
    
    /**
     * 兴趣ID
     */
    private Long interestId;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
