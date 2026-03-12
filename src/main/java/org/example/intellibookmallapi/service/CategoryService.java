package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.entity.Category;

import java.util.List;

/**
 * 分类业务接口
 */
public interface CategoryService {
    
    /**
     * 根据ID查询分类
     * @param categoryId 分类ID
     * @return 分类信息
     */
    Category getCategoryById(Long categoryId);
    
    /**
     * 查询所有一级分类
     * @return 一级分类列表
     */
    List<Category> getTopLevelCategories();
    
    /**
     * 查询指定父分类下的子分类
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> getCategoriesByParentId(Long parentId);
    
    /**
     * 查询分类树（包含子分类和电子书数量）
     * @return 分类树列表
     */
    List<Category> getCategoryTree();
    
    /**
     * 查询热门分类
     * @param limit 限制数量（默认10）
     * @return 热门分类列表
     */
    List<Category> getPopularCategories(Integer limit);
    
    /**
     * 根据分类名称搜索分类
     * @param categoryName 分类名称
     * @return 分类列表
     */
    List<Category> searchCategoriesByName(String categoryName);
    
    /**
     * 查询分类路径（从根分类到指定分类的完整路径）
     * @param categoryId 分类ID
     * @return 分类路径列表
     */
    List<Category> getCategoryPath(Long categoryId);
}
