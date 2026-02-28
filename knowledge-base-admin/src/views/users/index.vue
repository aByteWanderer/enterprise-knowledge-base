<template>
  <div class="user-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item label="用户名">
              <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>
      
      <div class="toolbar">
        <el-button v-if="hasPermission('user:create')" type="primary" @click="handleCreate">
          <el-icon><Plus /></el-icon>新建用户
        </el-button>
      </div>
      
      <el-table :data="userList" v-loading="loading" style="width: 100%">
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="roleNames" label="角色" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="role in (row.roleNames || '').split(',').filter(r => r)"
              :key="role"
              size="small"
              class="role-tag"
            >
              {{ role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button v-if="hasPermission('user:update')" type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="hasPermission('user:assignRole')" type="primary" link @click="handleRoles(row)">分配角色</el-button>
            <el-button v-if="hasPermission('user:resetPassword')" type="warning" link @click="handleResetPassword(row)">重置密码</el-button>
            <el-button v-if="hasPermission('user:delete')" type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 用户编辑/分配角色对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'role' ? '分配角色' : (dialogType === 'create' ? '新建用户' : '编辑用户')"
      width="500px"
    >
      <!-- 分配角色表单 -->
      <el-form v-if="dialogType === 'role'" label-width="80px">
        <el-form-item label="用户名">
          <span>{{ userForm.username }}</span>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="userForm.roleIds" multiple placeholder="请选择角色" style="width: 100%;">
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <!-- 用户编辑表单 -->
      <el-form v-else ref="formRef" :model="userForm" :rules="formRules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="userForm.roleIds" multiple placeholder="请选择角色" style="width: 100%;">
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser, assignRoles, resetPasswordWithResult } from '@/api/user'
import { getRoleList } from '@/api/role'
import { usePermission } from '@/composables/usePermission'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)

const { hasPermission } = usePermission()

const userList = ref([])
const roleList = ref([])

const searchForm = reactive({
  username: '',
  email: ''
})

const userForm = reactive({
  id: null,
  username: '',
  password: '123456',
  email: '',
  phone: '',
  status: 1,
  roleIds: []
})

const formRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUserList(searchForm)
    userList.value = res.data || []
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchRoles = async () => {
  try {
    const res = await getRoleList()
    roleList.value = res.data || []
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

const handleSearch = () => {
  fetchData()
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.email = ''
  fetchData()
}

const handleCreate = () => {
  dialogType.value = 'create'
  userForm.id = null
  userForm.username = ''
  userForm.password = '123456'
  userForm.email = ''
  userForm.phone = ''
  userForm.status = 1
  userForm.roleIds = []
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(userForm, row)
  dialogVisible.value = true
}

const handleRoles = (row) => {
  dialogType.value = 'role'
  userForm.id = row.id
  userForm.username = row.username
  // 解析角色ID数组
  if (row.roleIds) {
    userForm.roleIds = typeof row.roleIds === 'string' 
      ? row.roleIds.split(',').map(id => parseInt(id, 10)) 
      : row.roleIds
  } else {
    userForm.roleIds = []
  }
  dialogVisible.value = true
}

const handleResetPassword = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要重置用户 "${row.username}" 的密码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await resetPasswordWithResult(row.id)
    const newPassword = res.data
    
    ElMessageBox.alert(`密码已重置，新密码为：<strong>${newPassword}</strong><br><br>请告知用户及时修改密码！`, '密码重置成功', {
      confirmButtonText: '我知道了',
      dangerouslyUseHTMLString: true,
      type: 'success'
    })
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重置密码失败:', error)
    }
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleSave = async () => {
  try {
    if (dialogType.value === 'role') {
      await assignRoles(userForm.id, userForm.roleIds)
      ElMessage.success('角色分配成功')
    } else {
      await formRef.value.validate()
      
      if (dialogType.value === 'create') {
        const res = await createUser(userForm)
        const initialPassword = res.data.initialPassword
        ElMessageBox.alert(`用户创建成功！<br><br>初始密码为：<strong>${initialPassword}</strong><br><br>请告知用户及时修改密码！`, '创建用户成功', {
          confirmButtonText: '我知道了',
          dangerouslyUseHTMLString: true,
          type: 'success'
        })
      } else {
        await updateUser(userForm.id, userForm)
        ElMessage.success('更新成功')
      }
    }
    
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

onMounted(() => {
  fetchData()
  fetchRoles()
})
</script>

<style lang="scss" scoped>
.user-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .toolbar {
    margin-bottom: 16px;
  }
  
  .role-tag {
    margin-right: 4px;
    margin-bottom: 4px;
  }
}
</style>
