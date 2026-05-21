package org.example.intellibookmallapi.controller;

import org.example.intellibookmallapi.annotation.RequireAdmin;
import org.example.intellibookmallapi.dto.AdminUserSearchParam;
import org.example.intellibookmallapi.dto.AdminUserVO;
import org.example.intellibookmallapi.service.AdminUserService;
import org.example.intellibookmallapi.util.PageResult;
import org.example.intellibookmallapi.util.Result;
import org.example.intellibookmallapi.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员用户管理Controller
 * 路径前缀 /api/admin/users，由 AdminInterceptor 统一鉴权
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 分页查询用户列表
     * GET /api/admin/users?keyword=&lockedFlag=-1&pageNum=1&pageSize=10
     */
    @RequireAdmin
    @GetMapping
    public Result<PageResult<AdminUserVO>> getUserList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "-1") Integer lockedFlag,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        AdminUserSearchParam param = new AdminUserSearchParam();
        param.setKeyword(keyword);
        param.setLockedFlag(lockedFlag);
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);

        return ResultGenerator.genSuccessResult(adminUserService.getUserList(param));
    }

    /**
     * 查询用户详情
     * GET /api/admin/users/{userId}
     */
    @RequireAdmin
    @GetMapping("/{userId}")
    public Result<AdminUserVO> getUserById(@PathVariable Long userId) {
        return ResultGenerator.genSuccessResult(adminUserService.getUserById(userId));
    }

    /**
     * 锁定用户
     * PUT /api/admin/users/{userId}/lock
     */
    @RequireAdmin
    @PutMapping("/{userId}/lock")
    public Result<String> lockUser(@PathVariable Long userId) {
        adminUserService.lockUser(userId);
        return ResultGenerator.genSuccessResult("锁定成功");
    }

    /**
     * 解锁用户
     * PUT /api/admin/users/{userId}/unlock
     */
    @RequireAdmin
    @PutMapping("/{userId}/unlock")
    public Result<String> unlockUser(@PathVariable Long userId) {
        adminUserService.unlockUser(userId);
        return ResultGenerator.genSuccessResult("解锁成功");
    }
}
