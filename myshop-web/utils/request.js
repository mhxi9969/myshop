import axios from 'axios'

const service = axios.create({
  baseURL: process.env.API_BASE,
  //baseURL: "http://localhost:8000/api",   //部署在本地3000端口时，直接请求网关，保证同一域名可以发送cookie
  //baseURL: "http://52.69.65.203/api",   //部署在nginx时，通过nginx转发给网关，保证同一域名可以发送cookie
  timeout: 5000,       // 请求超时 5 秒
  withCredentials: true   // 允许跨域携带 Cookie
})


export default service
