<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1>404</h1>
      <p>抱歉，您访问的页面不存在</p>
      <el-button type="primary" @click="$router.push('/dashboard')">
        返回首页
      </el-button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.not-found-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
}

.not-found-content {
  text-align: center;
  
  h1 {
    font-size: 120px;
    color: #409EFF;
    margin-bottom: 20px;
  }
  
  p {
    font-size: 18px;
    color: #666;
    margin-bottom: 30px;
  }
}
</style>
