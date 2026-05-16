package org.example.intellibookmallapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.intellibookmallapi.dto.BookshelfVO;
import org.example.intellibookmallapi.dto.ReadingPermissionVO;
import org.example.intellibookmallapi.dto.UpdateReadingProgressParam;
import org.example.intellibookmallapi.entity.EBookFile;
import org.example.intellibookmallapi.entity.ReadingProgress;
import org.example.intellibookmallapi.mapper.BookshelfMapper;
import org.example.intellibookmallapi.mapper.EBookFileMapper;
import org.example.intellibookmallapi.service.BookshelfService;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 书架Service实现类
 */
@Slf4j
@Service
public class BookshelfServiceImpl implements BookshelfService {
    
    @Autowired
    private BookshelfMapper bookshelfMapper;
    
    @Autowired
    private EBookFileMapper eBookFileMapper;
    
    @Value("${file.storage.location:ebook-storage}")
    private String fileStorageLocation;
    
    @Override
    public PageResult<BookshelfVO> getBookshelfList(Long userId, Integer page, Integer pageSize, String sortBy) {
        log.info("获取用户书架列表，userId: {}, page: {}, pageSize: {}, sortBy: {}", userId, page, pageSize, sortBy);
        
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "recent";
        }
        
        // 计算偏移量
        Integer offset = (page - 1) * pageSize;
        
        // 查询书架列表
        List<BookshelfVO> list = bookshelfMapper.selectPurchasedBooks(userId, offset, pageSize, sortBy);
        
        // 统计总数
        Integer total = bookshelfMapper.countPurchasedBooks(userId);
        
        log.info("用户 {} 的书架共有 {} 本书，当前页返回 {} 本", userId, total, list.size());
        
        // 构建分页结果
        PageResult<BookshelfVO> pageResult = PageResult.of(page, pageSize, total.longValue(), list);
        
        return pageResult;
    }
    
    @Override
    public ReadingPermissionVO checkReadingPermission(Long userId, Long bookId) {
        log.info("检查阅读权限，userId: {}, bookId: {}", userId, bookId);
        
        ReadingPermissionVO permission = new ReadingPermissionVO();
        
        // 检查是否已购买
        Boolean hasPurchased = bookshelfMapper.checkPurchased(userId, bookId);
        permission.setHasPurchased(hasPurchased);
        permission.setCanRead(hasPurchased);
        
        if (!hasPurchased) {
            log.warn("用户 {} 未购买图书 {}", userId, bookId);
            return permission;
        }
        
        // 查询可用文件格式
        List<String> formats = bookshelfMapper.selectAvailableFormats(bookId);
        permission.setAvailableFormats(formats);
        
        // 查询阅读进度
        List<ReadingProgress> progressList = bookshelfMapper.selectReadingProgress(userId, bookId);
        if (progressList != null && !progressList.isEmpty()) {
            // 取最近的阅读进度
            ReadingProgress latestProgress = progressList.get(0);
            permission.setLastReadTime(latestProgress.getLastReadTime());
            permission.setLastReadFormat(latestProgress.getFileFormat());
            permission.setLastPosition(latestProgress.getLastPosition());
        }
        
        log.info("用户 {} 对图书 {} 有阅读权限，可用格式: {}", userId, bookId, formats);
        
        return permission;
    }
    
    @Override
    public Boolean updateReadingProgress(Long userId, UpdateReadingProgressParam param) {
        log.info("更新阅读进度，userId: {}, bookId: {}, format: {}", 
                userId, param.getBookId(), param.getFileFormat());
        
        // 参数校验
        if (param.getBookId() == null) {
            log.error("图书ID不能为空");
            return false;
        }
        if (param.getFileFormat() == null || param.getFileFormat().trim().isEmpty()) {
            log.error("文件格式不能为空");
            return false;
        }
        
        // 检查是否已购买
        Boolean hasPurchased = bookshelfMapper.checkPurchased(userId, param.getBookId());
        if (!hasPurchased) {
            log.error("用户 {} 未购买图书 {}，无法更新阅读进度", userId, param.getBookId());
            return false;
        }
        
        // 构建阅读进度对象
        ReadingProgress progress = new ReadingProgress();
        progress.setUserId(userId);
        progress.setBookId(param.getBookId());
        progress.setFileFormat(param.getFileFormat());
        progress.setLastPosition(param.getLastPosition());
        
        // 插入或更新阅读进度
        Integer rows = bookshelfMapper.upsertReadingProgress(progress);
        
        if (rows > 0) {
            log.info("阅读进度更新成功，userId: {}, bookId: {}", userId, param.getBookId());
            return true;
        } else {
            log.error("阅读进度更新失败，userId: {}, bookId: {}", userId, param.getBookId());
            return false;
        }
    }
    
    @Override
    public Path getBookFilePath(Long userId, Long bookId, String format) {
        log.info("获取图书文件路径，userId: {}, bookId: {}, format: {}", userId, bookId, format);
        
        // 1. 验证参数
        if (userId == null || bookId == null || format == null || format.trim().isEmpty()) {
            log.error("参数不能为空");
            return null;
        }
        
        // 2. 检查用户是否已购买该图书
        Boolean hasPurchased = bookshelfMapper.checkPurchased(userId, bookId);
        if (hasPurchased == null || !hasPurchased) {
            log.warn("用户 {} 未购买图书 {}，无法获取文件", userId, bookId);
            return null;
        }
        
        // 3. 查询文件信息
        EBookFile eBookFile = eBookFileMapper.selectByBookIdAndFormat(bookId, format.toLowerCase());
        if (eBookFile == null) {
            log.warn("图书 {} 不存在 {} 格式的文件", bookId, format);
            return null;
        }
        
        // 4. 构建文件路径
        // file_path 数据库中已存储完整相对路径（如 books/pdf/4.pdf），直接拼接存储根目录即可
        Path filePath = Paths.get(fileStorageLocation, eBookFile.getFilePath()).normalize();
        
        log.info("文件路径: {}", filePath.toString());
        
        return filePath;
    }
}
