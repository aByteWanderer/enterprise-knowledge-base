<template>
  <div class="article-detail-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <el-page-header @back="router.back()" title="返回">
            <template #content>
              <span class="article-title">{{ article.title }}</span>
            </template>
          </el-page-header>
        </div>
      </template>
      
      <div class="article-meta">
        <div class="meta-left">
          <el-tag v-if="article.categoryName" type="primary" size="small">{{ article.categoryName }}</el-tag>
          <el-tag v-for="tag in article.tags" :key="tag.id" size="small" class="tag-item">
            {{ tag.tagName }}
          </el-tag>
        </div>
        <div class="meta-right">
          <span class="meta-item">
            <el-icon><User /></el-icon>
            {{ article.authorName }}
          </span>
          <span class="meta-item">
            <el-icon><View /></el-icon>
            {{ article.viewCount || 0 }}
          </span>
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            {{ formatDate(article.createdAt) }}
          </span>
          <el-tag :type="getStatusType(article.status)" size="small">{{ getStatusText(article.status) }}</el-tag>
        </div>
      </div>
      
      <el-divider />
      
      <div class="article-content" v-html="article.content"></div>
      
      <el-divider v-if="article.attachments && article.attachments.length > 0" />
      
      <div v-if="article.attachments && article.attachments.length > 0" class="attachments-section">
        <h4>附件下载</h4>
        <div class="attachment-list">
          <div v-for="attachment in article.attachments" :key="attachment.id" class="attachment-item">
            <el-icon><Document /></el-icon>
            <span>{{ attachment.fileName }}</span>
            <el-button type="primary" link size="small" @click="downloadAttachment(attachment)">
              下载
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getArticle } from '@/api/article'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const article = ref({})

const fetchArticle = async () => {
  const id = route.params.id
  if (!id) {
    ElMessage.error('文章ID不存在')
    router.back()
    return
  }
  
  loading.value = true
  try {
    const res = await getArticle(id)
    if (res.data) {
      article.value = res.data
    } else {
      ElMessage.error('文章不存在')
      router.back()
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN')
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

const downloadAttachment = (attachment) => {
  ElMessage.info('下载功能需要后端支持')
}

onMounted(() => {
  fetchArticle()
})
</script>

<style lang="scss" scoped>
.article-detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.article-title {
  font-size: 20px;
  font-weight: bold;
  margin-left: 16px;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  padding: 10px 0;
  
  .meta-left {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .tag-item {
      margin-left: 8px;
    }
  }
  
  .meta-right {
    display: flex;
    align-items: center;
    gap: 16px;
    
    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #909399;
      font-size: 14px;
    }
  }
}

.article-content {
  min-height: 300px;
  padding: 20px 0;
  
  :deep(img) {
    max-width: 100%;
    height: auto;
  }
  
  :deep(p) {
    line-height: 1.8;
    margin-bottom: 16px;
  }
}

.attachments-section {
  h4 {
    margin-bottom: 12px;
    color: #303133;
  }
  
  .attachment-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
    
    .attachment-item {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 12px;
      background-color: #f5f7fa;
      border-radius: 4px;
    }
  }
}
</style>
