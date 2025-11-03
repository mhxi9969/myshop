import request from '@/utils/request'

export default {
  // 添加一个OrderItem
  insert(orderItemVO) {
    return request({
      url: `/order/orderItem`,
      method: 'post',
      data: orderItemVO
    })
  },

  // 根据id删除一个OrderItem
  deleteById(id) {
    return request({
      url: `/order/orderItem/${id}`,
      method: 'delete'
    })
  },

  // 更新一个OrderItem
  update(orderItemVO) {
    return request({
      url: `/order/orderItem`,
      method: 'put',
      data: orderItemVO
    })
  },

  // 根据id查询一个OrderItem
  selectById(id) {
    return request({
      url: `/order/orderItem/${id}`,
      method: 'get'
    })
  },

  // 查询所有OrderItem
  selectAll(id) {
    return request({
      url: `/order/orderItem/selectAll/${id}`,
      method: 'get'
    })
  },


}