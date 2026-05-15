package org.example.intellibookmallapi.recommend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户相似度结果
 * 用于存储两个用户之间的相似度计算结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimilarity implements Comparable<UserSimilarity> {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * Jaccard相似度值，范围 [0, 1]
     * 0 表示完全不相似，1 表示完全相同
     */
    private Double similarity;
    
    /**
     * 比较方法，用于排序
     * 按相似度降序排列（相似度高的排在前面）
     * 
     * @param other 另一个用户相似度对象
     * @return 比较结果
     */
    @Override
    public int compareTo(UserSimilarity other) {
        // 降序排列：other.similarity - this.similarity
        return Double.compare(other.similarity, this.similarity);
    }
    
    /**
     * 判断相似度是否有效（在 [0, 1] 区间内）
     * @return true 如果相似度值有效
     */
    public boolean isValid() {
        return similarity != null && similarity >= 0.0 && similarity <= 1.0;
    }
}
