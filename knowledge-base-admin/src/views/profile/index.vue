<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人中心</span>
        </div>
      </template>
      
      <div class="profile-info">
        <div class="avatar-section">
          <el-avatar :size="100" :src="userInfo?.avatar || ''">
            {{ userInfo?.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <h2 class="username">{{ userInfo?.username }}</h2>
          <el-tag v-for="role in userInfo?.roles" :key="role" type="primary" class="role-tag">
            {{ role }}
          </el-tag>
        </div>
        
        <el-divider />
        
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ userInfo?.id }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ userInfo?.username }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ userInfo?.email || '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ userInfo?.phone || '未设置' }}</el-descriptions-item>
        </el-descriptions>
        
        <el-divider />
        
        <div class="permissions-section">
          <h3>权限列表</h3>
          <div class="permissions-list">
            <el-tag v-for="perm in userInfo?.permissions" :key="perm" type="info" class="perm-tag">
              {{ perm }}
            </el-tag>
            <span v-if="!userInfo?.permissions?.length" class="no-data">暂无权限数据</span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
}

.profile-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.profile-info {
  .avatar-section {
    text-align: center;
    padding: 20px 0;
    
    .username {
      margin: 15px 0 10px;
      font-size: 24px;
      color: #303133;
    }
    
    .role-tag {
      margin: 0 5px;
    }
  }
  
  .permissions-section {
    h3 {
      margin-bottom: 15px;
      color: #303133;
    }
    
    .permissions-list {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      
      .perm-tag {
        margin: 0;
      }
      
      .no-data {
        color: #909399;
      }
    }
  }
}
</style>
