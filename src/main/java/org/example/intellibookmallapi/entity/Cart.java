package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 购物车实体类
 */
@Data
public class Cart {
    
    /**
     * 购物车ID
     */
    private Long cartId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 电子书ID
     */
    private Long bookId;
    
    /**
     * 是否删除（0-未删除 1-已删除）
     */
    private Integer isDeleted;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}
