import axios from 'axios'

const service = axios.create({
  baseURL: process.env.API_BASE,
  timeout: 5000,       // 请求超时 5 秒
  withCredentials: true   // 允许跨域携带 Cookie
})


export default service
