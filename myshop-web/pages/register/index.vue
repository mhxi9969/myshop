<template>
  <el-card class="register-card">
    <h2>会員登録</h2>
    <el-form :model="form" :rules="rules" ref="registerForm" label-width="130px">
      <el-form-item label="ユーザー名" prop="name">
        <el-input v-model="form.name" placeholder="ユーザー名を入力してください"></el-input>
      </el-form-item>

      <el-form-item label="email" prop="email">
        <el-input v-model="form.email" placeholder="emailを入力してください"></el-input>
      </el-form-item>

      <el-form-item label="パスワード" prop="password">
        <el-input type="password" v-model="form.password" placeholder="パスワードを入力してください"></el-input>
      </el-form-item>

      <el-form-item label="パスワード確認" prop="confirmPassword">
        <el-input type="password" v-model="form.confirmPassword" placeholder="パスワードを確認してください"></el-input>
      </el-form-item>

      <el-form-item label="認証コード" prop="confirmPassword">
        <el-input  v-model="form.code" placeholder="code"></el-input>
      </el-form-item>

      <el-form-item style="white-space: nowrap;">
        <el-button type="success"  @click="sendCode">認証コードを送信</el-button>
        <el-button type="primary"  @click="onSubmit">会員登録</el-button>
      </el-form-item>

      <el-button type="text" style="float: right" @click="$router.push('/login')">すでにアカウントをお持ちの方はこちら</el-button>


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
        name: '',
        email: '',
        password: '',
        confirmPassword: '',
        code: ''
      },
      rules: {
        name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        email: [{ required: true, message: '请输入メールアドレス', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        confirmPassword: [{ required: true, validator: validateConfirm, trigger: 'blur' }],
        code: [{ required: true, message: '请输入認証コード', trigger: 'blur' }]
      }
    };
  },
  methods: {
    sendCode() {
      userApi.sendCode(this.form.email).then(res => {})
    },

    onSubmit() {
      this.$refs.registerForm.validate(valid => {
        if (true) {

          let vo = {}
          vo.name = this.form.name;
          vo.email = this.form.email;
          vo.password = this.form.password;
          vo.code = this.form.code;

          userApi.register(vo)
            .then(res => {
              if (res.data.code === 20000) {
                this.$router.push('/login')
              } else {
                this.$message.error(res.data.message)
              }

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
