import request from '@/utils/request'

export default {
  // 添加一个ProductSku
  insert(productSkuVO) {
    return request({
      url: `/product/productSku`,
      method: 'post',
      data: productSkuVO
    })
  },

  // 根据id删除一个ProductSku
  deleteById(id) {
    return request({
      url: `/product/productSku/${id}`,
      method: 'delete'
    })
  },

  // 更新一个ProductSku
  update(productSkuVO) {
    return request({
      url: `/product/productSku`,
      method: 'put',
      data: productSkuVO
    })
  },

  // 根据id查询一个ProductSku
  selectById(id) {
    return request({
      url: `/product/productSku/${id}`,
      method: 'get'
    })
  },

  // 根据id查询一个ProductSku
  selectByIdTree(id) {
    return request({
      url: `/product/productSku/tree/${id}`,
      method: 'get'
    })
  },

  // 根据spu id查询所有ProductSku
  selectAllTreeBySpuId(id) {
    return request({
      url: `/product/productSku/selectAllTreeBySpuId/${id}`,
      method: 'get'
    })
  },


  // 根据条件查询ProductSku，带分页
  selectByCondition(current, queryCondition) {
    return request({
      url: `/product/productSku/selectByCondition/${current}`,
      method: 'post',
      data: queryCondition
    })
  },

}