package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
    /**
     * 查询用户的权限（树形结构）
     */
    @Select("SELECT DISTINCT p.* FROM tb_permissions p " +
            "INNER JOIN tb_role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN tb_user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.deleted = 0 " +
            "ORDER BY p.order_num")
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询权限
     */
    @Select("SELECT p.* FROM tb_permissions p " +
            "INNER JOIN tb_role_permissions rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.deleted = 0 " +
            "ORDER BY p.order_num")
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询所有权限（树形结构）
     */
    @Select("SELECT * FROM tb_permissions WHERE deleted = 0 ORDER BY order_num")
    List<Permission> selectAllPermissions();
    
    /**
     * 查询用户的权限编码
     */
    @Select("SELECT DISTINCT p.permission_code FROM tb_permissions p " +
            "INNER JOIN tb_role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN tb_user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.deleted = 0")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
    
}
