package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.dto.RoleDTO;
import com.knowledge.base.entity.Role;
import com.knowledge.base.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 角色控制器
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleService roleService;
    
    @Operation(summary = "查询所有角色")
    @GetMapping
    @PreAuthorize("hasAuthority('role:list')")
    public Result<List<Role>> listRoles() {
        List<Role> roles = roleService.selectRolesWithPermissions();
        return Result.success(roles);
    }
    
    @Operation(summary = "查询角色详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:list')")
    public Result<Role> getRole(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }
    
    @Operation(summary = "创建角色")
    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public Result<Long> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        Long roleId = roleService.createRole(roleDTO);
        return Result.success(roleId);
    }
    
    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id);
        roleService.updateRole(roleDTO);
        return Result.success();
    }
    
    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }
    
    @Operation(summary = "分配权限")
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody Long[] permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return Result.success();
    }
    
    @Operation(summary = "查询角色权限ID列表")
    @GetMapping("/{id}/permissions")
    public Result<Set<Long>> getRolePermissions(@PathVariable Long id) {
        Set<Long> permissionIds = roleService.selectPermissionIdsByRoleId(id);
        return Result.success(permissionIds);
    }
    
    @Operation(summary = "更新状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        roleService.updateStatus(id, status);
        return Result.success();
    }
    
}
