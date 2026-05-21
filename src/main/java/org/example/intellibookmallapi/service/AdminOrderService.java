package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.AdminDashboardVO;
import org.example.intellibookmallapi.dto.AdminOrderDetailVO;
import org.example.intellibookmallapi.dto.AdminOrderSearchParam;
import org.example.intellibookmallapi.dto.AdminOrderVO;
import org.example.intellibookmallapi.util.PageResult;

import java.util.List;

/**
 * 管理员订单管理Service
 */
public interface AdminOrderService {

    /**
     * 分页查询订单列表
     */
    PageResult<AdminOrderVO> getOrderList(AdminOrderSearchParam param);

    /**
     * 查询订单详情
     */
    AdminOrderDetailVO getOrderDetail(Long orderId);

    /**
     * 获取仪表盘统计数据
     */
    AdminDashboardVO getDashboard();

    /**
     * 修改订单状态（管理员强制变更）
     * orderStatus: 0=待支付 1=已完成 2=已取消
     */
    void updateOrderStatus(Long orderId, Integer orderStatus);

    /**
     * 逻辑删除单条订单
     */
    void deleteOrder(Long orderId);

    /**
     * 批量逻辑删除订单
     */
    void batchDeleteOrders(List<Long> orderIds);
}
