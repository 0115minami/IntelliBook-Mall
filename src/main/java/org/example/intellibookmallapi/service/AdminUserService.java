package org.example.intellibookmallapi.service;

import org.example.intellibookmallapi.dto.AdminUserSearchParam;
import org.example.intellibookmallapi.dto.AdminUserVO;
import org.example.intellibookmallapi.util.PageResult;

/**
 * 管理员用户管理Service
 */
public interface AdminUserService {

    /**
     * 分页查询用户列表
     */
    PageResult<AdminUserVO> getUserList(AdminUserSearchParam param);

    /**
     * 查询用户详情
     */
    AdminUserVO getUserById(Long userId);

    /**
     * 锁定用户
     */
    void lockUser(Long userId);

    /**
     * 解锁用户
     */
    void unlockUser(Long userId);
}
