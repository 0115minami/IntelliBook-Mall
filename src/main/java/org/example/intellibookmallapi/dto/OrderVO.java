package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 订单列表返回对象
 */
@Data
public class OrderVO {
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 订单总价（分）
     */
    private Integer totalPrice;
    
    /**
     * 支付状态（0-未支付 1-已支付）
     */
    private Integer payStatus;
    
    /**
     * 支付状态文本
     */
    private String payStatusText;
    
    /**
     * 订单状态（0-待支付 1-已完成 2-已取消）
     */
    private Integer orderStatus;
    
    /**
     * 订单状态文本
     */
    private String orderStatusText;
    
    /**
     * 订单项数量
     */
    private Integer itemCount;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 支付时间
     */
    private Date payTime;
}
