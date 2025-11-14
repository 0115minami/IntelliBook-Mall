package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 电子书实体类
 */
@Data
public class EBook {
    
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
     * ISBN
     */
    private String isbn;
    
    /**
     * 出版社
     */
    private String publisher;
    
    /**
     * 出版日期
     */
    private Date publishDate;
    
    /**
     * 简介
     */
    private String bookIntro;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称（关联查询）
     */
    private String categoryName;
    
    /**
     * 封面图片路径
     */
    private String coverImg;
    
    /**
     * 页数
     */
    private Integer pageCount;
    
    /**
     * 价格（分）
     */
    private Integer price;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 标签（逗号分隔）
     */
    private String tags;
    
    /**
     * 评分
     */
    private Double rating;
    
    /**
     * 评分人数
     */
    private Integer ratingCount;
    
    /**
     * 状态（1-上架 0-下架）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 电子书文件列表（关联查询）
     */
    private List<EBookFile> files;
    
    /**
     * 可用文件格式（逗号分隔，用于简单查询）
     */
    private String availableFormats;
    
    /**
     * 获取标签数组
     */
    public String[] getTagArray() {
        if (tags == null || tags.trim().isEmpty()) {
            return new String[0];
        }
        return tags.split(",");
    }
    
    /**
     * 检查是否上架
     */
    public boolean isOnSale() {
        return status != null && status == 1;
    }
}
