package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.Category;

import java.util.List;

/**
 * 分类Mapper接口
 */
@Mapper
public interface CategoryMapper {
    
    /**
     * 根据ID查询分类
     */
    Category selectByPrimaryKey(@Param("categoryId") Long categoryId);
    
    /**
     * 查询所有一级分类
     */
    List<Category> selectFirstLevelCategories();
    
    /**
     * 根据父分类ID查询子分类
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 查询所有分类
     */
    List<Category> selectAll();
    
    /**
     * 查询所有分类（带电子书数量）
     */
    List<Category> selectAllCategories();
    
    /**
     * 查询所有一级分类（带电子书数量）
     */
    List<Category> selectTopLevelCategories();
    
    /**
     * 查询指定父分类下的子分类（带电子书数量）
     */
    List<Category> selectCategoriesByParentId(@Param("parentId") Long parentId);
    
    /**
     * 查询分类树（包含子分类和电子书数量）
     */
    List<Category> selectCategoryTree();
    
    /**
     * 查询热门分类（按电子书数量排序）
     */
    List<Category> selectPopularCategories(@Param("limit") Integer limit);
    
    /**
     * 根据分类名称搜索分类
     */
    List<Category> searchCategoriesByName(@Param("categoryName") String categoryName);
    
    /**
     * 查询分类路径（从根分类到指定分类的完整路径）
     */
    List<Category> selectCategoryPath(@Param("categoryId") Long categoryId);
}
