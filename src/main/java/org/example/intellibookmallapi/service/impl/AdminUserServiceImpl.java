package org.example.intellibookmallapi.service.impl;

import org.example.intellibookmallapi.dto.AdminUserSearchParam;
import org.example.intellibookmallapi.dto.AdminUserVO;
import org.example.intellibookmallapi.exception.BusinessException;
import org.example.intellibookmallapi.mapper.AdminUserMapper;
import org.example.intellibookmallapi.service.AdminUserService;
import org.example.intellibookmallapi.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员用户管理Service实现
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public PageResult<AdminUserVO> getUserList(AdminUserSearchParam param) {
        if (param.getPageNum() == null || param.getPageNum() < 1) param.setPageNum(1);
        if (param.getPageSize() == null || param.getPageSize() < 1) param.setPageSize(10);

        int offset = (param.getPageNum() - 1) * param.getPageSize();
        List<AdminUserVO> list = adminUserMapper.selectUserList(param, offset, param.getPageSize());
        long total = adminUserMapper.countUsers(param);

        return PageResult.of(param.getPageNum(), param.getPageSize(), total, list);
    }

    @Override
    public AdminUserVO getUserById(Long userId) {
        AdminUserVO user = adminUserMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    @Override
    public void lockUser(Long userId) {
        AdminUserVO user = adminUserMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getIsAdmin() != null && user.getIsAdmin() == 1) {
            throw new BusinessException("不能锁定管理员账号");
        }
        int result = adminUserMapper.updateLockedFlag(userId, 1);
        if (result == 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public void unlockUser(Long userId) {
        AdminUserVO user = adminUserMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        int result = adminUserMapper.updateLockedFlag(userId, 0);
        if (result == 0) {
            throw new BusinessException("操作失败");
        }
    }
}
