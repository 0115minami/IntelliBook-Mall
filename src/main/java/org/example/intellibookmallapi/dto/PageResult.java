package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> {
    
    /**
     * 总记录数
     */
    private Integer totalCount;
    
    /**
     * 总页数
     */
    private Integer totalPage;
    
    /**
     * 当前页码
     */
    private Integer pageNumber;
    
    /**
     * 每页数量
     */
    private Integer pageSize;
    
    /**
     * 数据列表
     */
    private List<T> list;
    
    public PageResult() {
    }
    
    public PageResult(List<T> list, Integer totalCount, Integer pageSize, Integer pageNumber) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }
}
