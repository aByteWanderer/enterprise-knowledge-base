import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, logout, getInfo } from '@/api/login'
import { useRouter } from 'vue-router'

const USER_INFO_KEY = 'knowledge-base-user-info'

export const useUserStore = defineStore('user', () => {
  const router = useRouter()
  
  const token = ref(localStorage.getItem('token') || '')
  
  const savedUserInfo = localStorage.getItem(USER_INFO_KEY)
  const userInfo = ref(savedUserInfo ? JSON.parse(savedUserInfo) : null)
  
  const permissions = ref(userInfo.value?.permissions || [])
  const roles = ref(userInfo.value?.roles || [])
  
  const isLoggedIn = computed(() => !!token.value)
  
  const userPermissions = computed(() => permissions.value)
  
  function saveUserInfo(info) {
    userInfo.value = info
    permissions.value = info.permissions || []
    roles.value = info.roles || []
    localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
  }
  
  async function doLogin(username, password) {
    try {
      const res = await login({ username, password })
      token.value = res.data.token
      localStorage.setItem('token', token.value)
      
      if (res.data.user) {
        saveUserInfo(res.data.user)
      }
      
      return res
    } catch (error) {
      throw error
    }
  }
  
  async function fetchUserInfo() {
    if (!token.value) return
    
    try {
      const res = await getInfo()
      if (res.data) {
        saveUserInfo(res.data)
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
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
    localStorage.removeItem(USER_INFO_KEY)
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
