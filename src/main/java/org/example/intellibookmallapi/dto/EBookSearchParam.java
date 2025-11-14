package org.example.intellibookmallapi.dto;

import lombok.Data;

/**
 * 电子书搜索参数
 */
@Data
public class EBookSearchParam {
    
    /**
     * 关键词（搜索书名、作者、ISBN）
     */
    private String keyword;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 语言
     */
    private String language;
    
    /**
     * 最低价格（分）
     */
    private Integer minPrice;
    
    /**
     * 最高价格（分）
     */
    private Integer maxPrice;
    
    /**
     * 文件格式（如：PDF, EPUB, MOBI, AZW3）
     * 筛选包含指定格式的电子书
     */
    private String fileFormat;
    
    /**
     * 排序字段（rating-评分, price-价格, publish_date-出版日期）
     */
    private String sortBy;
    
    /**
     * 排序方向（asc-升序, desc-降序）
     */
    private String sortOrder;
    
    /**
     * 页码
     */
    private Integer pageNumber;
    
    /**
     * 每页数量
     */
    private Integer pageSize;
    
    /**
     * 获取排序字段（带默认值）
     */
    public String getSortBy() {
        if (sortBy == null || sortBy.isEmpty()) {
            return "book_id";
        }
        return sortBy;
    }
    
    /**
     * 获取排序方向（带默认值）
     */
    public String getSortOrder() {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return "desc";
        }
        return sortOrder;
    }
    
    /**
     * 获取页码（带默认值）
     */
    public Integer getPageNumber() {
        if (pageNumber == null || pageNumber < 1) {
            return 1;
        }
        return pageNumber;
    }
    
    /**
     * 获取每页数量（带默认值）
     */
    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        if (pageSize > 100) {
            return 100; // 最大100条
        }
        return pageSize;
    }
    
    /**
     * 获取偏移量
     */
    public Integer getOffset() {
        return (getPageNumber() - 1) * getPageSize();
    }
    
    /**
     * 获取 MyBatis 分页的 limit
     */
    public Integer getLimit() {
        return getPageSize();
    }
    
    /**
     * 验证排序参数是否有效
     */
    public boolean isValidSortBy() {
        if (sortBy == null) {
            return false;
        }
        return sortBy.matches("^(price_asc|price_desc|rating_desc|publish_date_desc|create_time_desc)$");
    }
    
    /**
     * 获取 SQL ORDER BY 子句
     */
    public String getSqlOrderBy() {
        if (!isValidSortBy()) {
            return "e.create_time DESC";
        }
        
        switch (sortBy) {
            case "price_asc":
                return "e.price ASC";
            case "price_desc":
                return "e.price DESC";
            case "rating_desc":
                return "e.rating DESC, e.rating_count DESC";
            case "publish_date_desc":
                return "e.publish_date DESC";
            case "create_time_desc":
            default:
                return "e.create_time DESC";
        }
    }
    
    /**
     * 获取页码（兼容pageNum命名）
     */
    public Integer getPageNum() {
        return getPageNumber();
    }
    
    /**
     * 设置页码（兼容pageNum命名）
     */
    public void setPageNum(Integer pageNum) {
        this.pageNumber = pageNum;
    }
}
