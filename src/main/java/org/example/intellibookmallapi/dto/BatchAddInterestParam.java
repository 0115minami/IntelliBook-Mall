package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.List;

/**
 * 批量添加兴趣参数
 */
@Data
public class BatchAddInterestParam {
    
    /**
     * 分类ID列表
     */
    private List<Long> categoryIds;
}
