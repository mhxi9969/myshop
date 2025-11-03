<template>
  <div class="cart-page">
    <!-- 表格 -->
    <el-table :data="cartList" style="width: 100%">

      <el-table-column label="" width="120">
        <template #default="scope">
          <img :src="scope.row.sku.picture" class="cart-product-img"/>
        </template>
      </el-table-column>

      <el-table-column prop="sku.name" label="名称" width="200"></el-table-column>
      <el-table-column prop="sku.price" label="单价" width="100"></el-table-column>
      <el-table-column prop="quantity" label="数量" width="100"></el-table-column>


      <el-table-column label="小计" width="120">
        <template #default="scope">
          ¥{{ (scope.row.quantity * scope.row.sku.price).toFixed(2) }}
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格 -->


    <!-- 收货信息 -->
    <el-form :model="order" label-width="100px" style="margin-bottom: 20px;">
      <el-form-item label="收货人">
        <el-input v-model="order.receiverName"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="order.receiverPhone"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="order.receiverAddress"></el-input>
      </el-form-item>
    </el-form>

    <!-- 支付方式 -->
    <el-form label-width="100px" style="margin-bottom: 20px;">
      <el-form-item label="支付方式">
        <el-radio-group v-model="order.payment">
          <el-radio label="paypay">paypay</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>


    <!-- 总价和提交 -->
    <div class="checkout-footer">
      <div class="total">总计：¥{{ totalPrice.toFixed(2) }}</div>
      <el-button type="primary" @click="submitOrder">提交订单</el-button>
    </div>


  </div>
</template>

<script>
import cartApi from '@/api/cart/cart'
import productSkuApi from '@/api/product/product-sku'
import orderApi from '@/api/order/order'

export default {
  data() {
    return {
      cartList: [],
      order: {}
    }
  },
  computed: {
    totalPrice() {
      return this.cartList.reduce((sum, item) => sum + item.sku.price * item.quantity, 0)
    }
  },
  created() {
    this.loadCart()
  },
  methods: {
    async loadCart() {
      try {
        const res = await cartApi.selectAllChecked()
        const cartItemTOs = res.data.data.record || []

        const promises = cartItemTOs.map(async cartItem => {
          const sku = await productSkuApi.selectById(cartItem.skuId)
          cartItem.sku = sku.data.data.record
          return cartItem
        })
        this.cartList = await Promise.all(promises)
      } catch (err) {
        console.error('加载购物车失败', err)
        this.cartList = []
      }
    },


    submitOrder() {
      const skuPrice = []   // sku和价格对应，让后端确认

      this.cartList.forEach(item => {
        var string = item.sku.id + ":" + item.sku.price
        skuPrice.push(string)
      })


      orderApi.insert(this.order, skuPrice)
          .then(res => {
            if (res.data.success) {
              // 正常下单成功
              let id = res.data.data.record
              this.$router.push('/payment/' + id)
            } else {
              //  逻辑错误（商品价格变动）
              this.$message({
                type: 'error',
                message: res.data.message || '下单失败，请刷新后再试'
              })
            }
          })

    }


  }
}
</script>

<style scoped>
.cart-product-img {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.checkout-footer {
  margin-top: 20px;
  text-align: right;
  font-size: 16px;
}

.total-price {
  display: inline-block;
  margin-right: 20px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}
</style>
