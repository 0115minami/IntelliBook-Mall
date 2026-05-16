package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 书架图书VO
 * 用于展示用户已购买的图书信息
 */
@Data
public class BookshelfVO {
    
    /**
     * 图书ID
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
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 价格（分）
     */
    private Integer price;
    
    /**
     * 评分
     */
    private Double rating;
    
    /**
     * 评分人数
     */
    private Integer ratingCount;
    
    /**
     * 购买时间
     */
    private Date purchaseTime;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 最后阅读时间
     */
    private Date lastReadTime;
    
    /**
     * 最后阅读格式
     */
    private String lastReadFormat;
    
    /**
     * 最后阅读位置（JSON格式）
     */
    private String lastPosition;
    
    /**
     * 是否有阅读记录
     */
    private Boolean hasReadingProgress;
    
    /**
     * 可用文件格式（逗号分隔）
     */
    private String availableFormats;
}
