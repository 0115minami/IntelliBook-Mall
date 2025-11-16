package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 订单实体类
 */
@Data
public class Order {
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单总价（分）
     */
    private Integer totalPrice;
    
    /**
     * 支付状态（0-未支付 1-已支付）
     */
    private Integer payStatus;
    
    /**
     * 支付方式（0-未支付 1-支付宝 2-微信 3-余额）
     */
    private Integer payType;
    
    /**
     * 支付时间
     */
    private Date payTime;
    
    /**
     * 订单状态（0-待支付 1-已完成 2-已取消）
     */
    private Integer orderStatus;
    
    /**
     * 额外信息
     */
    private String extraInfo;
    
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
