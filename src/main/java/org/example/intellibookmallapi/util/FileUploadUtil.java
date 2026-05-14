package org.example.intellibookmallapi.util;

import lombok.Data;
import org.example.intellibookmallapi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传工具类
 */
@Component
public class FileUploadUtil {
    
    private static String basePath = "ebook-storage";
    
    // 支持的封面图片格式
    private static final List<String> IMAGE_FORMATS = Arrays.asList("jpg", "jpeg", "png");
    
    // 支持的电子书格式
    private static final List<String> EBOOK_FORMATS = Arrays.asList("pdf", "epub", "mobi");
    
    // 封面图片最大大小：5MB
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    
    // 电子书最大大小：100MB
    private static final long MAX_EBOOK_SIZE = 100 * 1024 * 1024;
    
    /**
     * 电子书文件上传结果
     */
    @Data
    public static class EBookFileResult {
        private String filePath;
        private Long fileSize;
    }
    
    /**
     * 上传封面图片
     * 
     * @param file 图片文件
     * @return 文件相对路径
     */
    public static String uploadCoverImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("封面图片不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BusinessException("封面图片大小不能超过5MB");
        }
        
        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        
        // 验证文件格式
        if (!IMAGE_FORMATS.contains(extension.toLowerCase())) {
            throw new BusinessException("封面图片格式不支持，仅支持：" + String.join(", ", IMAGE_FORMATS));
        }
        
        // 生成临时文件名，后续会根据bookId重命名
        String filename = System.currentTimeMillis() + "." + extension;
        String relativePath = "covers/" + filename;
        
        // 保存文件
        saveFile(file, relativePath);
        
        return relativePath;
    }
    
    /**
     * 重命名封面图片（在获得bookId后）
     */
    public static String renameCoverImage(String tempPath, Long bookId) {
        if (tempPath == null || bookId == null) {
            return tempPath;
        }
        
        try {
            Path oldPath = Paths.get(basePath, tempPath);
            if (!Files.exists(oldPath)) {
                return tempPath;
            }
            
            String extension = getFileExtension(tempPath);
            String newFilename = bookId + "." + extension;
            String newRelativePath = "covers/" + newFilename;
            Path newPath = Paths.get(basePath, newRelativePath);
            
            Files.move(oldPath, newPath);
            return newRelativePath;
        } catch (IOException e) {
            // 重命名失败，返回原路径
            return tempPath;
        }
    }
    
    /**
     * 上传电子书文件
     * 
     * @param bookId 图书ID
     * @param format 文件格式
     * @param file 电子书文件
     * @return 上传结果
     */
    public static EBookFileResult uploadEBookFile(Long bookId, String format, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("电子书文件不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_EBOOK_SIZE) {
            throw new BusinessException("电子书文件大小不能超过100MB");
        }
        
        // 验证文件格式
        String formatLower = format.toLowerCase();
        if (!EBOOK_FORMATS.contains(formatLower)) {
            throw new BusinessException("电子书格式不支持，仅支持：" + String.join(", ", EBOOK_FORMATS));
        }
        
        // 生成文件名：{book_id}.{format}
        String filename = bookId + "." + formatLower;
        String relativePath = "books/" + formatLower + "/" + filename;
        
        // 保存文件
        saveFile(file, relativePath);
        
        EBookFileResult result = new EBookFileResult();
        result.setFilePath(relativePath);
        result.setFileSize(file.getSize());
        
        return result;
    }
    
    /**
     * 删除封面图片
     * 
     * @param relativePath 文件相对路径
     */
    public static void deleteCoverImage(String relativePath) {
        deleteFile(relativePath);
    }
    
    /**
     * 删除电子书文件
     * 
     * @param relativePath 文件相对路径
     */
    public static void deleteEBookFile(String relativePath) {
        deleteFile(relativePath);
    }
    
    /**
     * 删除文件
     * 
     * @param relativePath 文件相对路径
     */
    public static void deleteFile(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return;
        }
        
        try {
            Path filePath = Paths.get(basePath, relativePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // 删除失败不抛异常，只记录日志
            System.err.println("删除文件失败：" + relativePath + ", 错误：" + e.getMessage());
        }
    }
    
    /**
     * 保存文件
     * 
     * @param file 文件
     * @param relativePath 相对路径
     */
    private static void saveFile(MultipartFile file, String relativePath) {
        try {
            Path filePath = Paths.get(basePath, relativePath);
            
            // 创建目录
            Files.createDirectories(filePath.getParent());
            
            // 保存文件
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new BusinessException("文件保存失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param filename 文件名
     * @return 扩展名（小写）
     */
    private static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        
        return "";
    }
}
