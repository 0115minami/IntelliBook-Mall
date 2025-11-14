package org.example.intellibookmallapi.util;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果封装类
 * @param <T> 数据类型
 */
public class PageResult<T> {
    
    /**
     * 当前页码
     */
    private Integer pageNum;
    
    /**
     * 每页数量
     */
    private Integer pageSize;
    
    /**
     * 总记录数
     */
    private Long totalCount;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 当前页数据
     */
    private List<T> list;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    // ========== 构造方法 ==========
    
    public PageResult() {}
    
    public PageResult(Integer pageNum, Integer pageSize, Long totalCount, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.list = list;
        
        // 计算总页数
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        // 计算是否有上一页/下一页
        this.hasPrevious = pageNum > 1;
        this.hasNext = pageNum < totalPages;
    }
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(Integer pageNum, Integer pageSize, Long totalCount, List<T> list) {
        return new PageResult<>(pageNum, pageSize, totalCount, list);
    }
    
    /**
     * 创建空分页结果
     */
    public static <T> PageResult<T> empty(Integer pageNum, Integer pageSize) {
        return new PageResult<>(pageNum, pageSize, 0L, Collections.emptyList());
    }
    
    // ========== Getter & Setter ==========
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
    public Boolean getHasPrevious() {
        return hasPrevious;
    }
    
    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
    
    public Boolean getHasNext() {
        return hasNext;
    }
    
    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
    
    // ========== 业务方法 ==========
    
    /**
     * 是否为空结果
     */
    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }
    
    /**
     * 获取当前页数据数量
     */
    public int getCurrentPageSize() {
        return list == null ? 0 : list.size();
    }
    
    @Override
    public String toString() {
        return "PageResult{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", totalPages=" + totalPages +
                ", currentPageSize=" + getCurrentPageSize() +
                '}';
    }
}
