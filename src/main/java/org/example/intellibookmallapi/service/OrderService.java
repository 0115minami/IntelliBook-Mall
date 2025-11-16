package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.CreateOrderParam;
import org.example.intellibookmallapi.dto.OrderVO;
import org.example.intellibookmallapi.dto.OrderDetailVO;
import org.example.intellibookmallapi.util.PageResult;

/**
 * 订单Service接口
 */
public interface OrderService {
    
    /**
     * 创建订单（从购物车）
     * @param userId 用户ID
     * @param param 创建订单参数
     * @return 订单号
     */
    String createOrder(Long userId, CreateOrderParam param);
    
    /**
     * 查询用户的订单列表（分页）
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 订单列表
     */
    PageResult<OrderVO> getOrderList(Long userId, Integer page, Integer pageSize);
    
    /**
     * 查询订单详情
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailVO getOrderDetail(Long userId, Long orderId);
    
    /**
     * 取消订单
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean cancelOrder(Long userId, Long orderId);
    
    /**
     * 支付订单（模拟支付）
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param payType 支付方式
     * @return 是否成功
     */
    boolean payOrder(Long userId, Long orderId, Integer payType);
}
