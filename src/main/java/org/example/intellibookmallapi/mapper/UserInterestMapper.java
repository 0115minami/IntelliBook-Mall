package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.entity.UserInterest;

import java.util.List;

/**
 * 用户兴趣Mapper接口
 */
@Mapper
public interface UserInterestMapper {
    
    /**
     * 添加用户兴趣
     */
    int insert(UserInterest userInterest);
    
    /**
     * 批量添加用户兴趣
     */
    int batchInsert(@Param("list") List<UserInterest> userInterests);
    
    /**
     * 删除用户兴趣
     */
    int deleteByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
    
    /**
     * 查询用户的所有兴趣（带分类名称）
     */
    List<UserInterest> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 检查用户是否已添加某个分类为兴趣
     */
    UserInterest selectByUserIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
    
    /**
     * 查询用户兴趣数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 查询某个分类被多少用户添加为兴趣
     */
    int countByCategoryId(@Param("categoryId") Long categoryId);
}
