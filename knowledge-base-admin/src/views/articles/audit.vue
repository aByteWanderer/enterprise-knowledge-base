<template>
  <div class="audit-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>待审核文章</span>
        </div>
      </template>
      
      <el-table :data="pendingList" v-loading="loading" style="width: 100%">
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="authorName" label="作者" width="120" />
        <el-table-column prop="createdAt" label="提交时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="success" link @click="handleApprove(row)">通过</el-button>
            <el-button type="danger" link @click="handleReject(row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 文章查看对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="文章详情"
      width="80%"
      top="5vh"
    >
      <div class="article-detail" v-if="currentArticle">
        <h1 class="article-title">{{ currentArticle.title }}</h1>
        <div class="article-meta">
          <span>分类: {{ currentArticle.categoryName }}</span>
          <span>作者: {{ currentArticle.authorName }}</span>
          <span>发布时间: {{ currentArticle.createdAt }}</span>
        </div>
        <div class="article-summary" v-if="currentArticle.summary">
          <strong>摘要:</strong> {{ currentArticle.summary }}
        </div>
        <div class="article-content" v-html="currentArticle.content"></div>
      </div>
      
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="success" @click="handleApprove(currentArticle)">通过</el-button>
        <el-button type="danger" @click="handleReject(currentArticle)">拒绝</el-button>
      </template>
    </el-dialog>
    
    <!-- 拒绝原因对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="拒绝原因"
      width="400px"
    >
      <el-form>
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectReason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPendingArticles, auditArticle, getArticle } from '@/api/article'

const loading = ref(false)
const viewDialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const pendingList = ref([])
const currentArticle = ref(null)
const rejectReason = ref('')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPendingArticles()
    pendingList.value = res.data || []
  } catch (error) {
    console.error('获取待审核文章失败:', error)
  } finally {
    loading.value = false
  }
}

const handleView = async (row) => {
  try {
    const res = await getArticle(row.id)
    currentArticle.value = res.data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取文章详情失败:', error)
  }
}

const handleApprove = async (article) => {
  if (!article) return
  
  try {
    await auditArticle(article.id, 'APPROVED', '审核通过')
    ElMessage.success('审核通过')
    viewDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('审核失败:', error)
  }
}

const handleReject = (article) => {
  currentArticle.value = article
  rejectReason.value = ''
  viewDialogVisible.value = false
  rejectDialogVisible.value = true
}

const confirmReject = async () => {
  if (!currentArticle.value) return
  
  try {
    await auditArticle(currentArticle.value.id, 'REJECTED', rejectReason.value)
    ElMessage.success('已拒绝')
    rejectDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('拒绝失败:', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.audit-container {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.article-detail {
  .article-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 16px;
    text-align: center;
  }
  
  .article-meta {
    display: flex;
    justify-content: center;
    gap: 24px;
    color: #999;
    margin-bottom: 16px;
  }
  
  .article-summary {
    padding: 16px;
    background: #f5f7fa;
    border-radius: 4px;
    margin-bottom: 16px;
  }
  
  .article-content {
    line-height: 1.8;
    
    :deep(img) {
      max-width: 100%;
    }
  }
}
</style>
