package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.dto.AdminDashboardVO;
import org.example.intellibookmallapi.dto.AdminOrderDetailVO;
import org.example.intellibookmallapi.dto.AdminOrderSearchParam;
import org.example.intellibookmallapi.dto.AdminOrderVO;

import java.util.List;

/**
 * 管理员订单管理Mapper
 */
@Mapper
public interface AdminOrderMapper {

    /**
     * 分页查询订单列表（含用户名）
     */
    List<AdminOrderVO> selectOrderList(@Param("param") AdminOrderSearchParam param,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    /**
     * 统计订单总数
     */
    long countOrders(@Param("param") AdminOrderSearchParam param);

    /**
     * 查询订单详情（含用户信息和订单项）
     */
    AdminOrderDetailVO selectOrderDetailById(@Param("orderId") Long orderId);

    /**
     * 今日订单总数
     */
    int countTodayOrders();

    /**
     * 今日已支付订单数
     */
    int countTodayPaidOrders();

    /**
     * 今日已取消订单数
     */
    int countTodayCancelOrders();

    /**
     * 今日销售额（分，仅已支付）
     */
    Integer sumTodaySales();

    /**
     * 最近7天每日统计
     */
    List<AdminDashboardVO.DailyStatVO> selectDailyStats();

    /**
     * 管理员修改订单状态
     * 可将订单强制设为：0=待支付 1=已完成 2=已取消
     */
    int updateOrderStatus(@Param("orderId") Long orderId,
                          @Param("orderStatus") Integer orderStatus);

    /**
     * 管理员逻辑删除订单
     */
    int deleteOrder(@Param("orderId") Long orderId);

    /**
     * 批量逻辑删除订单
     */
    int batchDeleteOrders(@Param("orderIds") List<Long> orderIds);
}
