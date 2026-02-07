package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联Mapper
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    
    /**
     * 删除用户的角色关联
     */
    @Delete("DELETE FROM tb_user_roles WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 删除角色的用户关联
     */
    @Delete("DELETE FROM tb_user_roles WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);
    
}
