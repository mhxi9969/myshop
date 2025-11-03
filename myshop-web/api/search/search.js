import request from '@/utils/request'

export default {

  // 根据条件查询ProductSku，带分页
  search(page, queryCondition) {
    return request({
      url: `/search/search/search/${page}`,
      method: 'post',
      data: queryCondition
    })
  },

  // 根据spu检索商品sku
  searchBySpuID(id) {
    return request({
      url: `/search/search/searchBySpuID/${id}`,
      method: 'post',
    })
  },

}