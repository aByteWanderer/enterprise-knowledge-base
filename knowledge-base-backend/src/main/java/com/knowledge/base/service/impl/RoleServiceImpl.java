package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.dto.RoleDTO;
import com.knowledge.base.entity.Role;
import com.knowledge.base.entity.RolePermission;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.RoleMapper;
import com.knowledge.base.mapper.RolePermissionMapper;
import com.knowledge.base.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    
    @Override
    public Role selectByRoleCode(String roleCode) {
        return roleMapper.selectByRoleCode(roleCode);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleDTO roleDTO) {
        // 检查角色编码是否存在
        Role existRole = selectByRoleCode(roleDTO.getRoleCode());
        if (existRole != null) {
            throw new BusinessException(ResultCode.ROLE_EXISTS, "角色编码已存在");
        }
        
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setStatus(roleDTO.getStatus() != null ? roleDTO.getStatus() : 1);
        
        roleMapper.insert(role);
        
        // 分配权限
        if (roleDTO.getPermissionIds() != null && roleDTO.getPermissionIds().length > 0) {
            assignPermissions(role.getId(), roleDTO.getPermissionIds());
        }
        
        return role.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDTO roleDTO) {
        Role role = roleMapper.selectById(roleDTO.getId());
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        // 检查角色编码是否被其他角色使用
        Role existRole = selectByRoleCode(roleDTO.getRoleCode());
        if (existRole != null && !existRole.getId().equals(roleDTO.getId())) {
            throw new BusinessException(ResultCode.ROLE_EXISTS, "角色编码已存在");
        }
        
        BeanUtils.copyProperties(roleDTO, role);
        roleMapper.updateById(role);
        
        // 更新权限
        if (roleDTO.getPermissionIds() != null) {
            assignPermissions(role.getId(), roleDTO.getPermissionIds());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        role.setDeleted(1);
        roleMapper.updateById(role);
        
        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleId(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, Long[] permissionIds) {
        // 删除原有权限
        rolePermissionMapper.deleteByRoleId(roleId);
        
        // 分配新权限
        if (permissionIds != null && permissionIds.length > 0) {
            LocalDateTime now = LocalDateTime.now();
            for (Long permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermission.setCreatedAt(now);
                rolePermissionMapper.insert(rolePermission);
            }
        }
    }
    
    @Override
    public List<Role> selectRolesWithPermissions() {
        return roleMapper.selectRolesWithPermissions();
    }
    
    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }
    
    @Override
    public Set<Long> selectPermissionIdsByRoleId(Long roleId) {
        return roleMapper.selectPermissionIdsByRoleId(roleId);
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        role.setStatus(status);
        role.setUpdatedAt(LocalDateTime.now());
        roleMapper.updateById(role);
    }
    
}
