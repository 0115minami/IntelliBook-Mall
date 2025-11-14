package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.dto.EBookSearchParam;
import org.example.intellibookmallapi.dto.EBookVO;
import org.example.intellibookmallapi.service.EBookService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电子书控制器
 */
@RestController
@RequestMapping("/api/ebooks")
public class EBookController {
    
    @Autowired
    private EBookService ebookService;
    
    /**
     * 根据ID查询电子书详情
     * GET /api/ebooks/{bookId}
     */
    @GetMapping("/{bookId}")
    public Result<EBookVO> getEBookById(@PathVariable Long bookId) {
        EBookVO ebook = ebookService.getEBookById(bookId);
        return ResultGenerator.genSuccessResult(ebook);
    }
    
    /**
     * 搜索电子书（分页）
     * GET /api/ebooks/search
     * 
     * 参数：
     * - keyword: 搜索关键词（可选）
     * - categoryId: 分类ID（可选）
     * - language: 语言（可选）
     * - minPrice: 最低价格/分（可选）
     * - maxPrice: 最高价格/分（可选）
     * - fileFormat: 文件格式（可选，如：PDF, EPUB, MOBI, AZW3）
     * - sortBy: 排序方式（可选，默认create_time_desc）
     * - pageNum: 页码（可选，默认1）
     * - pageSize: 每页数量（可选，默认20）
     */
    @GetMapping("/search")
    public Result<PageResult<EBookVO>> searchEBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String fileFormat,
            @RequestParam(required = false, defaultValue = "create_time_desc") String sortBy,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        
        EBookSearchParam searchParam = new EBookSearchParam();
        searchParam.setKeyword(keyword);
        searchParam.setCategoryId(categoryId);
        searchParam.setLanguage(language);
        searchParam.setMinPrice(minPrice);
        searchParam.setMaxPrice(maxPrice);
        searchParam.setFileFormat(fileFormat);
        searchParam.setSortBy(sortBy);
        searchParam.setPageNum(pageNum);
        searchParam.setPageSize(pageSize);
        
        PageResult<EBookVO> result = ebookService.searchEBooks(searchParam);
        return ResultGenerator.genSuccessResult(result);
    }
    
    /**
     * 获取热门电子书
     * GET /api/ebooks/popular
     * 
     * 参数：
     * - limit: 限制数量（可选，默认10）
     */
    @GetMapping("/popular")
    public Result<List<EBookVO>> getPopularEBooks(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<EBookVO> ebooks = ebookService.getPopularEBooks(limit);
        return ResultGenerator.genSuccessResult(ebooks);
    }
    
    /**
     * 获取最新电子书
     * GET /api/ebooks/latest
     * 
     * 参数：
     * - limit: 限制数量（可选，默认10）
     */
    @GetMapping("/latest")
    public Result<List<EBookVO>> getLatestEBooks(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<EBookVO> ebooks = ebookService.getLatestEBooks(limit);
        return ResultGenerator.genSuccessResult(ebooks);
    }
    
    /**
     * 根据分类查询电子书（分页）
     * GET /api/ebooks/category/{categoryId}
     * 
     * 参数：
     * - pageNum: 页码（可选，默认1）
     * - pageSize: 每页数量（可选，默认20）
     */
    @GetMapping("/category/{categoryId}")
    public Result<PageResult<EBookVO>> getEBooksByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        
        PageResult<EBookVO> result = ebookService.getEBooksByCategory(categoryId, pageNum, pageSize);
        return ResultGenerator.genSuccessResult(result);
    }
    
    /**
     * 获取相关推荐电子书
     * GET /api/ebooks/{bookId}/related
     * 
     * 参数：
     * - limit: 限制数量（可选，默认5）
     */
    @GetMapping("/{bookId}/related")
    public Result<List<EBookVO>> getRelatedEBooks(
            @PathVariable Long bookId,
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        List<EBookVO> ebooks = ebookService.getRelatedEBooks(bookId, limit);
        return ResultGenerator.genSuccessResult(ebooks);
    }
}
