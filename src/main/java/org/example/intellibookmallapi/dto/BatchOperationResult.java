package org.example.intellibookmallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量操作结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchOperationResult {
    
    /**
     * 成功数量
     */
    private Integer successCount;
    
    /**
     * 失败数量
     */
    private Integer failureCount;
}
