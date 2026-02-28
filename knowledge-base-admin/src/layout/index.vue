<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo">
        <img src="@/assets/logo.png" alt="logo" />
        <span v-show="!isCollapse">知识库</span>
      </div>
      
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :unique-opened="true"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item
            v-for="route in routes"
            :key="route.path"
            :index="route.path"
          >
            <el-icon><component :is="route.meta.icon" /></el-icon>
            <span>{{ route.meta.title }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        
        <div class="header-right">
          <el-button
            :icon="themeStore.isDark ? Sunny : Moon"
            circle
            @click="themeStore.toggleTheme"
            class="theme-toggle"
          />
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="userInfo?.avatar || ''">
                {{ userInfo?.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ userInfo?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item command="password">
                  <el-icon><Lock /></el-icon>修改密码
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Sunny, Moon } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const themeStore = useThemeStore()

const isCollapse = ref(false)
const userInfo = computed(() => userStore.userInfo)

const menuRoutes = [
  {
    path: '/dashboard',
    name: 'Dashboard',
    meta: { title: '首页', icon: 'Odometer' }
  },
  {
    path: '/users',
    name: 'UserManagement',
    meta: { title: '用户管理', icon: 'User', permission: 'user:list' }
  },
  {
    path: '/roles',
    name: 'RoleManagement',
    meta: { title: '角色管理', icon: 'UserFilled', permission: 'role:list' }
  },
  {
    path: '/permissions',
    name: 'PermissionManagement',
    meta: { title: '权限管理', icon: 'Key', permission: 'permission:list' }
  },
  {
    path: '/categories',
    name: 'CategoryManagement',
    meta: { title: '分类管理', icon: 'Folder', permission: 'category:list' }
  },
  {
    path: '/articles',
    name: 'ArticleManagement',
    meta: { title: '文章管理', icon: 'Document', permission: 'article:list' }
  },
  {
    path: '/articles/audit',
    name: 'ArticleAudit',
    meta: { title: '文章审核', icon: 'Stamp', permission: 'article:audit' }
  },
  {
    path: '/tags',
    name: 'TagManagement',
    meta: { title: '标签管理', icon: 'PriceTag', permission: 'tag:list' }
  },
  {
    path: '/help',
    name: 'Help',
    meta: { title: '使用帮助', icon: 'QuestionFilled' }
  }
]

const routes = computed(() => {
  const userRoles = userStore.roles || []
  const userPermissions = userStore.permissions || []
  
  // 超级管理员可以看到所有菜单
  if (userRoles.includes('superadmin') || userRoles.includes('admin')) {
    return menuRoutes
  }
  
  // 普通用户根据权限过滤菜单
  return menuRoutes.filter(route => {
    // 没有权限要求的菜单（如首页、使用帮助）直接显示
    if (!route.meta.permission) {
      return true
    }
    // 检查用户是否有该权限
    return userPermissions.includes(route.meta.permission)
  })
})

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'password':
      router.push('/password')
      break
    case 'logout':
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userStore.doLogout()
        ElMessage.success('已退出登录')
      })
      break
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    background-color: #263445;
    
    img {
      width: 32px;
      height: 32px;
    }
    
    span {
      color: #fff;
      font-size: 18px;
      font-weight: bold;
    }
  }
  
  .el-menu {
    border-right: none;
  }
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  
  .collapse-btn {
    font-size: 20px;
    cursor: pointer;
    
    &:hover {
      color: #409EFF;
    }
  }
  
  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .theme-toggle {
      font-size: 18px;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      
      .username {
        color: #333;
      }
    }
  }
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
