package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM tb_users WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM tb_users WHERE email = #{email} AND deleted = 0")
    User selectByEmail(@Param("email") String email);
    
    /**
     * 查询用户角色编码
     */
    @Select("SELECT r.role_code FROM tb_roles r " +
            "INNER JOIN tb_user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0")
    Set<String> selectRoleCodesByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户权限编码
     */
    @Select("SELECT DISTINCT p.permission_code FROM tb_permissions p " +
            "INNER JOIN tb_role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN tb_user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.deleted = 0")
    Set<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
    
    /**
     * 查询所有用户（带角色）
     */
    @Select("SELECT u.*, GROUP_CONCAT(DISTINCT r.role_name SEPARATOR ',') as roleNames, " +
            "GROUP_CONCAT(DISTINCT r.id SEPARATOR ',') as roleIds " +
            "FROM tb_users u " +
            "LEFT JOIN tb_user_roles ur ON u.id = ur.user_id " +
            "LEFT JOIN tb_roles r ON ur.role_id = r.id " +
            "WHERE u.deleted = 0 " +
            "GROUP BY u.id")
    List<User> selectUsersWithRoles();
    
}
