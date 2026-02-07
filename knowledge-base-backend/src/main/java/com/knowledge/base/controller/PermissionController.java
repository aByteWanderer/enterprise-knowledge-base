package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.entity.Permission;
import com.knowledge.base.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@Tag(name = "权限管理")
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {
    
    private final PermissionService permissionService;
    
    @Operation(summary = "查询所有权限")
    @GetMapping
    @PreAuthorize("hasAuthority('permission:list')")
    public Result<List<Permission>> listPermissions() {
        List<Permission> permissions = permissionService.selectAllPermissions();
        return Result.success(permissions);
    }
    
    @Operation(summary = "查询当前用户的权限")
    @GetMapping("/user")
    public Result<List<Permission>> getCurrentUserPermissions() {
        return Result.success(null);
    }
    
    @Operation(summary = "查询权限详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:list')")
    public Result<Permission> getPermission(@PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        return Result.success(permission);
    }
    
    @Operation(summary = "创建权限")
    @PostMapping
    @PreAuthorize("hasAuthority('permission:create')")
    public Result<Long> createPermission(@Valid @RequestBody Permission permission) {
        Long permissionId = permissionService.createPermission(permission);
        return Result.success(permissionId);
    }
    
    @Operation(summary = "更新权限")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:update')")
    public Result<Void> updatePermission(@PathVariable Long id, @Valid @RequestBody Permission permission) {
        permission.setId(id);
        permissionService.updatePermission(permission);
        return Result.success();
    }
    
    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public Result<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.success();
    }
    
}
