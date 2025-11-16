package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.Order;
import org.example.intellibookmallapi.dto.OrderVO;
import org.example.intellibookmallapi.dto.OrderDetailVO;

import java.util.List;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper {
    
    /**
     * 插入订单
     */
    int insert(Order order);
    
    /**
     * 根据订单ID查询订单
     */
    Order selectByPrimaryKey(@Param("orderId") Long orderId);
    
    /**
     * 根据订单号查询订单
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 查询用户的订单列表（分页）
     */
    List<OrderVO> selectOrderListByUserId(@Param("userId") Long userId, 
                                          @Param("offset") Integer offset, 
                                          @Param("limit") Integer limit);
    
    /**
     * 统计用户的订单数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 查询订单详情（包含订单项）
     */
    OrderDetailVO selectOrderDetailById(@Param("orderId") Long orderId, 
                                        @Param("userId") Long userId);
    
    /**
     * 更新订单状态
     */
    int updateOrderStatus(@Param("orderId") Long orderId, 
                         @Param("orderStatus") Integer orderStatus);
    
    /**
     * 更新支付信息
     */
    int updatePayInfo(@Param("orderId") Long orderId, 
                     @Param("payStatus") Integer payStatus,
                     @Param("payType") Integer payType);
    
    /**
     * 取消订单
     */
    int cancelOrder(@Param("orderId") Long orderId, 
                   @Param("userId") Long userId);
}
