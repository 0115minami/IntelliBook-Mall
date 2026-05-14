package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.*;
import org.example.intellibookmallapi.entity.EBook;
import org.example.intellibookmallapi.entity.EBookFile;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.AdminEBookMapper;
import org.example.intellibookmallapi.mapper.EBookFileMapper;
import org.example.intellibookmallapi.service.AdminEBookService;
import org.example.intellibookmallapi.util.FileUploadUtil;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员电子书服务实现类
 */
@Service
public class AdminEBookServiceImpl implements AdminEBookService {
    
    @Autowired
    private AdminEBookMapper adminEBookMapper;
    
    @Autowired
    private EBookFileMapper eBookFileMapper;
    
    @Override
    public PageResult<AdminEBookVO> searchEBooks(AdminEBookSearchParam searchParam) {
        // 参数校验
        if (searchParam == null) {
            searchParam = new AdminEBookSearchParam();
        }
        
        // 验证并设置默认值
        if (searchParam.getPageNum() == null || searchParam.getPageNum() < 1) {
            searchParam.setPageNum(1);
        }
        if (searchParam.getPageSize() == null || searchParam.getPageSize() < 1) {
            searchParam.setPageSize(20);
        }
        if (searchParam.getPageSize() > 100) {
            searchParam.setPageSize(100); // 限制最大每页数量
        }
        
        // 验证排序参数
        if (!searchParam.isValidSortBy()) {
            searchParam.setSortBy("create_time_desc");
        }
        
        // 查询总数
        Long totalCount = adminEBookMapper.countSearchEBooksForAdmin(searchParam);
        
        // 如果没有结果，直接返回空分页
        if (totalCount == 0) {
            return PageResult.empty(searchParam.getPageNum(), searchParam.getPageSize());
        }
        
        // 查询数据列表
        List<EBook> ebooks = adminEBookMapper.searchEBooksForAdmin(searchParam);
        
        // 转换为VO并查询文件信息
        List<AdminEBookVO> voList = ebooks.stream()
                .map(this::convertToAdminVO)
                .collect(Collectors.toList());
        
        return PageResult.of(searchParam.getPageNum(), searchParam.getPageSize(), totalCount, voList);
    }
    
    @Override
    public AdminEBookVO getEBookById(Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        
        EBook ebook = adminEBookMapper.selectByPrimaryKeyForAdmin(bookId);
        if (ebook == null) {
            throw new BusinessException("书籍不存在");
        }
        
        AdminEBookVO vo = convertToAdminVO(ebook);
        
        // 查询文件列表
        List<EBookFile> files = eBookFileMapper.selectByBookId(bookId);
        vo.setFiles(files);
        
        return vo;
    }
    
    @Override
    @Transactional
    public Long createEBook(AdminEBookCreateParam param) {
        // 参数校验
        validateCreateParam(param);
        
        // 检查ISBN是否已存在
        if (param.getIsbn() != null && !param.getIsbn().trim().isEmpty()) {
            EBook existingBook = adminEBookMapper.selectByIsbnForAdmin(param.getIsbn());
            if (existingBook != null) {
                throw new BusinessException("ISBN已存在：" + param.getIsbn());
            }
        }
        
        // 上传封面图片
        String coverImgPath = null;
        if (param.getCoverImage() != null) {
            coverImgPath = FileUploadUtil.uploadCoverImage(param.getCoverImage());
        }
        
        // 创建EBook实体
        EBook ebook = new EBook();
        BeanUtils.copyProperties(param, ebook);
        ebook.setCoverImg(coverImgPath);
        ebook.setRating(0.0);
        ebook.setRatingCount(0);
        ebook.setViewCount(0);
        ebook.setDownloadCount(0);
        ebook.setStatus(1); // 默认上架
        ebook.setIsDeleted(0);
        
        // 插入电子书记录
        int result = adminEBookMapper.insertEBook(ebook);
        if (result <= 0) {
            throw new BusinessException("创建图书失败");
        }
        
        Long bookId = ebook.getBookId();
        
        // 上传并保存电子书文件
        if (param.getEbookFiles() != null && !param.getEbookFiles().isEmpty()) {
            for (AdminEBookCreateParam.EBookFileParam fileParam : param.getEbookFiles()) {
                uploadAndSaveEBookFile(bookId, fileParam.getFileFormat(), fileParam.getFile());
            }
        }
        
        return bookId;
    }
    
    @Override
    @Transactional
    public void updateEBook(Long bookId, AdminEBookUpdateParam param) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        
        // 检查书籍是否存在
        EBook existingBook = adminEBookMapper.selectByPrimaryKeyForAdmin(bookId);
        if (existingBook == null) {
            throw new BusinessException("书籍不存在");
        }
        
        // 检查ISBN冲突（如果修改了ISBN）
        if (param.getIsbn() != null && !param.getIsbn().equals(existingBook.getIsbn())) {
            EBook conflictBook = adminEBookMapper.selectByIsbnForAdmin(param.getIsbn());
            if (conflictBook != null && !conflictBook.getBookId().equals(bookId)) {
                throw new BusinessException("ISBN已存在：" + param.getIsbn());
            }
        }
        
        // 处理封面图片更新
        if (param.getCoverImage() != null) {
            // 删除旧封面
            if (existingBook.getCoverImg() != null) {
                FileUploadUtil.deleteCoverImage(existingBook.getCoverImg());
            }
            // 上传新封面
            String newCoverPath = FileUploadUtil.uploadCoverImage(param.getCoverImage());
            param.setCoverImg(newCoverPath);
        }
        
        // 更新基本信息
        EBook updateBook = new EBook();
        BeanUtils.copyProperties(param, updateBook);
        updateBook.setBookId(bookId);
        
        int result = adminEBookMapper.updateEBook(updateBook);
        if (result <= 0) {
            throw new BusinessException("更新图书失败");
        }
        
        // 处理文件删除
        if (param.getDeleteFileIds() != null && !param.getDeleteFileIds().isEmpty()) {
            for (Long fileId : param.getDeleteFileIds()) {
                deleteEBookFileInternal(bookId, fileId);
            }
        }
        
        // 处理新文件上传
        if (param.getNewEbookFiles() != null && !param.getNewEbookFiles().isEmpty()) {
            for (AdminEBookUpdateParam.EBookFileParam fileParam : param.getNewEbookFiles()) {
                uploadAndSaveEBookFile(bookId, fileParam.getFileFormat(), fileParam.getFile());
            }
        }
    }
    
    @Override
    public void updateEBookStatus(Long bookId, Integer status) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException("状态值无效");
        }
        
        // 检查书籍是否存在
        EBook existingBook = adminEBookMapper.selectByPrimaryKeyForAdmin(bookId);
        if (existingBook == null) {
            throw new BusinessException("书籍不存在");
        }
        
        int result = adminEBookMapper.updateEBookStatus(bookId, status);
        if (result <= 0) {
            throw new BusinessException("更新状态失败");
        }
    }
    
    @Override
    public BatchOperationResult batchUpdateStatus(List<Long> bookIds, Integer status) {
        if (bookIds == null || bookIds.isEmpty()) {
            throw new BusinessException("书籍ID列表不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException("状态值无效");
        }
        
        int successCount = adminEBookMapper.batchUpdateEBookStatus(bookIds, status);
        int failureCount = bookIds.size() - successCount;
        
        BatchOperationResult result = new BatchOperationResult();
        result.setSuccessCount(successCount);
        result.setFailureCount(failureCount);
        
        return result;
    }
    
    @Override
    @Transactional
    public void deleteEBook(Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        
        // 检查书籍是否存在
        EBook existingBook = adminEBookMapper.selectByPrimaryKeyForAdmin(bookId);
        if (existingBook == null) {
            throw new BusinessException("书籍不存在");
        }
        
        // 逻辑删除
        int result = adminEBookMapper.deleteEBook(bookId);
        if (result <= 0) {
            throw new BusinessException("删除图书失败");
        }
    }
    
    @Override
    public BatchOperationResult batchDeleteEBooks(List<Long> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) {
            throw new BusinessException("书籍ID列表不能为空");
        }
        
        int successCount = adminEBookMapper.batchDeleteEBooks(bookIds);
        int failureCount = bookIds.size() - successCount;
        
        BatchOperationResult result = new BatchOperationResult();
        result.setSuccessCount(successCount);
        result.setFailureCount(failureCount);
        
        return result;
    }
    
    @Override
    @Transactional
    public void deleteEBookFile(Long bookId, Long fileId) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        if (fileId == null || fileId <= 0) {
            throw new BusinessException("文件ID不能为空");
        }
        
        deleteEBookFileInternal(bookId, fileId);
    }
    
    @Override
    @Transactional
    public Long addEBookFile(Long bookId, String fileFormat, MultipartFile file) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException("书籍ID不能为空");
        }
        if (fileFormat == null || fileFormat.trim().isEmpty()) {
            throw new BusinessException("文件格式不能为空");
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        
        // 检查书籍是否存在
        EBook existingBook = adminEBookMapper.selectByPrimaryKeyForAdmin(bookId);
        if (existingBook == null) {
            throw new BusinessException("书籍不存在");
        }
        
        // 检查该格式文件是否已存在
        EBookFile existingFile = adminEBookMapper.selectEBookFileByBookIdAndFormat(bookId, fileFormat);
        if (existingFile != null) {
            throw new BusinessException("该格式的文件已存在");
        }
        
        return uploadAndSaveEBookFile(bookId, fileFormat, file);
    }
    
    // ========== 私有方法 ==========
    
    /**
     * 转换为管理员VO
     */
    private AdminEBookVO convertToAdminVO(EBook ebook) {
        AdminEBookVO vo = new AdminEBookVO();
        BeanUtils.copyProperties(ebook, vo);
        return vo;
    }
    
    /**
     * 验证创建参数
     */
    private void validateCreateParam(AdminEBookCreateParam param) {
        if (param == null) {
            throw new BusinessException("参数不能为空");
        }
        if (param.getBookTitle() == null || param.getBookTitle().trim().isEmpty()) {
            throw new BusinessException("书名不能为空");
        }
        if (param.getAuthor() == null || param.getAuthor().trim().isEmpty()) {
            throw new BusinessException("作者不能为空");
        }
        if (param.getCategoryId() == null || param.getCategoryId() <= 0) {
            throw new BusinessException("分类ID不能为空");
        }
        if (param.getPrice() == null || param.getPrice() < 0) {
            throw new BusinessException("价格不能为空且不能为负数");
        }
        if (param.getCoverImage() == null || param.getCoverImage().isEmpty()) {
            throw new BusinessException("封面图片不能为空");
        }
        if (param.getEbookFiles() == null || param.getEbookFiles().isEmpty()) {
            throw new BusinessException("至少需要上传一个电子书文件");
        }
    }
    
    /**
     * 上传并保存电子书文件
     */
    private Long uploadAndSaveEBookFile(Long bookId, String fileFormat, MultipartFile file) {
        // 上传文件
        FileUploadUtil.EBookFileResult uploadResult = FileUploadUtil.uploadEBookFile(bookId, fileFormat, file);
        
        // 保存文件记录
        EBookFile ebookFile = new EBookFile();
        ebookFile.setBookId(bookId);
        ebookFile.setFileFormat(fileFormat.toUpperCase());
        ebookFile.setFilePath(uploadResult.getFilePath());
        ebookFile.setFileSize(uploadResult.getFileSize());
        ebookFile.setDownloadCount(0);
        
        int result = adminEBookMapper.insertEBookFile(ebookFile);
        if (result <= 0) {
            // 如果数据库插入失败，删除已上传的文件
            FileUploadUtil.deleteEBookFile(uploadResult.getFilePath());
            throw new BusinessException("保存文件记录失败");
        }
        
        return ebookFile.getFileId();
    }
    
    /**
     * 删除电子书文件（内部方法）
     */
    private void deleteEBookFileInternal(Long bookId, Long fileId) {
        // 查询文件信息
        EBookFile ebookFile = adminEBookMapper.selectEBookFileById(fileId);
        if (ebookFile == null) {
            throw new BusinessException("文件不存在");
        }
        if (!ebookFile.getBookId().equals(bookId)) {
            throw new BusinessException("文件不属于该书籍");
        }
        
        // 删除数据库记录
        int result = adminEBookMapper.deleteEBookFile(fileId);
        if (result <= 0) {
            throw new BusinessException("删除文件记录失败");
        }
        
        // 删除物理文件
        FileUploadUtil.deleteEBookFile(ebookFile.getFilePath());
    }
}