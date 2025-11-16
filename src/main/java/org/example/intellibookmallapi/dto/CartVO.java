package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 购物车返回对象（包含书籍详细信息）
 */
@Data
public class CartVO {
    
    /**
     * 购物车ID
     */
    private Long cartId;
    
    /**
     * 电子书ID
     */
    private Long bookId;
    
    /**
     * 书名
     */
    private String bookTitle;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 封面图片
     */
    private String coverImg;
    
    /**
     * 价格（分）
     */
    private Integer price;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 状态（1-上架 0-下架）
     */
    private Integer status;
    
    /**
     * 添加到购物车的时间
     */
    private Date createTime;
}
