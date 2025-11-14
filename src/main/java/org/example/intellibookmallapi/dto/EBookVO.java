package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 电子书VO（返回给前端）
 */
@Data
public class EBookVO {
    
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
    private String[] tags; // 标签数组
    private Double rating;
    private Integer ratingCount;
    
    /**
     * 可用的文件格式列表
     */
    private List<FileFormatVO> availableFormats;
    
    @Data
    public static class FileFormatVO {
        private Long fileId;
        private String format;
        private Long fileSize;
        private String fileSizeText; // 格式化后的文件大小，如 "12.5 MB"
    }
    
    /**
     * 从 EBook 实体转换为 VO
     */
    public static EBookVO fromEntity(org.example.intellibookmallapi.entity.EBook ebook) {
        if (ebook == null) {
            return null;
        }
        
        EBookVO vo = new EBookVO();
        vo.setBookId(ebook.getBookId());
        vo.setBookTitle(ebook.getBookTitle());
        vo.setAuthor(ebook.getAuthor());
        vo.setIsbn(ebook.getIsbn());
        vo.setPublisher(ebook.getPublisher());
        vo.setPublishDate(ebook.getPublishDate());
        vo.setBookIntro(ebook.getBookIntro());
        vo.setCategoryId(ebook.getCategoryId());
        vo.setCategoryName(ebook.getCategoryName());
        vo.setCoverImg(ebook.getCoverImg());
        vo.setPageCount(ebook.getPageCount());
        vo.setPrice(ebook.getPrice());
        vo.setLanguage(ebook.getLanguage());
        vo.setRating(ebook.getRating());
        vo.setRatingCount(ebook.getRatingCount());
        
        // 处理标签
        if (ebook.getTags() != null && !ebook.getTags().trim().isEmpty()) {
            vo.setTags(ebook.getTags().split(","));
        } else {
            vo.setTags(new String[0]);
        }
        
        // 处理文件格式
        if (ebook.getFiles() != null && !ebook.getFiles().isEmpty()) {
            List<FileFormatVO> formats = new java.util.ArrayList<>();
            for (org.example.intellibookmallapi.entity.EBookFile file : ebook.getFiles()) {
                FileFormatVO format = new FileFormatVO();
                format.setFileId(file.getFileId());
                format.setFormat(file.getFileFormat());
                format.setFileSize(file.getFileSize());
                format.setFileSizeText(formatFileSize(file.getFileSize()));
                formats.add(format);
            }
            vo.setAvailableFormats(formats);
        }
        
        return vo;
    }
    
    /**
     * 格式化文件大小
     */
    private static String formatFileSize(Long size) {
        if (size == null || size == 0) {
            return "0 B";
        }
        
        final String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double fileSize = size.doubleValue();
        
        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", fileSize, units[unitIndex]);
    }
}
