package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 角色Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    
    /**
     * 根据角色编码查询
     */
    @Select("SELECT * FROM tb_roles WHERE role_code = #{roleCode} AND deleted = 0")
    Role selectByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * 查询用户的角色
     */
    @Select("SELECT r.* FROM tb_roles r " +
            "INNER JOIN tb_user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0")
    List<Role> selectRolesByUserId(@Param("userId") Long userId);
    
    /**
     * 查询角色的权限ID
     */
    @Select("SELECT permission_id FROM tb_role_permissions WHERE role_id = #{roleId}")
    Set<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 查询所有角色（带权限）
     */
    @Select("SELECT r.*, GROUP_CONCAT(p.permission_code) as permission_codes " +
            "FROM tb_roles r " +
            "LEFT JOIN tb_role_permissions rp ON r.id = rp.role_id " +
            "LEFT JOIN tb_permissions p ON rp.permission_id = p.id " +
            "WHERE r.deleted = 0 " +
            "GROUP BY r.id")
    List<Role> selectRolesWithPermissions();
    
}
