<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.articleCount }}</div>
              <div class="stat-label">文章总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;">
              <el-icon><Folder /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.categoryCount }}</div>
              <div class="stat-label">分类数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C;">
              <el-icon><PriceTag /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.tagCount }}</div>
              <div class="stat-label">标签数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;">
              <el-icon><View /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.viewCount }}</div>
              <div class="stat-label">总浏览量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近文章</span>
              <el-button type="primary" link @click="$router.push('/articles')">
                查看更多
              </el-button>
            </div>
          </template>
          <el-table :data="recentArticles" style="width: 100%">
            <el-table-column prop="title" label="标题" show-overflow-tooltip />
            <el-table-column prop="categoryName" label="分类" width="120" />
            <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
            <el-table-column prop="createdAt" label="发布时间" width="160" />
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>热门标签</span>
            </div>
          </template>
          <div class="tag-list">
            <el-tag
              v-for="tag in hotTags"
              :key="tag.id"
              :color="tag.color"
              effect="dark"
              class="tag-item"
            >
              {{ tag.tagName }} ({{ tag.articleCount }})
            </el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticleList } from '@/api/article'
import { getHotTags } from '@/api/tag'

const stats = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0,
  viewCount: 0
})

const recentArticles = ref([])
const hotTags = ref([])

const fetchData = async () => {
  try {
    // 获取文章列表
    const articlesRes = await getArticleList({ pageNum: 1, pageSize: 5 })
    recentArticles.value = articlesRes.data.list || []
    stats.value.articleCount = articlesRes.data.total || 0
    
    // 统计浏览量
    stats.value.viewCount = recentArticles.value.reduce((sum, article) => {
      return sum + (article.viewCount || 0)
    }, 0)
    
    // 获取热门标签
    const tagsRes = await getHotTags(10)
    hotTags.value = tagsRes.data || []
    stats.value.tagCount = hotTags.value.length
  } catch (error) {
    console.error('获取数据失败:', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 0;
}

.stat-card {
  .stat-content {
    display: flex;
    align-items: center;
    gap: 16px;
    
    .stat-icon {
      width: 56px;
      height: 56px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .el-icon {
        font-size: 28px;
        color: #fff;
      }
    }
    
    .stat-info {
      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #333;
      }
      
      .stat-label {
        font-size: 14px;
        color: #999;
        margin-top: 4px;
      }
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  
  .tag-item {
    font-size: 12px;
  }
}
</style>
