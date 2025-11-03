<template>
  <el-card class="login-card">
    <h2>登录</h2>
    <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">登录</el-button>
        <el-button type="text" @click="$router.push('/register')">没有账号？去注册</el-button>
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
              this.$message.error(err.response.data.message || '登录失败');
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