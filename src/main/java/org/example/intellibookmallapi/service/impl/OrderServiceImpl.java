package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.CartVO;
import org.example.intellibookmallapi.dto.CreateOrderParam;
import org.example.intellibookmallapi.dto.OrderVO;
import org.example.intellibookmallapi.dto.OrderDetailVO;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.entity.Order;
import org.example.intellibookmallapi.entity.OrderItem;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.CartMapper;
import org.example.intellibookmallapi.mapper.EBookMapper;
import org.example.intellibookmallapi.mapper.OrderMapper;
import org.example.intellibookmallapi.mapper.OrderItemMapper;
import org.example.intellibookmallapi.service.OrderService;
import org.example.intellibookmallapi.util.OrderNoGenerator;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private EBookMapper eBookMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(Long userId, CreateOrderParam param) {
        // 1. 验证参数（cartIds和bookIds二选一）
        boolean hasCartIds = param.getCartIds() != null && !param.getCartIds().isEmpty();
        boolean hasBookIds = param.getBookIds() != null && !param.getBookIds().isEmpty();
        
        if (!hasCartIds && !hasBookIds) {
            throw new BusinessException("请选择要购买的商品");
        }
        
        if (hasCartIds && hasBookIds) {
            throw new BusinessException("不能同时使用购物车和直接购买方式");
        }
        
        List<OrderItem> orderItems = new ArrayList<>();
        int totalPrice = 0;
        
        // 2. 根据不同方式创建订单
        if (hasCartIds) {
            // 方式1：从购物车创建订单
            totalPrice = createOrderFromCart(userId, param.getCartIds(), orderItems);
        } else {
            // 方式2：直接购买（不经过购物车）
            totalPrice = createOrderFromBooks(userId, param.getBookIds(), orderItems);
        }
        
        // 3. 创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoGenerator.generateOrderNo());
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setPayStatus(0); // 未支付
        order.setPayType(0); // 未支付
        order.setOrderStatus(0); // 待支付
        order.setExtraInfo(param.getRemark());
        
        int orderResult = orderMapper.insert(order);
        if (orderResult == 0) {
            throw new BusinessException("创建订单失败");
        }
        
        // 4. 设置订单ID并批量插入订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getOrderId());
        }
        
        int itemResult = orderItemMapper.batchInsert(orderItems);
        if (itemResult == 0) {
            throw new BusinessException("创建订单项失败");
        }
        
        // 5. 如果是从购物车创建，删除购物车中的商品
        if (hasCartIds) {
            for (Long cartId : param.getCartIds()) {
                cartMapper.deleteByCartId(cartId);
            }
        }
        
        return order.getOrderNo();
    }
    
    /**
     * 从购物车创建订单
     */
    private int createOrderFromCart(Long userId, List<Long> cartIds, List<OrderItem> orderItems) {
        // 查询购物车商品信息
        List<CartVO> cartList = cartMapper.selectCartListByUserId(userId);
        if (cartList == null || cartList.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        
        // 过滤出要购买的商品
        Map<Long, CartVO> cartMap = cartList.stream()
            .collect(Collectors.toMap(CartVO::getCartId, cart -> cart));
        
        List<CartVO> selectedCarts = new ArrayList<>();
        for (Long cartId : cartIds) {
            CartVO cart = cartMap.get(cartId);
            if (cart == null) {
                throw new BusinessException("购物车商品不存在：" + cartId);
            }
            if (cart.getStatus() == 0) {
                throw new BusinessException("商品已下架：" + cart.getBookTitle());
            }
            selectedCarts.add(cart);
        }
        
        // 计算总价并创建订单项
        int totalPrice = 0;
        for (CartVO cart : selectedCarts) {
            OrderItem item = new OrderItem();
            item.setBookId(cart.getBookId());
            item.setBookTitle(cart.getBookTitle());
            item.setPrice(cart.getPrice());
            orderItems.add(item);
            totalPrice += cart.getPrice();
        }
        
        return totalPrice;
    }
    
    /**
     * 直接购买（不经过购物车）
     */
    private int createOrderFromBooks(Long userId, List<Long> bookIds, List<OrderItem> orderItems) {
        int totalPrice = 0;
        
        for (Long bookId : bookIds) {
            // 查询书籍信息
            EBook book = eBookMapper.selectByPrimaryKey(bookId);
            if (book == null) {
                throw new BusinessException("书籍不存在：ID=" + bookId);
            }
            if (book.getStatus() == null || book.getStatus() == 0) {
                throw new BusinessException("书籍已下架：" + book.getBookTitle());
            }
            
            // 创建订单项
            OrderItem item = new OrderItem();
            item.setBookId(book.getBookId());
            item.setBookTitle(book.getBookTitle());
            item.setPrice(book.getPrice());
            orderItems.add(item);
            totalPrice += book.getPrice();
        }
        
        return totalPrice;
    }
    
    @Override
    public PageResult<OrderVO> getOrderList(Long userId, Integer page, Integer pageSize) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }
        
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询订单列表
        List<OrderVO> list = orderMapper.selectOrderListByUserId(userId, offset, pageSize);
        
        // 设置状态文本
        for (OrderVO orderVO : list) {
            orderVO.setPayStatusText(getPayStatusText(orderVO.getPayStatus()));
            orderVO.setOrderStatusText(getOrderStatusText(orderVO.getOrderStatus()));
        }
        
        // 查询总数
        int total = orderMapper.countByUserId(userId);
        
        // 构建分页结果
        PageResult<OrderVO> pageResult = new PageResult<>();
        pageResult.setList(list);
        pageResult.setTotalCount((long) total);
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages((int) Math.ceil((double) total / pageSize));
        pageResult.setHasPrevious(page > 1);
        pageResult.setHasNext(page < pageResult.getTotalPages());
        
        return pageResult;
    }
    
    @Override
    public OrderDetailVO getOrderDetail(Long userId, Long orderId) {
        // 查询订单基本信息
        OrderDetailVO orderDetail = orderMapper.selectOrderDetailById(orderId, userId);
        if (orderDetail == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 设置状态文本
        orderDetail.setPayStatusText(getPayStatusText(orderDetail.getPayStatus()));
        orderDetail.setPayTypeText(getPayTypeText(orderDetail.getPayType()));
        orderDetail.setOrderStatusText(getOrderStatusText(orderDetail.getOrderStatus()));
        
        // 查询订单项
        List<OrderDetailVO.OrderItemVO> items = orderItemMapper.selectByOrderId(orderId);
        orderDetail.setItems(items);
        
        return orderDetail;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long userId, Long orderId) {
        int result = orderMapper.cancelOrder(orderId, userId);
        if (result == 0) {
            throw new BusinessException("取消订单失败，订单不存在或已支付");
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long userId, Long orderId, Integer payType) {
        // 查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }
        if (order.getPayStatus() == 1) {
            throw new BusinessException("订单已支付");
        }
        if (order.getOrderStatus() == 2) {
            throw new BusinessException("订单已取消");
        }
        
        // 更新支付信息
        int payResult = orderMapper.updatePayInfo(orderId, 1, payType);
        if (payResult == 0) {
            throw new BusinessException("支付失败");
        }
        
        // 更新订单状态为已完成
        int statusResult = orderMapper.updateOrderStatus(orderId, 1);
        if (statusResult == 0) {
            throw new BusinessException("更新订单状态失败");
        }
        
        return true;
    }
    
    /**
     * 获取支付状态文本
     */
    private String getPayStatusText(Integer payStatus) {
        if (payStatus == null) return "未知";
        switch (payStatus) {
            case 0: return "未支付";
            case 1: return "已支付";
            default: return "未知";
        }
    }
    
    /**
     * 获取支付方式文本
     */
    private String getPayTypeText(Integer payType) {
        if (payType == null) return "未知";
        switch (payType) {
            case 0: return "未支付";
            case 1: return "支付宝";
            case 2: return "微信";
            case 3: return "余额";
            default: return "未知";
        }
    }
    
    /**
     * 获取订单状态文本
     */
    private String getOrderStatusText(Integer orderStatus) {
        if (orderStatus == null) return "未知";
        switch (orderStatus) {
            case 0: return "待支付";
            case 1: return "已完成";
            case 2: return "已取消";
            default: return "未知";
        }
    }
}
