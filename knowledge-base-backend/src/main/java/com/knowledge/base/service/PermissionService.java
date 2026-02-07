package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService extends IService<Permission> {
    
    /**
     * 查询所有权限（树形结构）
     */
    List<Permission> selectAllPermissions();
    
    /**
     * 查询用户的权限（树形结构）
     */
    List<Permission> selectPermissionsByUserId(Long userId);
    
    /**
     * 查询角色的权限
     */
    List<Permission> selectPermissionsByRoleId(Long roleId);
    
    /**
     * 创建权限
     */
    Long createPermission(Permission permission);
    
    /**
     * 更新权限
     */
    void updatePermission(Permission permission);
    
    /**
     * 删除权限
     */
    void deletePermission(Long id);
    
    /**
     * 构建权限树
     */
    List<Permission> buildPermissionTree(List<Permission> permissions);
    
}
