<template>
  <div class="article-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item label="标题" class="search-item">
              <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width: 160px" />
            </el-form-item>
            <el-form-item label="分类" class="search-item">
              <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable style="width: 140px">
                <el-option
                  v-for="cat in categories"
                  :key="cat.id"
                  :label="cat.categoryName"
                  :value="cat.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" class="search-item">
              <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="待审核" value="PENDING" />
                <el-option label="已发布" value="PUBLISHED" />
                <el-option label="已归档" value="ARCHIVED" />
              </el-select>
            </el-form-item>
            <el-form-item class="search-item">
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>
      
      <div class="toolbar">
        <el-button type="primary" @click="handleCreate">
          <el-icon><Plus /></el-icon>新建文章
        </el-button>
        <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
          <el-icon><Delete /></el-icon>批量删除
        </el-button>
      </div>
      
      <el-table
        :data="articleList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="title" label="标题" show-overflow-tooltip min-width="200" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column label="标签" width="150">
          <template #default="{ row }">
            <template v-if="row.tags && row.tags.length">
              <el-tag 
                v-for="tag in row.tags.slice(0, 2)" 
                :key="tag.id" 
                size="small" 
                class="tag-inline"
              >
                {{ tag.tagName }}
              </el-tag>
              <span v-if="row.tags.length > 2" class="more-tags">+{{ row.tags.length - 2 }}</span>
            </template>
            <span v-else class="no-tags">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="authorName" label="作者" width="80" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="80" align="center" />
        <el-table-column prop="createdAt" label="创建时间" width="140" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button 
              v-if="row.status === 'DRAFT'" 
              type="warning" 
              link 
              @click="handleSubmitAudit(row)"
            >提交审核</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
    
    <!-- 文章编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新建文章' : '编辑文章'"
      width="80%"
      top="5vh"
    >
      <el-form ref="formRef" :model="articleForm" :rules="formRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="articleForm.title" placeholder="请输入标题" maxlength="200" show-word-limit />
        </el-form-item>
        
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="articleForm.categoryId" placeholder="请选择分类" style="width: 100%;">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.categoryName"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="标签">
          <el-select v-model="articleForm.tagIds" multiple placeholder="请选择标签" style="width: 100%;">
            <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.tagName"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="articleForm.summary" type="textarea" :rows="3" placeholder="请输入摘要" />
        </el-form-item>
        
        <el-form-item label="内容" prop="content">
          <div class="editor-container">
            <Toolbar
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
              style="border-bottom: 1px solid #ccc"
            />
            <Editor
              :defaultConfig="editorConfig"
              :mode="mode"
              v-model="articleForm.content"
              style="height: 400px; overflow-y: auto"
              @onCreated="handleEditorCreated"
              @onChange="handleEditorChange"
            />
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getArticleList, createArticle, updateArticle, deleteArticle, submitArticle } from '@/api/article'
import { getTagList } from '@/api/tag'
import { getCategoryTree } from '@/api/category'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { DomEditor } from '@wangeditor/editor'

const router = useRouter()

const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)
const editorRef = ref(null)
const mode = 'default'

const articleList = ref([])
const categories = ref([])
const tags = ref([])
const selectedIds = ref([])

const searchForm = reactive({
  title: '',
  categoryId: null,
  status: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const articleForm = reactive({
  id: null,
  title: '',
  categoryId: null,
  summary: '',
  content: '',
  tagIds: []
})

const toolbarConfig = {
  excludeKeys: ['fullScreen']
}

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 10 * 1024 * 1024,
    }
  }
}

const handleEditorCreated = (editor) => {
  editorRef.value = editor
}

const handleEditorChange = (editor) => {
  // 确保内容同步到表单
  articleForm.content = editor.getHtml()
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
  editorRef.value = null
})

const formRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      title: searchForm.title || undefined,
      categoryId: searchForm.categoryId || undefined
    }
    const res = await getArticleList(params)
    articleList.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取文章列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchCategories = async () => {
  try {
    const res = await getCategoryTree()
    categories.value = res.data || []
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

const fetchTags = async () => {
  try {
    const res = await getTagList()
    tags.value = res.data || []
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.title = ''
  searchForm.categoryId = null
  searchForm.status = ''
  handleSearch()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleCreate = () => {
  dialogType.value = 'create'
  articleForm.id = null
  articleForm.title = ''
  articleForm.categoryId = null
  articleForm.summary = ''
  articleForm.content = ''
  articleForm.tagIds = []
  dialogVisible.value = true
  nextTick(() => {
    if (editorRef.value) {
      editorRef.value.clear()
    }
  })
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(articleForm, {
    id: row.id,
    title: row.title,
    categoryId: row.categoryId,
    summary: row.summary,
    content: row.content || '',
    tagIds: row.tags?.map(t => t.id) || []
  })
  dialogVisible.value = true
  nextTick(() => {
    if (editorRef.value && articleForm.content) {
      editorRef.value.setHtml(articleForm.content)
    } else if (editorRef.value) {
      editorRef.value.clear()
    }
  })
}

const handleView = (row) => {
  router.push(`/articles/${row.id}`)
}

const handleSubmitAudit = (row) => {
  if (row.status !== 'DRAFT') {
    ElMessage.warning('只有草稿状态的文章可以提交审核')
    return
  }
  
  ElMessageBox.confirm('确定要提交该文章进行审核吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await submitArticle(row.id)
      ElMessage.success('提交审核成功')
      fetchData()
    } catch (error) {
      console.error('提交审核失败:', error)
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该文章吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteArticle(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const handleBatchDelete = () => {
  if (!selectedIds.value.length) return
  
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 篇文章吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      for (const id of selectedIds.value) {
        await deleteArticle(id)
      }
      ElMessage.success('批量删除成功')
      fetchData()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    
    if (dialogType.value === 'create') {
      await createArticle(articleForm)
      ElMessage.success('创建成功')
    } else {
      await updateArticle(articleForm.id, articleForm)
      ElMessage.success('更新成功')
    }
    
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

const handlePageChange = (page) => {
  pagination.pageNum = page
  fetchData()
}

const getStatusType = (status) => {
  const map = {
    DRAFT: 'info',
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger',
    PUBLISHED: 'success',
    ARCHIVED: 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    DRAFT: '草稿',
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已拒绝',
    PUBLISHED: '已发布',
    ARCHIVED: '已归档'
  }
  return map[status] || status
}

onMounted(() => {
  fetchData()
  fetchCategories()
  fetchTags()
})
</script>

<style lang="scss" scoped>
.article-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 0;
    
    .search-item {
      margin-right: 8px;
      margin-bottom: 8px;
      
      :deep(.el-form-item__label) {
        padding-right: 8px;
      }
    }
  }
  
  .toolbar {
    margin-bottom: 16px;
  }
  
  .pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
  }
  
  .tag-inline {
    margin-right: 4px;
    margin-bottom: 2px;
  }
  
  .more-tags {
    color: #909399;
    font-size: 12px;
  }
  
  .no-tags {
    color: #c0c4cc;
  }
}
</style>
