package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.OrderItem;
import org.example.intellibookmallapi.dto.OrderDetailVO;

import java.util.List;

/**
 * 订单项Mapper接口
 */
@Mapper
public interface OrderItemMapper {
    
    /**
     * 批量插入订单项
     */
    int batchInsert(@Param("items") List<OrderItem> items);
    
    /**
     * 根据订单ID查询订单项列表
     */
    List<OrderDetailVO.OrderItemVO> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 统计订单的商品数量
     */
    int countByOrderId(@Param("orderId") Long orderId);
}
