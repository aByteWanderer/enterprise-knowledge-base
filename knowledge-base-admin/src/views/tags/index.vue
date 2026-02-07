<template>
  <div class="tag-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>标签列表</span>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>新建标签
          </el-button>
        </div>
      </template>
      
      <el-table :data="tagList" v-loading="loading" style="width: 100%">
        <el-table-column prop="tagName" label="标签名称" width="200" />
        <el-table-column label="颜色" width="100">
          <template #default="{ row }">
            <el-color-picker v-model="row.color" disabled size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="articleCount" label="文章数量" width="120" align="center" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 标签编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新建标签' : '编辑标签'"
      width="400px"
    >
      <el-form ref="formRef" :model="tagForm" :rules="formRules" label-width="80px">
        <el-form-item label="标签名称" prop="tagName">
          <el-input v-model="tagForm.tagName" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="tagForm.color" />
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
import { getTagList, createTag, updateTag, deleteTag } from '@/api/tag'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)

const tagList = ref([])

const tagForm = reactive({
  id: null,
  tagName: '',
  color: '#409EFF'
})

const formRules = {
  tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getTagList()
    tagList.value = res.data || []
  } catch (error) {
    console.error('获取标签列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  dialogType.value = 'create'
  tagForm.id = null
  tagForm.tagName = ''
  tagForm.color = '#409EFF'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(tagForm, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该标签吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTag(row.id)
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
      await createTag(tagForm)
      ElMessage.success('创建成功')
    } else {
      await updateTag(tagForm.id, tagForm)
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
.tag-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
