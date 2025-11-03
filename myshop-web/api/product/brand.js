import request from '@/utils/request'

export default {
  // 添加一个Brand
  insert(brandVO) {
    return request({
      url: `/product/brand`,
      method: 'post',
      data: brandVO
    })
  },

  // 根据id删除一个Brand
  deleteById(id) {
    return request({
      url: `/product/brand/${id}`,
      method: 'delete'
    })
  },

  // 更新一个Brand
  update(brandVO) {
    return request({
      url: `/product/brand`,
      method: 'put',
      data: brandVO
    })
  },

  // 根据id查询一个Brand
  selectById(id) {
    return request({
      url: `/product/brand/${id}`,
      method: 'get'
    })
  },

  // 查询所有Brand
  selectAll() {
    return request({
      url: `/product/brand/selectAll`,
      method: 'get'
    })
  },

  // 根据条件查询Brand，带分页
  selectByCondition(current, queryCondition) {
    return request({
      url: `/product/brand/selectByCondition/${current}`,
      method: 'post',
      data: queryCondition
    })
  },

}