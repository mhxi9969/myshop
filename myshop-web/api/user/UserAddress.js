import request from '@/utils/request'

export default {
  // 添加一个UserAddress
  insert(userAddressVO) {
    return request({
      url: `/user/userAddress`,
      method: 'post',
      data: userAddressVO
    })
  },

  // 根据id删除一个UserAddress
  deleteById(id) {
    return request({
      url: `/user/userAddress/${id}`,
      method: 'delete'
    })
  },

  // 更新一个UserAddress
  update(userAddressVO) {
    return request({
      url: `/user/userAddress`,
      method: 'put',
      data: userAddressVO
    })
  },

  // 根据id查询一个UserAddress
  selectById(id) {
    return request({
      url: `/user/userAddress/${id}`,
      method: 'get'
    })
  },

  // 查询所有UserAddress
  selectAll() {
    return request({
      url: `/user/userAddress/selectAll`,
      method: 'get'
    })
  },


}