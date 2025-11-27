package org.example.intellibookmallapi.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件服务控制器
 * 提供电子书封面图片和文件下载服务
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private final String fileStorageLocation = "ebook-storage";

    /**
     * 获取电子书封面图片
     */
    @GetMapping("/covers/{filename:.+}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(fileStorageLocation, "covers", filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = getContentType(filename);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取电子书文件
     */
    @GetMapping("/books/{format}/{filename:.+}")
    public ResponseEntity<Resource> getEBookFile(
            @PathVariable String format,
            @PathVariable String filename) {
        try {
            if (!isValidFormat(format)) {
                return ResponseEntity.badRequest().build();
            }
            
            Path filePath = Paths.get(fileStorageLocation, "books", format.toLowerCase(), filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = getContentTypeForEBook(format);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取电子书文件信息
     */
    @GetMapping("/books/{format}/{filename:.+}/info")
    public ResponseEntity<?> getEBookFileInfo(
            @PathVariable String format,
            @PathVariable String filename) {
        try {
            if (!isValidFormat(format)) {
                return ResponseEntity.badRequest().body("不支持的文件格式");
            }
            
            Path filePath = Paths.get(fileStorageLocation, "books", format.toLowerCase(), filename).normalize();
            
            if (Files.exists(filePath)) {
                long fileSize = Files.size(filePath);
                String contentType = getContentTypeForEBook(format);
                
                return ResponseEntity.ok()
                        .body(new FileInfo(filename, format, fileSize, contentType));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("获取文件信息失败");
        }
    }

    private String getContentType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }

    private String getContentTypeForEBook(String format) {
        switch (format.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "epub":
                return "application/epub+zip";
            case "mobi":
                return "application/x-mobipocket-ebook";
            case "azw3":
                return "application/vnd.amazon.ebook";
            default:
                return "application/octet-stream";
        }
    }

    private boolean isValidFormat(String format) {
        return format != null && 
               (format.equalsIgnoreCase("pdf") || 
                format.equalsIgnoreCase("epub") || 
                format.equalsIgnoreCase("mobi") || 
                format.equalsIgnoreCase("azw3"));
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    public static class FileInfo {
        private String filename;
        private String format;
        private long size;
        private String contentType;

        public FileInfo(String filename, String format, long size, String contentType) {
            this.filename = filename;
            this.format = format;
            this.size = size;
            this.contentType = contentType;
        }

        public String getFilename() { return filename; }
        public String getFormat() { return format; }
        public long getSize() { return size; }
        public String getContentType() { return contentType; }
        
        public void setFilename(String filename) { this.filename = filename; }
        public void setFormat(String format) { this.format = format; }
        public void setSize(long size) { this.size = size; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }
}
