package org.example.intellibookmallapi.dto;

import lombok.Data;
import java.util.Date;

/**
 * 管理员用户管理VO
 */
@Data
public class AdminUserVO {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    /** 0=普通用户 1=管理员 */
    private Integer isAdmin;
    /** 0=正常 1=锁定 */
    private Integer lockedFlag;
    private Date createTime;
}
