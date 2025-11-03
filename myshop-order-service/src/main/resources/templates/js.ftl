import request from '@/utils/request'

export default {
  // 添加一个${entity}
  insert(${entityLower}VO) {
    return request({
      url: `/order/${entityLower}`,
      method: 'post',
      data: ${entityLower}VO
    })
  },

  // 根据id删除一个${entity}
  deleteById(id) {
    return request({
      url: `/order/${entityLower}/<#noparse>${id}</#noparse>`,
      method: 'delete'
    })
  },

  // 更新一个${entity}
  update(${entityLower}VO) {
    return request({
      url: `/order/${entityLower}`,
      method: 'put',
      data: ${entityLower}VO
    })
  },

  // 根据id查询一个${entity}
  selectById(id) {
    return request({
      url: `/order/${entityLower}/<#noparse>${id}</#noparse>`,
      method: 'get'
    })
  },

  // 查询所有${entity}
  selectAll() {
    return request({
      url: `/order/${entityLower}/selectAll`,
      method: 'get'
    })
  },


}