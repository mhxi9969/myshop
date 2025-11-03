import request from '@/utils/request'

export default {
  // 添加一个Attr
  insert(attrVO) {
    return request({
      url: `/product/attr`,
      method: 'post',
      data: attrVO
    })
  },

  // 根据id删除一个Attr
  deleteById(id) {
    return request({
      url: `/product/attr/${id}`,
      method: 'delete'
    })
  },

  // 更新一个Attr
  update(attrVO) {
    return request({
      url: `/product/attr`,
      method: 'put',
      data: attrVO
    })
  },

  // 根据id查询一个Attr
  selectById(id) {
    return request({
      url: `/product/attr/${id}`,
      method: 'get'
    })
  },

  // 根据二级分类id，查询所有Attr
  selectAll(id) {
    return request({
      url: `/product/attr/selectAll/${id}`,
      method: 'get'
    })
  },

  // 根据二级分类id，查询所有Attr，带选项
  selectAllTree(id) {
    return request({
      url: `/product/attr/selectAllTree/${id}`,
      method: 'get'
    })
  },

}