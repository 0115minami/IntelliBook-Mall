package org.example.intellibookmallapi.recommend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物品共现关系（Item-CF）
 * 记录两本图书在用户行为中的共现情况
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCooccurrence {
    
    /**
     * 图书1的ID
     */
    private Long bookId1;
    
    /**
     * 图书2的ID
     */
    private Long bookId2;
    
    /**
     * 共现次数（有多少用户同时购买/收藏了这两本书）
     */
    private Integer cooccurrenceCount;
    
    /**
     * 相似度分数（基于共现次数计算）
     */
    private Double similarity;
    
    /**
     * 构造函数（不包含相似度）
     */
    public ItemCooccurrence(Long bookId1, Long bookId2, Integer cooccurrenceCount) {
        this.bookId1 = bookId1;
        this.bookId2 = bookId2;
        this.cooccurrenceCount = cooccurrenceCount;
        this.similarity = 0.0;
    }
    
    /**
     * 判断共现关系是否有效（共现次数 >= 2）
     * @return true 如果共现次数达到阈值
     */
    public boolean isValid() {
        return cooccurrenceCount != null && cooccurrenceCount >= 2;
    }
}
