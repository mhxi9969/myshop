import request from '@/utils/request'

export default {
  // 添加一个Category
  insert(categoryVO) {
    return request({
      url: `/product/category`,
      method: 'post',
      data: categoryVO
    })
  },

  // 根据id删除一个Category
  deleteById(id) {
    return request({
      url: `/product/category/${id}`,
      method: 'delete'
    })
  },

  // 更新一个Category
  update(categoryVO) {
    return request({
      url: `/product/category`,
      method: 'put',
      data: categoryVO
    })
  },

  // 根据id查询一个Category
  selectById(id) {
    return request({
      url: `/product/category/${id}`,
      method: 'get'
    })
  },

  // 查询所有一级Category
  selectAll() {
    return request({
      url: `/product/category/selectAll`,
      method: 'get'
    })
  },

  // 根据父分类id查询所有二级Category
  selectAll2(id) {
    return request({
      url: `/product/category/selectAll2/${id}`,
      method: 'get'
    })
  },

  // 查询所有二级Category
  all2(id) {
    return request({
      url: `/product/category/all2`,
      method: 'get'
    })
  },


}