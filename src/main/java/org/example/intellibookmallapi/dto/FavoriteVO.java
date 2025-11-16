package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 收藏返回对象（包含书籍详细信息）
 */
@Data
public class FavoriteVO {
    
    /**
     * 收藏ID
     */
    private Long favoriteId;
    
    /**
     * 书籍ID
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
     * 评分
     */
    private Double rating;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 收藏时间
     */
    private Date createTime;
}
