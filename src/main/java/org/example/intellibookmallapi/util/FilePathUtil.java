package org.example.intellibookmallapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文件路径工具类
 * 提供统一的文件路径生成策略
 * 
 * @author IntelliBook Team
 * @since 1.0.0
 */
public class FilePathUtil {
    
    /**
     * 路径生成策略枚举
     */
    public enum PathStrategy {
        FLAT,           // 扁平结构：covers/1.jpg
        DATE_BASED      // 按日期分目录：covers/2024/01/1.jpg
    }
    
    // 当前使用的策略（可通过配置文件控制）
    private static final PathStrategy CURRENT_STRATEGY = PathStrategy.FLAT;
    
    /**
     * 生成封面图片路径
     * 
     * @param bookId 书籍ID
     * @param extension 文件扩展名（jpg, png）
     * @return 相对路径
     */
    public static String generateCoverPath(Long bookId, String extension) {
        switch (CURRENT_STRATEGY) {
            case DATE_BASED:
                LocalDate now = LocalDate.now();
                return String.format("covers/%d/%02d/%d.%s", 
                    now.getYear(), now.getMonthValue(), bookId, extension);
            case FLAT:
            default:
                return String.format("covers/%d.%s", bookId, extension);
        }
    }
    
    /**
     * 生成电子书文件路径
     * 
     * @param bookId 书籍ID
     * @param format 文件格式（pdf, epub, mobi）
     * @return 相对路径
     */
    public static String generateBookPath(Long bookId, String format) {
        String formatLower = format.toLowerCase();
        
        switch (CURRENT_STRATEGY) {
            case DATE_BASED:
                LocalDate now = LocalDate.now();
                return String.format("books/%s/%d/%02d/%d.%s", 
                    formatLower, now.getYear(), now.getMonthValue(), bookId, formatLower);
            case FLAT:
            default:
                return String.format("books/%s/%d.%s", formatLower, bookId, formatLower);
        }
    }
    
    /**
     * 生成临时文件路径
     * 
     * @param originalFilename 原始文件名
     * @return 临时文件路径
     */
    public static String generateTempPath(String originalFilename) {
        long timestamp = System.currentTimeMillis();
        return String.format("temp/uploads/%d_%s", timestamp, originalFilename);
    }
    
    /**
     * 从完整路径中提取文件名
     * 
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }
        
        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < filePath.length() - 1) {
            return filePath.substring(lastSlash + 1);
        }
        
        return filePath;
    }
    
    /**
     * 验证文件扩展名是否支持
     * 
     * @param filename 文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return 是否支持
     */
    public static boolean isValidExtension(String filename, String... allowedExtensions) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        
        String extension = getFileExtension(filename).toLowerCase();
        
        for (String allowed : allowedExtensions) {
            if (allowed.toLowerCase().equals(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param filename 文件名
     * @return 扩展名（不包含点号）
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1) {
            return filename.substring(lastDot + 1);
        }
        
        return "";
    }
    
    /**
     * 支持的封面图片格式
     */
    public static final String[] SUPPORTED_IMAGE_FORMATS = {"jpg", "jpeg", "png"};
    
    /**
     * 支持的电子书格式
     */
    public static final String[] SUPPORTED_EBOOK_FORMATS = {"pdf", "epub", "mobi"};
}