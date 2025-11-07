package org.example.intellibookmallapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件访问配置类
 * 配置静态资源访问路径，使电子书文件和封面图片可以通过 HTTP 访问
 * 
 * @author IntelliBook Team
 * @since 1.0.0
 */
@Configuration
public class FileAccessConfig implements WebMvcConfigurer {
    
    /**
     * 文件存储根目录
     */
    @Value("${ebook.storage.base-path}")
    private String basePath;
    
    /**
     * 文件访问 URL 前缀
     */
    @Value("${ebook.file.url-prefix}")
    private String urlPrefix;
    
    /**
     * 配置静态资源处理器
     * 将 URL 路径映射到文件系统路径
     * 
     * 示例：
     * - URL: http://localhost:8080/files/covers/1.jpg
     * - 映射到: ebook-storage/covers/1.jpg
     * 
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置电子书文件访问路径
        // 注意：Windows 系统需要使用 file:/// 三个斜杠
        String resourceLocation = "file:///" + basePath + "/";
        
        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations(resourceLocation);
        
        System.out.println("=================================================");
        System.out.println("文件访问配置已加载:");
        System.out.println("  URL 前缀: " + urlPrefix);
        System.out.println("  文件路径: " + resourceLocation);
        System.out.println("  访问示例: http://localhost:8080" + urlPrefix + "/covers/1.jpg");
        System.out.println("=================================================");
    }
}
