package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.entity.Permission;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.PermissionMapper;
import com.knowledge.base.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    
    private final PermissionMapper permissionMapper;
    
    @Override
    public List<Permission> selectAllPermissions() {
        List<Permission> permissions = permissionMapper.selectAllPermissions();
        return buildPermissionTree(permissions);
    }
    
    @Override
    public List<Permission> selectPermissionsByUserId(Long userId) {
        List<Permission> permissions = permissionMapper.selectPermissionsByUserId(userId);
        return buildPermissionTree(permissions);
    }
    
    @Override
    public List<Permission> selectPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }
    
    @Override
    public Long createPermission(Permission permission) {
        // 检查权限编码是否已存在
        Permission exist = permissionMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Permission>()
                .eq("permission_code", permission.getPermissionCode())
                .eq("deleted", 0)
        ).stream().findFirst().orElse(null);
        
        if (exist != null) {
            throw new BusinessException(ResultCode.PERMISSION_EXISTS, "权限编码已存在: " + permission.getPermissionCode());
        }
        
        permissionMapper.insert(permission);
        return permission.getId();
    }
    
    @Override
    public void updatePermission(Permission permission) {
        Permission exist = permissionMapper.selectById(permission.getId());
        if (exist == null) {
            throw new BusinessException(ResultCode.PERMISSION_NOT_FOUND);
        }
        permissionMapper.updateById(permission);
    }
    
    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(ResultCode.PERMISSION_NOT_FOUND);
        }
        permission.setDeleted(1);
        permissionMapper.updateById(permission);
    }
    
    @Override
    public List<Permission> buildPermissionTree(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 构建树形结构
        Map<Long, List<Permission>> parentMap = new HashMap<>();
        List<Permission> roots = new ArrayList<>();
        
        for (Permission permission : permissions) {
            Long parentId = permission.getParentId();
            if (parentId == null || parentId == 0L || parentId.equals(0)) {
                roots.add(permission);
            } else {
                parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(permission);
            }
        }
        
        // 递归构建子树
        buildChildPermissions(roots, parentMap);
        
        return roots;
    }
    
    private void buildChildPermissions(List<Permission> permissions, Map<Long, List<Permission>> parentMap) {
        for (Permission permission : permissions) {
            List<Permission> children = parentMap.get(permission.getId());
            if (children != null && !children.isEmpty()) {
                permission.setChildren(children);
                buildChildPermissions(children, parentMap);
            }
        }
    }
    
}
