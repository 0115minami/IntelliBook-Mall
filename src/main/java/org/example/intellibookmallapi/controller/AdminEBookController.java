package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.annotation.RequireAdmin;
import org.example.intellibookmallapi.dto.*;
import org.example.intellibookmallapi.service.AdminEBookService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 管理员图书管理控制器
 */
@RestController
@RequestMapping("/api/admin/ebooks")
public class AdminEBookController {
    
    @Autowired
    private AdminEBookService adminEBookService;
    
    /**
     * 查询图书列表
     * GET /api/admin/ebooks
     */
    @RequireAdmin
    @GetMapping
    public Result<PageResult<AdminEBookVO>> searchEBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "-1") Integer status,
            @RequestParam(required = false, defaultValue = "create_time_desc") String sortBy,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        
        AdminEBookSearchParam searchParam = new AdminEBookSearchParam();
        searchParam.setKeyword(keyword);
        searchParam.setCategoryId(categoryId);
        searchParam.setStatus(status);
        searchParam.setSortBy(sortBy);
        searchParam.setPageNum(pageNum);
        searchParam.setPageSize(pageSize);
        
        PageResult<AdminEBookVO> result = adminEBookService.searchEBooks(searchParam);
        return ResultGenerator.genSuccessResult(result);
    }
    
    /**
     * 获取图书详情
     * GET /api/admin/ebooks/{bookId}
     */
    @RequireAdmin
    @GetMapping("/{bookId}")
    public Result<AdminEBookVO> getEBookById(@PathVariable Long bookId) {
        AdminEBookVO ebook = adminEBookService.getEBookById(bookId);
        return ResultGenerator.genSuccessResult(ebook);
    }
    
    /**
     * 创建图书
     * POST /api/admin/ebooks
     */
    @RequireAdmin
    @PostMapping
    public Result<Map<String, Object>> createEBook(@ModelAttribute AdminEBookCreateParam param) {
        Long bookId = adminEBookService.createEBook(param);
        return ResultGenerator.genSuccessResult(Map.of("bookId", bookId));
    }
    
    /**
     * 更新图书
     * PUT /api/admin/ebooks/{bookId}
     */
    @RequireAdmin
    @PutMapping("/{bookId}")
    public Result<Void> updateEBook(
            @PathVariable Long bookId,
            @ModelAttribute AdminEBookUpdateParam param) {
        adminEBookService.updateEBook(bookId, param);
        return ResultGenerator.genSuccessResult("图书更新成功");
    }
    
    /**
     * 更新图书状态（上下架）
     * PATCH /api/admin/ebooks/{bookId}/status
     */
    @RequireAdmin
    @PatchMapping("/{bookId}/status")
    public Result<Void> updateEBookStatus(
            @PathVariable Long bookId,
            @RequestBody Map<String, Integer> body) {
        Integer status = body.get("status");
        adminEBookService.updateEBookStatus(bookId, status);
        return ResultGenerator.genSuccessResult("状态更新成功");
    }
    
    /**
     * 批量更新图书状态
     * PATCH /api/admin/ebooks/batch/status
     */
    @RequireAdmin
    @PatchMapping("/batch/status")
    public Result<BatchOperationResult> batchUpdateStatus(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> bookIds = (List<Long>) body.get("bookIds");
        Integer status = (Integer) body.get("status");
        
        BatchOperationResult result = adminEBookService.batchUpdateStatus(bookIds, status);
        return ResultGenerator.genSuccessResult(result);
    }
    
    /**
     * 删除图书
     * DELETE /api/admin/ebooks/{bookId}
     */
    @RequireAdmin
    @DeleteMapping("/{bookId}")
    public Result<Void> deleteEBook(@PathVariable Long bookId) {
        adminEBookService.deleteEBook(bookId);
        return ResultGenerator.genSuccessResult("图书删除成功");
    }
    
    /**
     * 批量删除图书
     * DELETE /api/admin/ebooks/batch
     */
    @RequireAdmin
    @DeleteMapping("/batch")
    public Result<BatchOperationResult> batchDeleteEBooks(@RequestBody Map<String, List<Long>> body) {
        List<Long> bookIds = body.get("bookIds");
        BatchOperationResult result = adminEBookService.batchDeleteEBooks(bookIds);
        return ResultGenerator.genSuccessResult(result);
    }
    
    /**
     * 删除电子书文件
     * DELETE /api/admin/ebooks/{bookId}/files/{fileId}
     */
    @RequireAdmin
    @DeleteMapping("/{bookId}/files/{fileId}")
    public Result<Void> deleteEBookFile(
            @PathVariable Long bookId,
            @PathVariable Long fileId) {
        adminEBookService.deleteEBookFile(bookId, fileId);
        return ResultGenerator.genSuccessResult("文件删除成功");
    }
    
    /**
     * 添加电子书文件
     * POST /api/admin/ebooks/{bookId}/files
     */
    @RequireAdmin
    @PostMapping("/{bookId}/files")
    public Result<Map<String, Object>> addEBookFile(
            @PathVariable Long bookId,
            @RequestParam String fileFormat,
            @RequestParam MultipartFile file) {
        Long fileId = adminEBookService.addEBookFile(bookId, fileFormat, file);
        return ResultGenerator.genSuccessResult(Map.of("fileId", fileId));
    }
}
