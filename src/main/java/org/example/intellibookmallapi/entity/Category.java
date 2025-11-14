package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.List;

/**
 * 分类实体类
 */
@Data
public class Category {
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称（中文）
     */
    private String categoryName;
    
    /**
     * 分类英文名称
     */
    private String categoryNameEn;
    
    /**
     * 父分类ID（0表示一级分类）
     */
    private Long parentId;
    
    /**
     * 分类级别（1-一级 2-二级）
     */
    private Integer categoryLevel;
    
    /**
     * 排序值
     */
    private Integer sortOrder;
    
    /**
     * 父分类名称（关联查询字段）
     */
    private String parentName;
    
    /**
     * 该分类下的电子书数量（统计字段）
     */
    private Integer bookCount;
    
    /**
     * 子分类列表（用于树形结构）
     */
    private List<Category> children;
}
