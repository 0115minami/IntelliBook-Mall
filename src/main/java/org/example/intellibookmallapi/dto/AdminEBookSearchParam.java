package org.example.intellibookmallapi.dto;

import lombok.Data;

/**
 * 管理员图书搜索参数
 */
@Data
public class AdminEBookSearchParam {
    
    /**
     * 搜索关键词（书名/作者/ISBN）
     */
    private String keyword;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 上下架状态（1-上架, 0-下架, -1-全部）
     */
    private Integer status;
    
    /**
     * 排序方式（create_time_desc/price_asc/rating_desc）
     */
    private String sortBy = "create_time_desc";
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页数量
     */
    private Integer pageSize = 20;
    
    /**
     * 获取偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
    
    /**
     * 获取限制数量
     */
    public Integer getLimit() {
        return pageSize;
    }
    
    /**
     * 获取SQL排序字段
     */
    public String getSqlOrderBy() {
        switch (sortBy) {
            case "create_time_desc":
                return "e.create_time DESC";
            case "create_time_asc":
                return "e.create_time ASC";
            case "price_desc":
                return "e.price DESC";
            case "price_asc":
                return "e.price ASC";
            case "rating_desc":
                return "e.rating DESC";
            case "rating_asc":
                return "e.rating ASC";
            case "view_count_desc":
                return "e.view_count DESC";
            case "download_count_desc":
                return "e.download_count DESC";
            default:
                return "e.create_time DESC";
        }
    }
    
    /**
     * 验证排序参数是否有效
     */
    public boolean isValidSortBy() {
        return sortBy != null && (
            sortBy.equals("create_time_desc") ||
            sortBy.equals("create_time_asc") ||
            sortBy.equals("price_desc") ||
            sortBy.equals("price_asc") ||
            sortBy.equals("rating_desc") ||
            sortBy.equals("rating_asc") ||
            sortBy.equals("view_count_desc") ||
            sortBy.equals("download_count_desc")
        );
    }
}
