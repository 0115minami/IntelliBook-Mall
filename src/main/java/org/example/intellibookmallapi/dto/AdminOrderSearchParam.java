package org.example.intellibookmallapi.dto;

import lombok.Data;

/**
 * 管理员订单搜索参数
 */
@Data
public class AdminOrderSearchParam {

    /** 订单号或用户名模糊搜索 */
    private String keyword;

    /** 支付状态：-1=全部 0=未支付 1=已支付 */
    private Integer payStatus = -1;

    /** 订单状态：-1=全部 0=待支付 1=已完成 2=已取消 */
    private Integer orderStatus = -1;

    /** 开始日期（yyyy-MM-dd） */
    private String startDate;

    /** 结束日期（yyyy-MM-dd） */
    private String endDate;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
