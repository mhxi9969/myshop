import request from '@/utils/request'

export default {
    // 添加一个Order
    insert(orderVO, skuPrice) {
        return request({
            url: `/order/order`,
            method: 'post',
            data: {
                order: orderVO,
                skuPrice: skuPrice
            }
        })
    },

}