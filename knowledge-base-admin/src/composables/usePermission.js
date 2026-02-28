import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

export function usePermission() {
  const userStore = useUserStore()
  
  const roles = computed(() => userStore.roles || [])
  const permissions = computed(() => userStore.permissions || [])
  
  /**
   * 检查是否有指定权限
   */
  const hasPermission = (permission) => {
    // 超级管理员或admin角色拥有所有权限
    if (roles.value.includes('superadmin') || roles.value.includes('admin')) {
      return true
    }
    return permissions.value.includes(permission)
  }
  
  /**
   * 检查是否有任意一个权限
   */
  const hasAnyPermission = (permissionList) => {
    if (roles.value.includes('superadmin') || roles.value.includes('admin')) {
      return true
    }
    return permissionList.some(p => permissions.value.includes(p))
  }
  
  /**
   * 检查是否有所有权限
   */
  const hasAllPermissions = (permissionList) => {
    if (roles.value.includes('superadmin') || roles.value.includes('admin')) {
      return true
    }
    return permissionList.every(p => permissions.value.includes(p))
  }
  
  /**
   * 检查是否有指定角色
   */
  const hasRole = (role) => {
    return roles.value.includes(role)
  }
  
  return {
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    hasRole,
    roles,
    permissions
  }
}
