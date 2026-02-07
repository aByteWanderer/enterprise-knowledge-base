<template>
  <div class="category-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>分类列表</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>新建分类
          </el-button>
        </div>
      </template>
      
      <el-table
        :data="categoryTree"
        v-loading="loading"
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children' }"
        style="width: 100%"
      >
        <el-table-column prop="categoryName" label="分类名称" min-width="200" />
        <el-table-column prop="categoryCode" label="分类编码" width="150" />
        <el-table-column prop="level" label="层级" width="100" align="center">
          <template #default="{ row }">
            <el-tag>{{ row.level }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleCreateChild(row)">添加子分类</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 分类编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新建分类' : '编辑分类'"
      width="500px"
    >
      <el-form ref="formRef" :model="categoryForm" :rules="formRules" label-width="80px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="categoryForm.categoryName" />
        </el-form-item>
        <el-form-item label="分类编码" prop="categoryCode">
          <el-input v-model="categoryForm.categoryCode" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="父级分类" prop="parentId">
          <el-select v-model="categoryForm.parentId" placeholder="请选择父级分类" clearable style="width: 100%;">
            <el-option
              v-for="cat in rootCategories"
              :key="cat.id"
              :label="cat.categoryName"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="categoryForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="categoryForm.orderNum" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="categoryForm.status">
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
import { getCategoryTree, getCategoryList, createCategory, updateCategory, deleteCategory } from '@/api/category'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)
const parentId = ref(0)

const categoryTree = ref([])
const allCategories = ref([])
const rootCategories = ref([])

const categoryForm = reactive({
  id: null,
  categoryName: '',
  categoryCode: '',
  parentId: 0,
  description: '',
  orderNum: 0,
  status: 1
})

const formRules = {
  categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  categoryCode: [{ required: true, message: '请输入分类编码', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCategoryTree()
    categoryTree.value = res.data || []
    
    const listRes = await getCategoryList()
    allCategories.value = listRes.data || []
    rootCategories.value = allCategories.value.filter(c => c.parentId === 0)
  } catch (error) {
    console.error('获取分类列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  dialogType.value = 'create'
  parentId.value = 0
  resetForm()
  dialogVisible.value = true
}

const handleCreateChild = (row) => {
  dialogType.value = 'create'
  parentId.value = row.id
  resetForm()
  categoryForm.parentId = row.id
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(categoryForm, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该分类下有子分类，无法删除')
    return
  }
  
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const resetForm = () => {
  categoryForm.id = null
  categoryForm.categoryName = ''
  categoryForm.categoryCode = ''
  categoryForm.description = ''
  categoryForm.orderNum = 0
  categoryForm.status = 1
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    
    if (dialogType.value === 'create') {
      await createCategory(categoryForm)
      ElMessage.success('创建成功')
    } else {
      await updateCategory(categoryForm.id, categoryForm)
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
.category-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
