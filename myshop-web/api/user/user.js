import request from '@/utils/request'

export default {
  // 添加一个User
  insert(userVO) {
    return request({
      url: `/user/user`,
      method: 'post',
      data: userVO
    })
  },

  // 根据id删除一个User
  deleteById(id) {
    return request({
      url: `/user/user/${id}`,
      method: 'delete'
    })
  },

  // 更新一个User
  update(userVO) {
    return request({
      url: `/user/user`,
      method: 'put',
      data: userVO
    })
  },

  // 根据id查询一个User
  selectById(id) {
    return request({
      url: `/user/user/${id}`,
      method: 'get'
    })
  },

  // 查询所有User
  selectAll() {
    return request({
      url: `/user/user/selectAll`,
      method: 'get'
    })
  }, 
 
 
  //注册
  register(vo) {
    return request({
      url: `/user/user/register`,
      method: 'post',
      data: vo
    })
  },

  //登录
  login(vo) {
    return request({
      url: `/user/user/login`,
      method: 'post',
      data: vo
    })
  },

  //根据sessionid查询用户信息
  getUserBySession() {
    return request({
      url: `/user/user/getUserBySession`,
      method: 'post',
    })
  },

  //退出登录
  logout() {
    return request({
      url: `/user/user/logout`,
      method: 'post',
    })
  },

  //发送验证码
  sendCode(email) {
    return request({
      url: `/user/user/sendCode`,
      method: 'post',
      data: { email }  //用对象
    })
  },

}