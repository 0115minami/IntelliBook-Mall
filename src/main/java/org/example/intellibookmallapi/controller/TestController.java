package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于验证文件存储配置是否正确
 * 
 * @author IntelliBook Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private FileStorageProperties fileStorageProperties;
    
    @Value("${ebook.file.url-prefix}")
    private String urlPrefix;
    
    /**
     * 测试接口 - 验证应用是否正常运行
     * 
     * @return 欢迎信息
     */
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "欢迎使用 IntelliBook-Mall API");
        result.put("status", "running");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    /**
     * 文件存储配置信息
     * 用于检查文件存储配置是否正确加载
     * 
     * @return 配置信息
     */
    @GetMapping("/storage-config")
    public Map<String, Object> getStorageConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("basePath", fileStorageProperties.getBasePath());
        result.put("coverPath", fileStorageProperties.getCoverPath());
        result.put("bookPath", fileStorageProperties.getBookPath());
        result.put("tempPath", fileStorageProperties.getTempPath());
        result.put("urlPrefix", urlPrefix);
        
        // 检查目录是否存在
        File baseDir = new File(fileStorageProperties.getBasePath());
        result.put("basePathExists", baseDir.exists());
        result.put("basePathAbsolute", baseDir.getAbsolutePath());
        
        // 检查子目录
        Map<String, Boolean> directories = new HashMap<>();
        directories.put("covers", new File(fileStorageProperties.getCoverPath()).exists());
        directories.put("books/pdf", new File(fileStorageProperties.getBookPath() + "/pdf").exists());
        directories.put("books/epub", new File(fileStorageProperties.getBookPath() + "/epub").exists());
        directories.put("books/mobi", new File(fileStorageProperties.getBookPath() + "/mobi").exists());
        directories.put("temp/uploads", new File(fileStorageProperties.getTempPath() + "/uploads").exists());
        result.put("directories", directories);
        
        return result;
    }
    
    /**
     * 文件访问测试说明
     * 
     * @return 测试说明
     */
    @GetMapping("/file-access-guide")
    public Map<String, Object> getFileAccessGuide() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "文件访问测试指南");
        
        Map<String, String> examples = new HashMap<>();
        examples.put("封面图片", "http://localhost:8080" + urlPrefix + "/covers/1.jpg");
        examples.put("PDF电子书", "http://localhost:8080" + urlPrefix + "/books/pdf/1.pdf");
        examples.put("EPUB电子书", "http://localhost:8080" + urlPrefix + "/books/epub/4.epub");
        result.put("examples", examples);
        
        Map<String, String> steps = new HashMap<>();
        steps.put("1", "准备测试文件（图片和电子书）");
        steps.put("2", "按照命名规范放置文件到对应目录");
        steps.put("3", "访问上述示例 URL 测试文件是否可以访问");
        steps.put("4", "如果无法访问，检查文件路径和配置是否正确");
        result.put("steps", steps);
        
        return result;
    }
}
