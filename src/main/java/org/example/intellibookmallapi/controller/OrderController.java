package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.CreateOrderParam;
import org.example.intellibookmallapi.dto.OrderVO;
import org.example.intellibookmallapi.dto.OrderDetailVO;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.service.OrderService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单（支持两种方式）
     * 方式1：从购物车创建 - 传入cartIds
     * 方式2：直接购买 - 传入bookIds
     * @param param 创建订单参数
     * @param user 当前登录用户
     * @return 订单号
     */
    @PostMapping("/create")
    public Result createOrder(@RequestBody CreateOrderParam param,
                             @TokenToUser User user) {
        Long userId = user.getUserId();
        String orderNo = orderService.createOrder(userId, param);
        
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        
        return ResultGenerator.genSuccessResult(data);
    }
    
    /**
     * 立即购买（快捷方式，直接购买单本书）
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 订单号
     */
    @PostMapping("/buy-now/{bookId}")
    public Result buyNow(@PathVariable("bookId") Long bookId,
                        @TokenToUser User user) {
        Long userId = user.getUserId();
        
        // 构建参数
        CreateOrderParam param = new CreateOrderParam();
        List<Long> bookIds = new ArrayList<>();
        bookIds.add(bookId);
        param.setBookIds(bookIds);
        param.setRemark("立即购买");
        
        String orderNo = orderService.createOrder(userId, param);
        
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        
        return ResultGenerator.genSuccessResult(data);
    }
    
    /**
     * 查询订单列表（分页）
     * @param page 页码
     * @param pageSize 每页数量
     * @param user 当前登录用户
     * @return 订单列表
     */
    @GetMapping("/list")
    public Result getOrderList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              @TokenToUser User user) {
        Long userId = user.getUserId();
        PageResult<OrderVO> pageResult = orderService.getOrderList(userId, page, pageSize);
        
        return ResultGenerator.genSuccessResult(pageResult);
    }
    
    /**
     * 查询订单详情
     * @param orderId 订单ID
     * @param user 当前登录用户
     * @return 订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Result getOrderDetail(@PathVariable("orderId") Long orderId,
                                 @TokenToUser User user) {
        Long userId = user.getUserId();
        OrderDetailVO orderDetail = orderService.getOrderDetail(userId, orderId);
        
        return ResultGenerator.genSuccessResult(orderDetail);
    }
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PutMapping("/cancel/{orderId}")
    public Result cancelOrder(@PathVariable("orderId") Long orderId,
                             @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = orderService.cancelOrder(userId, orderId);
        
        if (success) {
            return ResultGenerator.genSuccessResult("取消成功");
        } else {
            return ResultGenerator.genFailResult("取消失败");
        }
    }
    
    /**
     * 支付订单（模拟支付）
     * @param orderId 订单ID
     * @param payType 支付方式（1-支付宝 2-微信 3-余额）
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PutMapping("/pay/{orderId}")
    public Result payOrder(@PathVariable("orderId") Long orderId,
                          @RequestParam(value = "payType", defaultValue = "1") Integer payType,
                          @TokenToUser User user) {
        Long userId = user.getUserId();
        boolean success = orderService.payOrder(userId, orderId, payType);
        
        if (success) {
            return ResultGenerator.genSuccessResult("支付成功");
        } else {
            return ResultGenerator.genFailResult("支付失败");
        }
    }
}
