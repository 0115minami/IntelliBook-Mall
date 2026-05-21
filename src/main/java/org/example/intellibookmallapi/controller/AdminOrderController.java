package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.annotation.RequireAdmin;
import org.example.intellibookmallapi.dto.AdminDashboardVO;
import org.example.intellibookmallapi.dto.AdminOrderDetailVO;
import org.example.intellibookmallapi.dto.AdminOrderSearchParam;
import org.example.intellibookmallapi.dto.AdminOrderVO;
import org.example.intellibookmallapi.service.AdminOrderService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员订单管理Controller
 * 路径前缀 /api/admin/orders，由 AdminInterceptor 统一鉴权
 */
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    /**
     * 仪表盘统计数据
     * GET /api/admin/orders/dashboard
     */
    @RequireAdmin
    @GetMapping("/dashboard")
    public Result<AdminDashboardVO> getDashboard() {
        return ResultGenerator.genSuccessResult(adminOrderService.getDashboard());
    }

    /**
     * 分页查询订单列表
     * GET /api/admin/orders?keyword=&payStatus=-1&orderStatus=-1&startDate=&endDate=&pageNum=1&pageSize=10
     */
    @RequireAdmin
    @GetMapping
    public Result<PageResult<AdminOrderVO>> getOrderList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "-1") Integer payStatus,
            @RequestParam(required = false, defaultValue = "-1") Integer orderStatus,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        AdminOrderSearchParam param = new AdminOrderSearchParam();
        param.setKeyword(keyword);
        param.setPayStatus(payStatus);
        param.setOrderStatus(orderStatus);
        param.setStartDate(startDate);
        param.setEndDate(endDate);
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);

        return ResultGenerator.genSuccessResult(adminOrderService.getOrderList(param));
    }

    /**
     * 查询订单详情
     * GET /api/admin/orders/{orderId}
     */
    @RequireAdmin
    @GetMapping("/{orderId}")
    public Result<AdminOrderDetailVO> getOrderDetail(@PathVariable Long orderId) {
        return ResultGenerator.genSuccessResult(adminOrderService.getOrderDetail(orderId));
    }

    /**
     * 修改订单状态（管理员强制变更）
     * PATCH /api/admin/orders/{orderId}/status
     * Body: { "orderStatus": 1 }
     * orderStatus: 0=待支付 1=已完成 2=已取消
     */
    @RequireAdmin
    @PatchMapping("/{orderId}/status")
    public Result<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, Integer> body) {
        Integer orderStatus = body.get("orderStatus");
        adminOrderService.updateOrderStatus(orderId, orderStatus);
        return ResultGenerator.genSuccessResult("状态更新成功");
    }

    /**
     * 逻辑删除单条订单
     * DELETE /api/admin/orders/{orderId}
     */
    @RequireAdmin
    @DeleteMapping("/{orderId}")
    public Result<String> deleteOrder(@PathVariable Long orderId) {
        adminOrderService.deleteOrder(orderId);
        return ResultGenerator.genSuccessResult("删除成功");
    }

    /**
     * 批量逻辑删除订单
     * DELETE /api/admin/orders/batch
     * Body: { "orderIds": [1, 2, 3] }
     */
    @RequireAdmin
    @DeleteMapping("/batch")
    public Result<String> batchDeleteOrders(@RequestBody Map<String, List<Long>> body) {
        List<Long> orderIds = body.get("orderIds");
        adminOrderService.batchDeleteOrders(orderIds);
        return ResultGenerator.genSuccessResult("批量删除成功");
    }
}
