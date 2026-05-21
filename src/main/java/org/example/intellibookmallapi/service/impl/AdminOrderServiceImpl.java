package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.*;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.AdminOrderMapper;
import org.example.intellibookmallapi.mapper.OrderItemMapper;
import org.example.intellibookmallapi.service.AdminOrderService;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员订单管理Service实现
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    private AdminOrderMapper adminOrderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public PageResult<AdminOrderVO> getOrderList(AdminOrderSearchParam param) {
        if (param.getPageNum() == null || param.getPageNum() < 1) param.setPageNum(1);
        if (param.getPageSize() == null || param.getPageSize() < 1) param.setPageSize(10);

        int offset = (param.getPageNum() - 1) * param.getPageSize();
        List<AdminOrderVO> list = adminOrderMapper.selectOrderList(param, offset, param.getPageSize());
        long total = adminOrderMapper.countOrders(param);

        // 填充状态文本
        for (AdminOrderVO vo : list) {
            vo.setPayStatusText(getPayStatusText(vo.getPayStatus()));
            vo.setOrderStatusText(getOrderStatusText(vo.getOrderStatus()));
        }

        return PageResult.of(param.getPageNum(), param.getPageSize(), total, list);
    }

    @Override
    public AdminOrderDetailVO getOrderDetail(Long orderId) {
        AdminOrderDetailVO detail = adminOrderMapper.selectOrderDetailById(orderId);
        if (detail == null) {
            throw new BusinessException("订单不存在");
        }

        detail.setPayStatusText(getPayStatusText(detail.getPayStatus()));
        detail.setPayTypeText(getPayTypeText(detail.getPayType()));
        detail.setOrderStatusText(getOrderStatusText(detail.getOrderStatus()));

        // 查询订单项（复用已有Mapper）
        List<OrderDetailVO.OrderItemVO> items = orderItemMapper.selectByOrderId(orderId);
        detail.setItems(items);

        return detail;
    }

    @Override
    public AdminDashboardVO getDashboard() {
        AdminDashboardVO vo = new AdminDashboardVO();
        vo.setTodayOrderCount(adminOrderMapper.countTodayOrders());
        vo.setTodayPaidCount(adminOrderMapper.countTodayPaidOrders());
        vo.setTodayCancelCount(adminOrderMapper.countTodayCancelOrders());

        Integer sales = adminOrderMapper.sumTodaySales();
        vo.setTodaySales(sales != null ? sales : 0);

        vo.setDailyStats(adminOrderMapper.selectDailyStats());
        return vo;
    }

    private String getPayStatusText(Integer payStatus) {
        if (payStatus == null) return "未知";
        return payStatus == 1 ? "已支付" : "未支付";
    }

    private String getPayTypeText(Integer payType) {
        if (payType == null) return "未知";
        switch (payType) {
            case 1: return "支付宝";
            case 2: return "微信";
            case 3: return "余额";
            default: return "未支付";
        }
    }

    private String getOrderStatusText(Integer orderStatus) {
        if (orderStatus == null) return "未知";
        switch (orderStatus) {
            case 0: return "待支付";
            case 1: return "已完成";
            case 2: return "已取消";
            default: return "未知";
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, Integer orderStatus) {
        if (orderStatus == null || orderStatus < 0 || orderStatus > 2) {
            throw new BusinessException("无效的订单状态，合法值：0=待支付 1=已完成 2=已取消");
        }
        // 确认订单存在
        AdminOrderDetailVO detail = adminOrderMapper.selectOrderDetailById(orderId);
        if (detail == null) {
            throw new BusinessException("订单不存在");
        }
        int rows = adminOrderMapper.updateOrderStatus(orderId, orderStatus);
        if (rows == 0) {
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        AdminOrderDetailVO detail = adminOrderMapper.selectOrderDetailById(orderId);
        if (detail == null) {
            throw new BusinessException("订单不存在");
        }
        int rows = adminOrderMapper.deleteOrder(orderId);
        if (rows == 0) {
            throw new BusinessException("删除失败");
        }
    }

    @Override
    public void batchDeleteOrders(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            throw new BusinessException("订单ID列表不能为空");
        }
        adminOrderMapper.batchDeleteOrders(orderIds);
    }
}
