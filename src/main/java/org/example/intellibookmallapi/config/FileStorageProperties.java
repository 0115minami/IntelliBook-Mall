package org.example.intellibookmallapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储配置属性类
 * 从 application.properties 读取文件存储相关配置
 * 
 * @author IntelliBook Team
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "ebook.storage")
public class FileStorageProperties {
    
    /**
     * 文件存储根目录
     */
    private String basePath;
    
    /**
     * 封面图片存储路径
     */
    private String coverPath;
    
    /**
     * 电子书文件存储路径
     */
    private String bookPath;
    
    /**
     * 临时文件存储路径
     */
    private String tempPath;
    
    // Getters and Setters
    
    public String getBasePath() {
        return basePath;
    }
    
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    public String getCoverPath() {
        return coverPath;
    }
    
    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
    
    public String getBookPath() {
        return bookPath;
    }
    
    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }
    
    public String getTempPath() {
        return tempPath;
    }
    
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }
    
    @Override
    public String toString() {
        return "FileStorageProperties{" +
                "basePath='" + basePath + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", bookPath='" + bookPath + '\'' +
                ", tempPath='" + tempPath + '\'' +
                '}';
    }
}
