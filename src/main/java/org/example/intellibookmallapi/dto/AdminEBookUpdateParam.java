package org.example.intellibookmallapi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 管理员更新图书参数
 */
@Data
public class AdminEBookUpdateParam {
    
    /**
     * 书名（可选）
     */
    private String bookTitle;
    
    /**
     * 作者（可选）
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
     * 分类ID（可选）
     */
    private Long categoryId;
    
    /**
     * 价格/分（可选）
     */
    private Integer price;
    
    /**
     * 语言（可选）
     */
    private String language;
    
    /**
     * 标签（可选）
     */
    private String tags;
    
    /**
     * 页数（可选）
     */
    private Integer pageCount;
    
    /**
     * 新封面图片（可选）
     */
    private MultipartFile coverImage;
    
    /**
     * 新增电子书文件（可选）
     */
    private List<EBookFileParam> newEbookFiles;
    
    /**
     * 删除的文件ID数组（可选）
     */
    private List<Long> deleteFileIds;
    
    /**
     * 封面图片路径（内部使用）
     */
    private String coverImg;
    
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
