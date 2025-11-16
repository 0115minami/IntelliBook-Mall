package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.List;

/**
 * 创建订单请求参数
 */
@Data
public class CreateOrderParam {
    
    /**
     * 购物车ID列表（从购物车创建订单）
     * 与bookIds二选一
     */
    private List<Long> cartIds;
    
    /**
     * 书籍ID列表（直接购买，不经过购物车）
     * 与cartIds二选一
     */
    private List<Long> bookIds;
    
    /**
     * 备注信息
     */
    private String remark;
}
