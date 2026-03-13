package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.entity.Category;
import org.example.intellibookmallapi.entity.UserInterest;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.CategoryMapper;
import org.example.intellibookmallapi.mapper.UserInterestMapper;
import org.example.intellibookmallapi.service.UserInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户兴趣Service实现类
 */
@Service
public class UserInterestServiceImpl implements UserInterestService {
    
    @Autowired
    private UserInterestMapper userInterestMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addInterest(Long userId, Long categoryId) {
        // 1. 验证分类是否存在
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 2. 检查是否已添加
        UserInterest existing = userInterestMapper.selectByUserIdAndCategoryId(userId, categoryId);
        if (existing != null) {
            throw new BusinessException("该分类已在兴趣列表中");
        }
        
        // 3. 添加兴趣
        UserInterest userInterest = new UserInterest();
        userInterest.setUserId(userId);
        userInterest.setCategoryId(categoryId);
        
        int result = userInterestMapper.insert(userInterest);
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAddInterests(Long userId, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BusinessException("分类ID列表不能为空");
        }
        
        // 验证所有分类是否存在，并过滤已添加的
        List<UserInterest> toAdd = new ArrayList<>();
        
        for (Long categoryId : categoryIds) {
            // 验证分类是否存在
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null) {
                continue; // 跳过不存在的分类
            }
            
            // 检查是否已添加
            UserInterest existing = userInterestMapper.selectByUserIdAndCategoryId(userId, categoryId);
            if (existing != null) {
                continue; // 跳过已添加的
            }
            
            // 添加到待插入列表
            UserInterest userInterest = new UserInterest();
            userInterest.setUserId(userId);
            userInterest.setCategoryId(categoryId);
            toAdd.add(userInterest);
        }
        
        // 批量插入
        if (toAdd.isEmpty()) {
            return 0;
        }
        
        return userInterestMapper.batchInsert(toAdd);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInterest(Long userId, Long categoryId) {
        int result = userInterestMapper.deleteByUserIdAndCategoryId(userId, categoryId);
        if (result == 0) {
            throw new BusinessException("兴趣不存在");
        }
        return true;
    }
    
    @Override
    public List<UserInterest> getUserInterests(Long userId) {
        return userInterestMapper.selectByUserId(userId);
    }
    
    @Override
    public boolean checkInterest(Long userId, Long categoryId) {
        UserInterest interest = userInterestMapper.selectByUserIdAndCategoryId(userId, categoryId);
        return interest != null;
    }
    
    @Override
    public int getInterestCount(Long userId) {
        return userInterestMapper.countByUserId(userId);
    }
}
