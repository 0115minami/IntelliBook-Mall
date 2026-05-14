package org.example.intellibookmallapi.dto;

import lombok.Data;
import org.example.intellibookmallapi.entity.EBookFile;

import java.util.Date;
import java.util.List;

/**
 * 管理员图书视图对象
 */
@Data
public class AdminEBookVO {
    
    private Long bookId;
    private String bookTitle;
    private String author;
    private String isbn;
    private String publisher;
    private Date publishDate;
    private String bookIntro;
    private Long categoryId;
    private String categoryName;
    private String coverImg;
    private Integer pageCount;
    private Integer price;
    private String language;
    private String tags;
    private Double rating;
    private Integer ratingCount;
    private Integer viewCount;
    private Integer downloadCount;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    /**
     * 电子书文件列表
     */
    private List<EBookFile> files;
}
