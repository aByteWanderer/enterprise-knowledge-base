<template>
  <div class="role-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>角色列表</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>新建角色
          </el-button>
        </div>
      </template>
      
      <el-table :data="roleList" v-loading="loading" style="width: 100%">
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 角色编辑对话框（包含权限分配） -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新建角色' : '编辑角色'"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="roleForm" :rules="formRules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分配权限" prop="permissions">
          <div class="permission-tree-container">
            <el-tree
              ref="permissionTreeRef"
              :data="permissionTreeData"
              :props="{ label: 'permissionName', children: 'children' }"
              show-checkbox
              node-key="id"
              :check-strictly="false"
            >
              <template #default="{ node, data }">
                <span class="permission-node">
                  <span>{{ data.permissionName }}</span>
                  <el-tag v-if="data.type === 'button'" type="warning" size="small" class="permission-type">
                    {{ data.type === 'button' ? '按钮' : '菜单' }}
                  </el-tag>
                </span>
              </template>
            </el-tree>
          </div>
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
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, createRole, updateRole, deleteRole, assignPermissions, getRolePermissions } from '@/api/role'
import { getPermissionList } from '@/api/permission'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)
const permissionTreeRef = ref(null)

const roleList = ref([])
const permissionList = ref([])
const permissionTreeData = ref([])

const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  status: 1
})

const formRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getRoleList()
    roleList.value = res.data || []
  } catch (error) {
    console.error('获取角色列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchPermissions = async () => {
  try {
    const res = await getPermissionList()
    permissionList.value = res.data || []
    permissionTreeData.value = permissionList.value
  } catch (error) {
    console.error('获取权限列表失败:', error)
  }
}

const handleCreate = async () => {
  dialogType.value = 'create'
  roleForm.id = null
  roleForm.roleName = ''
  roleForm.roleCode = ''
  roleForm.description = ''
  roleForm.status = 1
  dialogVisible.value = true
  
  await nextTick()
  if (permissionTreeRef.value) {
    permissionTreeRef.value.setCheckedKeys([])
  }
}

const handleEdit = async (row) => {
  dialogType.value = 'edit'
  roleForm.id = row.id
  roleForm.roleName = row.roleName
  roleForm.roleCode = row.roleCode
  roleForm.description = row.description
  roleForm.status = row.status
  
  // 获取角色已有权限
  let permissionIds = []
  try {
    const roleRes = await getRolePermissions(row.id)
    permissionIds = roleRes.data || []
  } catch (error) {
    console.error('获取角色权限失败:', error)
  }
  
  dialogVisible.value = true
  
  await nextTick()
  if (permissionTreeRef.value) {
    permissionTreeRef.value.setCheckedKeys(permissionIds)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
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
    
    let savedRoleId
    
    if (dialogType.value === 'create') {
      const res = await createRole(roleForm)
      savedRoleId = res.data
      ElMessage.success('创建成功')
    } else {
      await updateRole(roleForm.id, roleForm)
      savedRoleId = roleForm.id
      ElMessage.success('更新成功')
    }
    
    // 保存权限
    if (permissionTreeRef.value) {
      const checkedNodes = permissionTreeRef.value.getCheckedNodes()
      const halfCheckedNodes = permissionTreeRef.value.getHalfCheckedNodes()
      const allCheckedNodes = [...checkedNodes, ...halfCheckedNodes]
      const permissionIds = allCheckedNodes.map(n => Number(n.id))
      
      if (permissionIds.length > 0) {
        await assignPermissions(savedRoleId, permissionIds)
        ElMessage.success('权限分配成功')
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
  fetchPermissions()
})
</script>

<style lang="scss" scoped>
.role-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .permission-tree-container {
    max-height: 400px;
    overflow-y: auto;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 10px;
    
    .permission-node {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .permission-type {
        margin-left: 8px;
      }
    }
  }
}
</style>
