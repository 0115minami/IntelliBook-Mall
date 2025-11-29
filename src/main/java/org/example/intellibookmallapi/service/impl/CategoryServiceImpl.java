package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.entity.Category;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.CategoryMapper;
import org.example.intellibookmallapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类业务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectAllCategories();
    }
    
    @Override
    public Category getCategoryById(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new BusinessException("分类ID不能为空");
        }
        
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        return category;
    }
    
    @Override
    public List<Category> getTopLevelCategories() {
        return categoryMapper.selectTopLevelCategories();
    }
    
    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        if (parentId == null || parentId < 0) {
            throw new BusinessException("父分类ID不能为空");
        }
        
        return categoryMapper.selectCategoriesByParentId(parentId);
    }
    
    @Override
    public List<Category> getCategoryTree() {
        return categoryMapper.selectCategoryTree();
    }
    
    @Override
    public List<Category> getPopularCategories(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (limit > 50) {
            limit = 50;
        }
        
        return categoryMapper.selectPopularCategories(limit);
    }
    
    @Override
    public List<Category> searchCategoriesByName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new BusinessException("分类名称不能为空");
        }
        
        return categoryMapper.searchCategoriesByName(categoryName.trim());
    }
    
    @Override
    public List<Category> getCategoryPath(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new BusinessException("分类ID不能为空");
        }
        
        return categoryMapper.selectCategoryPath(categoryId);
    }
}
