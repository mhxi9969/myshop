import request from '@/utils/request'

export default {


  // 添加一个cartVO
  insert(cartVO) {
    return request({
      url: `/cart/cart`,
      method: 'post',
      data: cartVO
    })
  },

  // 根据id删除一个cartVO
  deleteById(id) {
    return request({
      url: `/cart/cart/${id}`,
      method: 'delete'
    })
  },

  // 更新一个cartVO
  update(cartVO) {
    return request({
      url: `/cart/cart`,
      method: 'put',
      data: cartVO
    })
  },


  // 查询所有cartVO
  selectAll() {
    return request({
      url: `/cart/cart/selectAll`,
      method: 'get'
    })
  },

  // 查询所有cartVO
  selectAllChecked() {
    return request({
      url: `/cart/cart/selectAllChecked`,
      method: 'get'
    })
  },
  
}