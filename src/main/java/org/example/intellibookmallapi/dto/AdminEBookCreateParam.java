package org.example.intellibookmallapi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 管理员创建图书参数
 */
@Data
public class AdminEBookCreateParam {
    
    /**
     * 书名（必填）
     */
    private String bookTitle;
    
    /**
     * 作者（必填）
     */
    private String author;
    
    /**
     * ISBN（可选）
     */
    private String isbn;
    
    /**
     * 出版社（可选）
     */
    private String publisher;
    
    /**
     * 出版日期（可选）
     */
    private Date publishDate;
    
    /**
     * 简介（可选）
     */
    private String bookIntro;
    
    /**
     * 分类ID（必填）
     */
    private Long categoryId;
    
    /**
     * 价格/分（必填）
     */
    private Integer price;
    
    /**
     * 语言（默认zh-CN）
     */
    private String language = "zh-CN";
    
    /**
     * 标签（可选，逗号分隔）
     */
    private String tags;
    
    /**
     * 页数（可选）
     */
    private Integer pageCount;
    
    /**
     * 封面图片文件（必填）
     */
    private MultipartFile coverImage;
    
    /**
     * 电子书文件列表（必填，至少1个）
     */
    private List<EBookFileParam> ebookFiles;
    
    @Data
    public static class EBookFileParam {
        /**
         * 文件格式（PDF/EPUB/MOBI）
         */
        private String fileFormat;
        
        /**
         * 文件内容
         */
        private MultipartFile file;
    }
}
