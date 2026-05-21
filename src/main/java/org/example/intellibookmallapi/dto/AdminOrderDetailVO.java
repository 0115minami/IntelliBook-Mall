package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 管理员订单详情VO
 */
@Data
public class AdminOrderDetailVO {

    private Long orderId;
    private String orderNo;
    private Long userId;
    private String username;
    private String nickname;
    private Integer totalPrice;
    private Integer payStatus;
    private String payStatusText;
    private Integer payType;
    private String payTypeText;
    private Integer orderStatus;
    private String orderStatusText;
    private String extraInfo;
    private Date createTime;
    private Date payTime;

    private List<OrderDetailVO.OrderItemVO> items;
}
