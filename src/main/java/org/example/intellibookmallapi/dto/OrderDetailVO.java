package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 订单详情返回对象
 */
@Data
public class OrderDetailVO {
    
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
     * 支付方式（0-未支付 1-支付宝 2-微信 3-余额）
     */
    private Integer payType;
    
    /**
     * 支付方式文本
     */
    private String payTypeText;
    
    /**
     * 订单状态（0-待支付 1-已完成 2-已取消）
     */
    private Integer orderStatus;
    
    /**
     * 订单状态文本
     */
    private String orderStatusText;
    
    /**
     * 额外信息
     */
    private String extraInfo;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 支付时间
     */
    private Date payTime;
    
    /**
     * 订单项列表
     */
    private List<OrderItemVO> items;
    
    /**
     * 订单项内部类
     */
    @Data
    public static class OrderItemVO {
        /**
         * 订单项ID
         */
        private Long itemId;
        
        /**
         * 书籍ID
         */
        private Long bookId;
        
        /**
         * 书名
         */
        private String bookTitle;
        
        /**
         * 作者
         */
        private String author;
        
        /**
         * 封面图片
         */
        private String coverImg;
        
        /**
         * 价格（分）
         */
        private Integer price;
    }
}
