package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.dto.RoleDTO;
import com.knowledge.base.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 根据角色编码查询
     */
    Role selectByRoleCode(String roleCode);
    
    /**
     * 创建角色
     */
    Long createRole(RoleDTO roleDTO);
    
    /**
     * 更新角色
     */
    void updateRole(RoleDTO roleDTO);
    
    /**
     * 删除角色
     */
    void deleteRole(Long id);
    
    /**
     * 分配权限
     */
    void assignPermissions(Long roleId, Long[] permissionIds);
    
    /**
     * 查询所有角色（带权限）
     */
    List<Role> selectRolesWithPermissions();
    
    /**
     * 查询用户的角色
     */
    List<Role> selectRolesByUserId(Long userId);
    
    /**
     * 查询角色的权限ID
     */
    Set<Long> selectPermissionIdsByRoleId(Long roleId);
    
    /**
     * 更新角色状态
     */
    void updateStatus(Long id, Integer status);
    
}
