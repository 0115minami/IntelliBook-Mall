package org.example.intellibookmallapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.intellibookmallapi.dto.AdminUserSearchParam;
import org.example.intellibookmallapi.dto.AdminUserVO;

import java.util.List;

/**
 * 管理员用户管理Mapper
 */
@Mapper
public interface AdminUserMapper {

    /**
     * 分页查询用户列表
     */
    List<AdminUserVO> selectUserList(@Param("param") AdminUserSearchParam param,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    /**
     * 统计用户总数
     */
    long countUsers(@Param("param") AdminUserSearchParam param);

    /**
     * 根据ID查询用户详情
     */
    AdminUserVO selectUserById(@Param("userId") Long userId);

    /**
     * 更新用户锁定状态
     */
    int updateLockedFlag(@Param("userId") Long userId, @Param("lockedFlag") Integer lockedFlag);
}
