import request from '@/utils/request'

export default {
  // 添加一个SkuAttrValue
  insert(skuAttrValueVO) {
    return request({
      url: `/product/skuAttrValue`,
      method: 'post',
      data: skuAttrValueVO
    })
  },

  // 根据id删除一个SkuAttrValue
  deleteById(id) {
    return request({
      url: `/product/skuAttrValue/${id}`,
      method: 'delete'
    })
  },

  // 更新一个SkuAttrValue
  update(skuAttrValueVO) {
    return request({
      url: `/product/skuAttrValue`,
      method: 'put',
      data: skuAttrValueVO
    })
  },

  // 根据id查询一个SkuAttrValue
  selectById(id) {
    return request({
      url: `/product/skuAttrValue/${id}`,
      method: 'get'
    })
  },

  // 查询所有SkuAttrValue
  selectAll() {
    return request({
      url: `/product/skuAttrValue/selectAll`,
      method: 'get'
    })
  },


}