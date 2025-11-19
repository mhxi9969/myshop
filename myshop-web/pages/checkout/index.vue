<template>
  <div class="cart-page">
    <!-- 表格 -->
    <el-table :data="cartList" style="width: 100%">

      <el-table-column label="商品画像" width="120">
        <template #default="scope">
          <img :src="scope.row.sku.picture" class="cart-product-img"/>
        </template>
      </el-table-column>

      <el-table-column prop="sku.name" label="商品名" width="200"></el-table-column>
      <el-table-column prop="sku.price" label="単価" width="100"></el-table-column>
      <el-table-column prop="quantity" label="数量" width="100"></el-table-column>


      <el-table-column label="小計" width="120">
        <template #default="scope">
          ¥{{ (scope.row.quantity * scope.row.sku.price).toFixed(2) }}
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格 -->


    <!-- 收货信息 -->
    <el-form :model="order" label-width="100px" style="margin-bottom: 20px;">
      <el-form-item label="お届け先名">
        <el-input v-model="order.receiverName"></el-input>
      </el-form-item>
      <el-form-item label="電話番号">
        <el-input v-model="order.receiverPhone"></el-input>
      </el-form-item>
      <el-form-item label="住所">
        <el-input v-model="order.receiverAddress"></el-input>
      </el-form-item>
    </el-form>

    <!-- 支付方式 -->
    <el-form label-width="100px" style="margin-bottom: 20px;">
      <el-form-item label="支払方法">
        <el-radio-group v-model="order.payment">
          <el-radio label="paypay">paypay</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>


    <!-- 总价和提交 -->
    <div class="checkout-footer">
      <div class="total">合計：¥{{ totalPrice.toFixed(2) }}</div>
      <el-button type="primary" @click="submitOrder">注文を確定する</el-button>
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
      order: {},
      token: ""
    }
  },
  computed: {
    totalPrice() {
      return this.cartList.reduce((sum, item) => sum + item.sku.price * item.quantity, 0)
    }
  },
  created() {
    this.loadCart()
    this.getToken()
  },
  methods: {
    async loadCart() {
      try {
        const res = await cartApi.selectAllChecked()  //查找购物车里面选中的购物项
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
      const skuPrice = []   // 发送当前显示sku价格，让后端确认，防止后端价格变化，前端不知道

      this.cartList.forEach(item => {
        var string = item.sku.id + ":" + item.sku.price
        skuPrice.push(string)
      })

      orderApi.insert(this.order, skuPrice, this.token)
          .then(res => {
            // 成功
            if (res.data.success) {
              let id = res.data.data.record
              this.$router.push('/payment/' + id)
              // 失败，则显示提示信息
            } else {
              this.$message({
                type: 'error',
                message: res.data.message
              })
            }
          })

    },

    // 进入结算页时，拿到token，下单时带上，防止重复下单
    getToken() {
      orderApi.getOrderToken().then(res => {
        this.token = res.data.data.record
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
