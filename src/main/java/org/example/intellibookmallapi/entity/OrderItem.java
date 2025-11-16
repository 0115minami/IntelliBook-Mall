package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 订单项实体类
 */
@Data
public class OrderItem {
    
    /**
     * 订单项ID
     */
    private Long itemId;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 书籍ID
     */
    private Long bookId;
    
    /**
     * 书名（冗余字段，防止书籍信息变更）
     */
    private String bookTitle;
    
    /**
     * 价格（分）
     */
    private Integer price;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
