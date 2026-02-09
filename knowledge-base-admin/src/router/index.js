import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    meta: { title: '布局', requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'Odometer' }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/users/index.vue'),
        meta: { title: '用户管理', icon: 'User', permission: 'user:list' }
      },
      {
        path: 'roles',
        name: 'RoleManagement',
        component: () => import('@/views/roles/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', permission: 'role:list' }
      },
      {
        path: 'permissions',
        name: 'PermissionManagement',
        component: () => import('@/views/permissions/index.vue'),
        meta: { title: '权限管理', icon: 'Key', permission: 'permission:list' }
      },
      {
        path: 'categories',
        name: 'CategoryManagement',
        component: () => import('@/views/categories/index.vue'),
        meta: { title: '分类管理', icon: 'Folder', permission: 'category:list' }
      },
      {
        path: 'articles',
        name: 'ArticleManagement',
        component: () => import('@/views/articles/index.vue'),
        meta: { title: '文章管理', icon: 'Document' }
      },
      {
        path: 'articles/:id',
        name: 'ArticleDetail',
        component: () => import('@/views/articles/detail.vue'),
        meta: { title: '文章详情', icon: 'Document' }
      },
      {
        path: 'articles/audit',
        name: 'ArticleAudit',
        component: () => import('@/views/articles/audit.vue'),
        meta: { title: '文章审核', icon: 'Stamp' }
      },
      {
        path: 'tags',
        name: 'TagManagement',
        component: () => import('@/views/tags/index.vue'),
        meta: { title: '标签管理', icon: 'PriceTag', permission: 'tag:list' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User' }
      },
      {
        path: 'password',
        name: 'ChangePassword',
        component: () => import('@/views/password/index.vue'),
        meta: { title: '修改密码', icon: 'Lock' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/404/index.vue'),
    meta: { title: '404' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 企业知识库` : '企业知识库'
  
  const userStore = useUserStore()
  const token = userStore.token
  
  if (to.meta.requiresAuth) {
    if (!token) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
    } else {
      // 如果有token但没有用户信息，重新获取
      if (!userStore.userInfo) {
        try {
          await userStore.fetchUserInfo()
        } catch (error) {
          // 获取失败，清除token并跳转登录
          userStore.clearUserData()
          next({ name: 'Login', query: { redirect: to.fullPath } })
          return
        }
      }
      next()
    }
  } else {
    if (to.name === 'Login' && token) {
      next({ name: 'Dashboard' })
    } else {
      next()
    }
  }
})

export default router
