<template>
  <div class="permission-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>权限列表</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>新建权限
          </el-button>
        </div>
      </template>
      
      <el-table :data="permissionList" v-loading="loading" style="width: 100%" row-key="id" default-expand-all>
        <el-table-column prop="permissionName" label="权限名称" width="200" />
        <el-table-column prop="permissionCode" label="权限编码" width="180" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 'menu' ? 'success' : 'warning'" size="small">
              {{ row.type === 'menu' ? '菜单' : '按钮' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link @click="handleAddChild(row)" v-if="row.type === 'menu'">新增子权限</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 权限编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新建权限' : '编辑权限'"
      width="500px"
    >
      <el-form ref="formRef" :model="permissionForm" :rules="formRules" label-width="80px">
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="permissionForm.permissionName" />
        </el-form-item>
        <el-form-item label="权限编码" prop="permissionCode">
          <el-input v-model="permissionForm.permissionCode" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="permissionForm.type">
            <el-radio label="menu">菜单</el-radio>
            <el-radio label="button">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="父级权限" prop="parentId">
          <el-select v-model="permissionForm.parentId" placeholder="请选择父级权限" clearable style="width: 100%;">
            <el-option
              v-for="p in rootPermissions"
              :key="p.id"
              :label="p.permissionName"
              :value="p.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="permissionForm.path" placeholder="如: /system/user" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="permissionForm.orderNum" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="permissionForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPermissionList, createPermission, updatePermission, deletePermission } from '@/api/permission'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)

const permissionList = ref([])
const rootPermissions = ref([])

const permissionForm = reactive({
  id: null,
  permissionName: '',
  permissionCode: '',
  type: 'menu',
  parentId: 0,
  path: '',
  icon: '',
  orderNum: 0,
  status: 1
})

const formRules = {
  permissionName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  permissionCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPermissionList()
    permissionList.value = res.data || []
    // 父级权限可以选择所有菜单类型的权限
    rootPermissions.value = permissionList.value.filter(p => p.type === 'menu')
  } catch (error) {
    console.error('获取权限列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  dialogType.value = 'create'
  permissionForm.id = null
  permissionForm.permissionName = ''
  permissionForm.permissionCode = ''
  permissionForm.type = 'menu'
  permissionForm.parentId = 0
  permissionForm.path = ''
  permissionForm.icon = ''
  permissionForm.orderNum = 0
  permissionForm.status = 1
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  dialogType.value = 'create'
  permissionForm.id = null
  permissionForm.permissionName = ''
  permissionForm.permissionCode = ''
  permissionForm.type = 'button'
  permissionForm.parentId = row.id
  permissionForm.path = ''
  permissionForm.icon = ''
  permissionForm.orderNum = 0
  permissionForm.status = 1
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(permissionForm, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该权限吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deletePermission(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    
    if (dialogType.value === 'create') {
      await createPermission(permissionForm)
      ElMessage.success('创建成功')
    } else {
      await updatePermission(permissionForm.id, permissionForm)
      ElMessage.success('更新成功')
    }
    
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.permission-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
