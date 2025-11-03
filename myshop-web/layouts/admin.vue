<template>
  <div class="admin-layout">
    <!-- 左侧菜单 -->
    <el-container style="height: 100vh">
      <el-aside width="200px" class="sidebar">

        <nuxt-link to="/" class="site-name-link">
          <div class="logo">MyShop</div>
        </nuxt-link>


        <el-menu
            :default-active="$route.path"
            router
            class="el-menu-vertical"
            background-color="#2d3a4b"
            text-color="#fff"
            active-text-color="#409EFF"
        >
          <el-submenu index="1">
            <template #title>
              <i class="el-icon-goods"></i>
              <span>商品管理</span>
            </template>

            <el-menu-item index="/admin/product/brand">品牌管理</el-menu-item>
            <el-menu-item index="/admin/product/category">分类管理</el-menu-item>
            <el-menu-item index="/admin/product/product">商品管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-container>
        <el-header class="header">
          <div class="header-right">
            <div  class="user-info">
              欢迎，{{ username }}
              <el-button type="text" @click="logout">退出登录</el-button>
            </div>

          </div>
        </el-header>

        <el-main class="main-content">
          <nuxt />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import userApi from "~/api/user/user";

export default {
  middleware: 'adminauth',
  data() {
    return {
      username: ''
    }
  },
  created() {
    this.getUserBySession()
  },

  methods: {
    getUserBySession() {
      userApi.getUserBySession()
          .then(res => {
            this.username = res.data.data.record.name

          })
          .catch()
    },

    logout() {
      userApi.logout()
          .then(res => {
            this.name = ''
            this.$router.push('/')
          })

    },

  }
}
</script>

<style>
html, body, #__nuxt {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

.admin-layout {
  display: flex;
  height: 100%;
  margin: 0;
}
.el-container, .el-header, .el-main, .el-aside {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.sidebar, .header {
  box-sizing: border-box;
}

/* 左侧菜单样式 */
.sidebar {
  background: #2d3a4b;
  color: #fff;
}

.logo {
  padding: 20px;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  color: #fff;
  border-bottom: 1px solid #1f2a36;
}

/* 右侧部分 */
.header {
  background: #fff;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  border-bottom: 1px solid #eee;
}

.main-content {
  background: #f5f6f8;
  padding: 20px;
  height: calc(100vh - 60px);
  overflow-y: auto;
}
.header-right {
  display: flex;
  align-items: center; /* 垂直居中 */
  gap: 10px; /* 按钮间距，可调 */
}

.user-info {
  font-weight: 500;
  color: #333;
}

.site-name-link {
  text-decoration: none;
  /* 去掉下划线 */
  color: #000000;
  /* 设置你想要的颜色 */
}
</style>
