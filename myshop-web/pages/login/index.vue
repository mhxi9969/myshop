<template>
  <el-card class="login-card">
    <h2>ログイン</h2>
    <el-form :model="form" :rules="rules" ref="loginForm" label-width="100px">
      <el-form-item label="ユーザー名" prop="username">
        <el-input v-model="form.username" placeholder="ユーザー名を入力してください"></el-input>
      </el-form-item>

      <el-form-item label="パスワード" prop="password">
        <el-input type="password" v-model="form.password" placeholder="パスワードを入力してください"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary"  @click="onSubmit">ログイン</el-button>
        <el-button type="text" @click="$router.push('/register')">アカウントをお持ちでない方はこちら</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import userApi from '@/api/user/user'

export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    };
  },
  methods: {
    onSubmit() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          let vo = {}
          vo.name = this.form.username
          vo.password = this.form.password

          userApi.login(vo)
            .then(res => {
              userApi.getUserBySession()
                  .then(res => {
                    let role = res.data.data.record.role

                    if (role === 'ADMIN') {
                      this.$router.push('/admin')
                    } else {
                      this.$router.push('/')
                    }
                  })

            }).catch(err => {
              this.$message.error(err.response.data.message || 'ログインに失敗しました');
            })
        }
      })
    }
  }
};
</script>

<style scoped>
.login-card {
  width: 400px;
  margin: 100px auto;
  padding: 20px;
}
</style>