import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, logout, getInfo } from '@/api/login'
import { useRouter } from 'vue-router'

export const useUserStore = defineStore('user', () => {
  const router = useRouter()
  
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  const permissions = ref([])
  const roles = ref([])
  
  const isLoggedIn = computed(() => !!token.value)
  
  const userPermissions = computed(() => permissions.value)
  
  async function doLogin(username, password) {
    try {
      const res = await login({ username, password })
      token.value = res.data.token
      localStorage.setItem('token', token.value)
      
      // 获取用户信息
      await fetchUserInfo()
      
      return res
    } catch (error) {
      throw error
    }
  }
  
  async function fetchUserInfo() {
    try {
      const res = await getInfo()
      userInfo.value = res.data
      permissions.value = res.data.permissions || []
      roles.value = res.data.roles || []
    } catch (error) {
      throw error
    }
  }
  
  async function doLogout() {
    try {
      await logout()
    } finally {
      clearUserData()
      router.push('/login')
    }
  }
  
  function clearUserData() {
    token.value = ''
    userInfo.value = null
    permissions.value = []
    roles.value = []
    localStorage.removeItem('token')
  }
  
  function hasPermission(permission) {
    if (roles.value.includes('superadmin')) {
      return true
    }
    return permissions.value.includes(permission)
  }
  
  return {
    token,
    userInfo,
    permissions,
    roles,
    isLoggedIn,
    userPermissions,
    doLogin,
    fetchUserInfo,
    doLogout,
    hasPermission
  }
}, {
  persistedState: {
    key: 'knowledge-base-user',
    paths: ['token']
  }
})
