<template>
  <div class="in-wrap">
    <header id="header">
      <div class="header">

        <div class="header-left">
          <nuxt-link to="/" class="site-name-link">
            <span class="site-name">MyShop</span>
          </nuxt-link>
        </div>

        <div class="header-center">
          <el-input placeholder="商品を検索" v-model="keyword" class="search-input" @keyup.enter.native="onSearch">
            <el-button slot="append" icon="el-icon-search" @click="onSearch"></el-button>
          </el-input>
        </div>

        <div class="header-right">
          <el-button type="primary" @click="goCart">カート</el-button>
          <el-button type="success" @click="goMyOrder">マイオーダー</el-button>

          <div v-if="username" class="user-info">
            ようこそ，{{ username }}
            <el-button type="danger" @click="logout">ログアウト</el-button>
          </div>

          <div v-else class="user-info">
            <el-button type="danger" @click="goLogin">ログイン</el-button>
            <el-button type="danger" @click="goRegister">会員登録</el-button>
          </div>
        </div>


      </div>
    </header>

    <nuxt />

    <footer id="footer">
      <div class="footer-content">

        <div class="footer-section">
          <h3>会社情報</h3>
          <p>MyShopはオンラインショッピングプラットフォームです。お買い物をもっと簡単に。</p>
        </div>

        <div class="footer-section">
          <h3>お問い合わせ</h3>
          <p>メール：support@myshop.com</p>
          <p>電話：+81-03-1234-5678</p>
          <p>住所：東京都渋谷区</p>
        </div>
      </div>

      <div class="footer-bottom">
        Copyright © 2025 MyShop. All Rights Reserved.
      </div>
    </footer>
  </div>
</template>

<script>
import userApi from '@/api/user/user'
import cookie from 'js-cookie'

export default {
  data() {
    return {
      keyword: '',
      username: ''
    }
  },
  created() {
    this.getUserBySession()
  },

  methods: {
    onSearch() {
      const keyword = this.keyword ? this.keyword.trim() : '';
      // 空字符串也传空字符串，而不是 undefined
      this.$router.push({ path: '/search/' + keyword});
    },
    goLogin() {
      this.$router.push('/login')
    },
    goRegister() {
      this.$router.push('/register')
    },
    goCart() {
      this.$router.push('/cart')
    },
    goMyOrder() {
      this.$router.push('/myorder')
    },

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
          this.username = ''
        })
    },
  }
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 30px;
  background: #ffffff;
  border-bottom: 1px solid #f2f2f2;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  width: 40px;
  margin-right: 10px;
}

.site-name-link {
  text-decoration: none;
  /* 去掉下划线 */
  color: #000000;
  /* 设置你想要的颜色 */
}


.site-name {
  font-size: 18px;
  font-weight: bold;
}

.header-center {
  flex: 1;
  padding: 0 80px;
}

.search-input {
  width: 100%;
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


.footer {
  margin-top: 50px;
  background-color: #f5f5f5;
  padding: 30px 20px;
  color: #666;
  font-size: 14px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  max-width: 1000px;
  margin: 0 auto;
}

.footer-section h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 10px;
}

.footer-section p {
  margin: 4px 0;
}

.footer-section a {
  color: #409EFF;
  text-decoration: none;
}

.footer-bottom {
  text-align: center;
  margin-top: 20px;
  border-top: 1px solid #ddd;
  padding-top: 10px;
  color: #999;
}
</style>
