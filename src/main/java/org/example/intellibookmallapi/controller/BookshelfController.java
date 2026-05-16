package org.example.intellibookmallapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.intellibookmallapi.config.TokenToUser;
import org.example.intellibookmallapi.dto.BookshelfVO;
import org.example.intellibookmallapi.dto.ReadingPermissionVO;
import org.example.intellibookmallapi.dto.UpdateReadingProgressParam;
import org.example.intellibookmallapi.entity.User;
import org.example.intellibookmallapi.service.BookshelfService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 书架Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/bookshelf")
public class BookshelfController {
    
    @Autowired
    private BookshelfService bookshelfService;
    
    /**
     * 获取用户书架列表（已购买的图书）
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param sortBy 排序方式：recent(最近阅读), purchase(购买时间), title(书名)，默认recent
     * @param user 当前登录用户
     * @return 书架列表
     */
    @GetMapping("/list")
    public Result getBookshelfList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy,
                                   @TokenToUser User user) {
        Long userId = user.getUserId();
        PageResult<BookshelfVO> pageResult = bookshelfService.getBookshelfList(userId, page, pageSize, sortBy);
        
        return ResultGenerator.genSuccessResult(pageResult);
    }
    
    /**
     * 检查用户对某本书的阅读权限
     * @param bookId 书籍ID
     * @param user 当前登录用户
     * @return 阅读权限信息（是否已购买、可用格式、阅读进度等）
     */
    @GetMapping("/check/{bookId}")
    public Result checkReadingPermission(@PathVariable("bookId") Long bookId,
                                        @TokenToUser User user) {
        Long userId = user.getUserId();
        ReadingPermissionVO permission = bookshelfService.checkReadingPermission(userId, bookId);
        
        return ResultGenerator.genSuccessResult(permission);
    }
    
    /**
     * 更新阅读进度
     * @param param 阅读进度参数（bookId, fileFormat, lastPosition）
     * @param user 当前登录用户
     * @return 操作结果
     */
    @PostMapping("/progress")
    public Result updateReadingProgress(@RequestBody UpdateReadingProgressParam param,
                                       @TokenToUser User user) {
        Long userId = user.getUserId();
        
        // 参数校验
        if (param.getBookId() == null) {
            return ResultGenerator.genFailResult("图书ID不能为空");
        }
        if (param.getFileFormat() == null || param.getFileFormat().trim().isEmpty()) {
            return ResultGenerator.genFailResult("文件格式不能为空");
        }
        
        Boolean success = bookshelfService.updateReadingProgress(userId, param);
        
        if (success) {
            return ResultGenerator.genSuccessResult("阅读进度更新成功");
        } else {
            return ResultGenerator.genFailResult("阅读进度更新失败");
        }
    }
    
    /**
     * 在线阅读图书文件
     * @param bookId 书籍ID
     * @param format 文件格式（pdf/epub/mobi）
     * @param range Range请求头（用于分片读取）
     * @param user 当前登录用户
     * @return 文件流
     */
    @GetMapping("/read/{bookId}")
    public ResponseEntity<Resource> readBook(@PathVariable("bookId") Long bookId,
                                            @RequestParam("format") String format,
                                            @RequestHeader(value = "Range", required = false) String range,
                                            @TokenToUser User user) {
        try {
            Long userId = user.getUserId();
            
            // 1. 参数校验
            if (!isValidFormat(format)) {
                log.warn("不支持的文件格式: {}", format);
                return ResponseEntity.badRequest().build();
            }
            
            // 2. 获取文件路径（包含权限验证）
            Path filePath = bookshelfService.getBookFilePath(userId, bookId, format);
            if (filePath == null) {
                log.warn("用户 {} 无权访问图书 {} 的 {} 文件", userId, bookId, format);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // 3. 检查文件是否存在
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                log.error("文件不存在或不可读: {}", filePath);
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            long fileSize = Files.size(filePath);
            
            // 4. 处理Range请求（分片读取）
            if (range != null && range.startsWith("bytes=")) {
                return handleRangeRequest(resource, range, fileSize, format, filePath);
            }
            
            // 5. 返回完整文件（在线预览）
            return ResponseEntity.ok()
                    .contentType(getMediaType(format))
                    .contentLength(fileSize)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                    .body(resource);
                    
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 处理Range请求（分片读取）
     */
    private ResponseEntity<Resource> handleRangeRequest(Resource resource, String range, 
                                                        long fileSize, String format, Path filePath) throws IOException {
        try {
            // 解析Range: bytes=0-1023 或 bytes=0-
            String[] ranges = range.substring(6).split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty() 
                       ? Long.parseLong(ranges[1]) 
                       : fileSize - 1;
            
            // 验证范围
            if (start >= fileSize || end >= fileSize || start > end) {
                log.warn("无效的Range请求: {}, 文件大小: {}", range, fileSize);
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                        .build();
            }
            
            long contentLength = end - start + 1;
            
            // 创建分片资源
            FileInputStream fis = new FileInputStream(filePath.toFile());
            fis.skip(start);
            InputStreamResource partialResource = new InputStreamResource(fis);
            
            log.info("Range请求: bytes {}-{}/{}, contentLength: {}", start, end, fileSize, contentLength);
            
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(getMediaType(format))
                    .contentLength(contentLength)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                    .body(partialResource);
                    
        } catch (NumberFormatException e) {
            log.error("解析Range请求失败: {}", range, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取文件的MediaType
     */
    private MediaType getMediaType(String format) {
        switch (format.toLowerCase()) {
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "epub":
                return MediaType.parseMediaType("application/epub+zip");
            case "mobi":
                return MediaType.parseMediaType("application/x-mobipocket-ebook");
            case "azw3":
                return MediaType.parseMediaType("application/vnd.amazon.ebook");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
    
    /**
     * 验证文件格式是否支持
     */
    private boolean isValidFormat(String format) {
        if (format == null) {
            return false;
        }
        String lowerFormat = format.toLowerCase();
        return lowerFormat.equals("pdf") || 
               lowerFormat.equals("epub") || 
               lowerFormat.equals("mobi") || 
               lowerFormat.equals("azw3");
    }
}
