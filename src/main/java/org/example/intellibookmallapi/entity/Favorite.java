package org.example.intellibookmallapi.entity;

import lombok.Data;
import java.util.Date;

/**
 * 收藏实体类
 */
@Data
public class Favorite {
    
    /**
     * 收藏ID
     */
    private Long favoriteId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 书籍ID
     */
    private Long bookId;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
