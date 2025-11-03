import request from '@/utils/request'

export default {
  // 添加一个AttrValue
  insert(attrValueVO) {
    return request({
      url: `/product/attrValue`,
      method: 'post',
      data: attrValueVO
    })
  },

  // 根据id删除一个AttrValue
  deleteById(id) {
    return request({
      url: `/product/attrValue/${id}`,
      method: 'delete'
    })
  },

  // 更新一个AttrValue
  update(attrValueVO) {
    return request({
      url: `/product/attrValue`,
      method: 'put',
      data: attrValueVO
    })
  },

  // 根据id查询一个AttrValue
  selectById(id) {
    return request({
      url: `/product/attrValue/${id}`,
      method: 'get'
    })
  },

  // 查询所有AttrValue
  selectAll(id) {
    return request({
      url: `/product/attrValue/selectAll/${id}`,
      method: 'get'
    })
  },


}