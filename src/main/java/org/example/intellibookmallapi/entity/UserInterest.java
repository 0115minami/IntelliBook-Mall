package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户兴趣实体类
 */
@Data
public class UserInterest {
    
    /**
     * 兴趣ID
     */
    private Long interestId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 分类名称（关联查询字段）
     */
    private String categoryName;
}
