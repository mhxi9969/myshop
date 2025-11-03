<template>
  <el-card class="register-card">
    <h2>注册</h2>
    <el-form :model="form" :rules="rules" ref="registerForm" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>

      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="form.confirmPassword" placeholder="请确认密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">注册</el-button>
        <el-button type="text" @click="$router.push('/login')">已有账号？去登录</el-button>
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
              this.$message.success('注册成功');
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
