import request from '@/utils/request'

export default {
  // 添加一个Order
  insert(orderVO, skuPrice, token) {
    return request({
      url: `/order/order`,
      method: 'post',
      data: {
        order: orderVO,
        skuPrice: skuPrice,
        token: token
      }
    })
  },

  // 根据id删除一个Order
  deleteById(id) {
    return request({
      url: `/order/order/${id}`,
      method: 'delete'
    })
  },

  // 更新一个Order
  update(orderVO) {
    return request({
      url: `/order/order`,
      method: 'put',
      data: orderVO
    })
  },

  // 根据id查询一个Order
  selectById(id) {
    return request({
      url: `/order/order/${id}`,
      method: 'get'
    })
  },

  // 根据用户session，查询所有Order
  selectAll() {
    return request({
      url: `/order/order/selectAll`,
      method: 'get'
    })
  },

  poll(id) {
    return request({
      url: `/order/order/poll/${id}`,
      method: 'get'
    })
  },

  getOrderToken() {
    return request({
      url: `/order/order/token`,
      method: 'get'
    })
  }


}