import request from '@/utils/request'

export default {
  // 添加一个ProductSpu
  insert(productSpuVO) {
    return request({
      url: `/product/productSpu`,
      method: 'post',
      data: productSpuVO
    })
  },

  // 根据id删除一个ProductSpu
  deleteById(id) {
    return request({
      url: `/product/productSpu/${id}`,
      method: 'delete'
    })
  },

  // 更新一个ProductSpu
  update(productSpuVO) {
    return request({
      url: `/product/productSpu`,
      method: 'put',
      data: productSpuVO
    })
  },

  // 根据id查询一个ProductSpu
  selectById(id) {
    return request({
      url: `/product/productSpu/${id}`,
      method: 'get'
    })
  },

  // 查询所有ProductSpu
  selectAll() {
    return request({
      url: `/product/productSpu/selectAll`,
      method: 'get'
    })
  },

  // 根据条件查询ProductSpu，带分页
  selectByCondition(current, queryCondition) {
    return request({
      url: `/product/productSpu/selectByCondition/${current}`,
      method: 'post',
      data: queryCondition
    })
  },

}