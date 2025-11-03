<template>
  <el-card class="register-card">
    <h2>会員登録</h2>
    <el-form :model="form" :rules="rules" ref="registerForm" label-width="110px">
      <el-form-item label="ユーザー名" prop="username">
        <el-input v-model="form.username" placeholder="ユーザー名を入力してください"></el-input>
      </el-form-item>

      <el-form-item label="パスワード" prop="password">
        <el-input type="password" v-model="form.password" placeholder="パスワードを入力してください"></el-input>
      </el-form-item>

      <el-form-item label="パスワード確認" prop="confirmPassword">
        <el-input type="password" v-model="form.confirmPassword" placeholder="パスワードを確認してください"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">会員登録</el-button>
        <el-button type="text" @click="$router.push('/login')">すでにアカウントをお持ちの方はこちら</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import userApi from '@/api/user/user'

export default {
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        confirmPassword: [{ validator: validateConfirm, trigger: 'blur' }]
      }
    };
  },
  methods: {
    onSubmit() {
      this.$refs.registerForm.validate(valid => {
        if (true) {

          let vo = {}
          vo.name = this.form.username
          vo.password = this.form.password

          userApi.register(vo)
            .then(res => {
              this.$router.push('/login');
            })
        }
      })
    }
  }
}
</script>

<style scoped>
.register-card {
  width: 400px;
  margin: 100px auto;
  padding: 20px;
}
</style>
