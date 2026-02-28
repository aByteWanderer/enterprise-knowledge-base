<template>
  <div class="help-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <h2>系统使用帮助</h2>
        </div>
      </template>

      <el-tabs v-model="activeTab" tab-position="left" class="help-tabs">
        <!-- 快速开始 -->
        <el-tab-pane label="快速开始" name="quickstart">
          <div class="help-content">
            <h3>快速开始</h3>
            <el-steps :active="stepsActive" finish-status="success" align-center>
              <el-step title="登录系统" description="使用账号密码登录" />
              <el-step title="创建分类" description="添加知识分类" />
              <el-step title="创建标签" description="添加文章标签" />
              <el-step title="撰写文章" description="创建并提交审核" />
              <el-step title="审核发布" description="审核通过后发布" />
            </el-steps>
          </div>
        </el-tab-pane>

        <!-- 用户管理 -->
        <el-tab-pane label="用户管理" name="users">
          <div class="help-content">
            <h3>用户管理</h3>
            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="查看用户列表" name="user-list">
                <p>在用户管理页面，可以查看所有已注册用户的信息，包括：</p>
                <ul>
                  <li>用户名</li>
                  <li>邮箱</li>
                  <li>手机号</li>
                  <li>状态（启用/禁用）</li>
                  <li>创建时间</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="创建新用户" name="user-create">
                <p>点击「新建用户」按钮，填写以下信息：</p>
                <ul>
                  <li>用户名（必填）</li>
                  <li>邮箱（必填）</li>
                  <li>手机号（选填）</li>
                  <li>状态（启用/禁用）</li>
                  <li>角色（选择用户角色）</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="分配角色" name="user-role">
                <p>点击用户行的「分配角色」按钮，可以：</p>
                <ul>
                  <li>查看当前用户已有角色</li>
                  <li>选择需要分配的新角色</li>
                  <li>保存后立即生效</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="编辑/删除用户" name="user-edit">
                <p>点击「编辑」可修改用户信息，点击「删除」需确认后删除用户。</p>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>

        <!-- 角色管理 -->
        <el-tab-pane label="角色管理" name="roles">
          <div class="help-content">
            <h3>角色管理</h3>
            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="角色列表" name="role-list">
                <p>查看系统中所有角色及其权限配置。</p>
              </el-collapse-item>
              <el-collapse-item title="创建角色" name="role-create">
                <p>点击「新建角色」，填写：</p>
                <ul>
                  <li>角色名称</li>
                  <li>角色编码（唯一标识）</li>
                  <li>描述</li>
                  <li>状态</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="分配权限" name="role-permission">
                <p>点击「权限」按钮，为角色勾选需要的权限。</p>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>

        <!-- 权限管理 -->
        <el-tab-pane label="权限管理" name="permissions">
          <div class="help-content">
            <h3>权限管理</h3>
            <p>权限管理用于维护系统的权限标识，支持：</p>
            <ul>
              <li>查看权限列表</li>
              <li>新增权限</li>
              <li>编辑权限信息</li>
              <li>删除权限</li>
            </ul>
          </div>
        </el-tab-pane>

        <!-- 分类管理 -->
        <el-tab-pane label="分类管理" name="categories">
          <div class="help-content">
            <h3>分类管理</h3>
            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="创建分类" name="cat-create">
                <p>点击「新建分类」，填写：</p>
                <ul>
                  <li>分类名称</li>
                  <li>父级分类（可选，用于创建二级分类）</li>
                  <li>排序号</li>
                  <li>状态</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="编辑/删除" name="cat-edit">
                <p>点击操作列的按钮进行编辑或删除。</p>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>

        <!-- 标签管理 -->
        <el-tab-pane label="标签管理" name="tags">
          <div class="help-content">
            <h3>标签管理</h3>
            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="创建标签" name="tag-create">
                <p>点击「新建标签」，填写：</p>
                <ul>
                  <li>标签名称</li>
                  <li>标签颜色（选择显示颜色）</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="使用标签" name="tag-use">
                <p>标签可在创建文章时选择，帮助文章分类和检索。</p>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>

        <!-- 文章管理 -->
        <el-tab-pane label="文章管理" name="articles">
          <div class="help-content">
            <h3>文章管理</h3>
            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="创建文章" name="art-create">
                <p>点击「新建文章」，填写：</p>
                <ul>
                  <li>标题</li>
                  <li>分类（必选）</li>
                  <li>标签（可选）</li>
                  <li>摘要</li>
                  <li>正文内容（富文本编辑器）</li>
                </ul>
                <p>文章创建后默认为「草稿」状态。</p>
              </el-collapse-item>
              <el-collapse-item title="提交审核" name="art-submit">
                <p>草稿状态的文章可「提交审核」，提交后状态变为「待审核」。</p>
              </el-collapse-item>
              <el-collapse-item title="审核发布" name="art-audit">
                <p>在「文章审核」页面，可对待审核文章进行：</p>
                <ul>
                  <li>查看文章内容</li>
                  <li>审核通过（发布文章）</li>
                  <li>审核拒绝（退回修改）</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="文章状态" name="art-status">
                <p>文章有以下状态：</p>
                <ul>
                  <li><el-tag type="info">草稿</el-tag> - 未提交审核</li>
                  <li><el-tag type="warning">待审核</el-tag> - 等待审核</li>
                  <li><el-tag type="success">已发布</el-tag> - 审核通过</li>
                  <li><el-tag type="danger">已拒绝</el-tag> - 审核未通过</li>
                </ul>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>

        <!-- 暗色模式 -->
        <el-tab-pane label="暗色模式" name="darkmode">
          <div class="help-content">
            <h3>暗色模式</h3>
            <p>点击顶部导航栏的月亮/太阳图标可切换暗色/亮色模式。</p>
            <ul>
              <li>🌙 点击切换到暗色模式</li>
              <li>☀️ 点击切换到亮色模式</li>
            </ul>
            <p>主题设置会自动保存，刷新页面后保持不变。</p>
          </div>
        </el-tab-pane>

        <!-- 常见问题 -->
        <el-tab-pane label="常见问题" name="faq">
          <div class="help-content">
            <h3>常见问题</h3>
            <el-collapse v-model="activeCollapse">
              <el-collapse-item title="Q1: 忘记密码怎么办？" name="faq1">
                <p>请联系管理员重置密码。</p>
              </el-collapse-item>
              <el-collapse-item title="Q2: 为什么无法创建文章？" name="faq2">
                <p>请确保：</p>
                <ul>
                  <li>已选择分类</li>
                  <li>标题不为空</li>
                  <li>内容不为空</li>
                </ul>
              </el-collapse-item>
              <el-collapse-item title="Q3: 提交审核后可以修改吗？" name="faq3">
                <p>提交审核后，文章进入待审核状态，无法直接修改。如需修改，可联系管理员退回。</p>
              </el-collapse-item>
              <el-collapse-item title="Q4: 如何让文章显示在首页？" name="faq4">
                <p>在文章列表中，可以设置「置顶」或「推荐」属性。</p>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('quickstart')
const activeCollapse = ref(['user-list'])
const stepsActive = ref(0)
</script>

<style lang="scss" scoped>
.help-container {
  .card-header {
    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
    }
  }
  
  .help-tabs {
    min-height: 500px;
    
    :deep(.el-tabs__content) {
      padding: 0 20px;
    }
  }
  
  .help-content {
    h3 {
      margin-bottom: 20px;
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
    
    ul {
      padding-left: 20px;
      
      li {
        margin-bottom: 8px;
        line-height: 1.8;
        color: #606266;
      }
    }
    
    p {
      line-height: 1.8;
      color: #606266;
      margin-bottom: 12px;
    }
    
    .el-steps {
      margin: 30px 0;
    }
  }
}
</style>
