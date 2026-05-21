package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 管理员订单列表VO
 */
@Data
public class AdminOrderVO {

    private Long orderId;
    private String orderNo;
    /** 下单用户名 */
    private String username;
    /** 商品数量 */
    private Integer itemCount;
    /** 订单总价（分） */
    private Integer totalPrice;
    /** 支付状态：0=未支付 1=已支付 */
    private Integer payStatus;
    private String payStatusText;
    /** 订单状态：0=待支付 1=已完成 2=已取消 */
    private Integer orderStatus;
    private String orderStatusText;
    private Date createTime;
    private Date payTime;
}
