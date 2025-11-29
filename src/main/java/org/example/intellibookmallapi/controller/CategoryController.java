package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.entity.Category;
import org.example.intellibookmallapi.service.CategoryService;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 获取所有分类（扁平列表）
     * GET /api/categories
     */
    @GetMapping
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResultGenerator.genSuccessResult(categories);
    }
    
    /**
     * 根据ID查询分类
     * GET /api/categories/{categoryId}
     */
    @GetMapping("/{categoryId}")
    public Result<Category> getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResultGenerator.genSuccessResult(category);
    }
    
    /**
     * 查询所有一级分类
     * GET /api/categories/top
     */
    @GetMapping("/top")
    public Result<List<Category>> getTopLevelCategories() {
        List<Category> categories = categoryService.getTopLevelCategories();
        return ResultGenerator.genSuccessResult(categories);
    }
    
    /**
     * 查询指定父分类下的子分类
     * GET /api/categories/parent/{parentId}
     */
    @GetMapping("/parent/{parentId}")
    public Result<List<Category>> getCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        return ResultGenerator.genSuccessResult(categories);
    }
    
    /**
     * 查询分类树（包含子分类和电子书数量）
     * GET /api/categories/tree
     */
    @GetMapping("/tree")
    public Result<List<Category>> getCategoryTree() {
        List<Category> categoryTree = categoryService.getCategoryTree();
        return ResultGenerator.genSuccessResult(categoryTree);
    }
    
    /**
     * 查询热门分类
     * GET /api/categories/popular
     * 
     * 参数：
     * - limit: 限制数量（可选，默认10）
     */
    @GetMapping("/popular")
    public Result<List<Category>> getPopularCategories(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<Category> categories = categoryService.getPopularCategories(limit);
        return ResultGenerator.genSuccessResult(categories);
    }
    
    /**
     * 根据分类名称搜索分类
     * GET /api/categories/search
     * 
     * 参数：
     * - name: 分类名称
     */
    @GetMapping("/search")
    public Result<List<Category>> searchCategoriesByName(
            @RequestParam String name) {
        List<Category> categories = categoryService.searchCategoriesByName(name);
        return ResultGenerator.genSuccessResult(categories);
    }
    
    /**
     * 查询分类路径（从根分类到指定分类的完整路径）
     * GET /api/categories/{categoryId}/path
     */
    @GetMapping("/{categoryId}/path")
    public Result<List<Category>> getCategoryPath(@PathVariable Long categoryId) {
        List<Category> path = categoryService.getCategoryPath(categoryId);
        return ResultGenerator.genSuccessResult(path);
    }
}
