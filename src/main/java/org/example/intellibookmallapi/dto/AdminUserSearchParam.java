package org.example.intellibookmallapi.dto;

import lombok.Data;

/**
 * 管理员用户搜索参数
 */
@Data
public class AdminUserSearchParam {

    /** 关键词（用户名或邮箱模糊搜索） */
    private String keyword;

    /** 锁定状态：-1=全部 0=正常 1=锁定 */
    private Integer lockedFlag = -1;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
