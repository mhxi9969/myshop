<template>
  <div class="cart-page">
    <!-- 表格 -->
    <el-table :data="cartList" style="width: 100%">
      <!-- 勾选框列 -->
      <el-table-column label="选择" width="60" align="center">
        <template #default="scope">
          <el-checkbox v-model="scope.row.checked" :true-label="1" :false-label="0" @change="updateCart(scope.row)"></el-checkbox>
        </template>
      </el-table-column>

      <el-table-column label="商品" width="120">
        <template #default="scope">
          <img :src="scope.row.sku.picture" class="cart-product-img" />
        </template>
      </el-table-column>

      <el-table-column prop="sku.name" label="名称" width="200"></el-table-column>
      <el-table-column prop="sku.price" label="单价" width="100"></el-table-column>

      <el-table-column label="数量" width="120">
        <template #default="scope">
          <el-input-number v-model="scope.row.quantity" :min="1" @change="updateCart(scope.row)" size="mini"
            style="width: 90px" />
        </template>
      </el-table-column>

      <el-table-column label="小计" width="120">
        <template #default="scope">
          ¥{{ (scope.row.quantity * scope.row.sku.price).toFixed(2) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" size="mini" @click="removeFromCart(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格 -->



    <div class="cart-footer">
      <div class="total-price">
        总价：<span class="price">¥{{ totalPrice.toFixed(2) }}</span>
      </div>
      <el-button type="primary" @click="goToCheckout" :disabled="cartList.length === 0">
        去结算
      </el-button>
    </div>


  </div>
</template>

<script>
import cartApi from '@/api/cart/cart'
import productSkuApi from '@/api/product/product-sku'

export default {
  middleware: 'user-auth',
  data() {
    return {
      cartList: []
    }
  },
  created() {
    this.loadCart()
  },
  computed: {
    totalPrice() {
      return this.cartList.reduce((sum, item) => sum + item.sku.price * item.quantity, 0)
    }
  },
  methods: {
    async loadCart() {
      try {
        const res = await cartApi.selectAll()
        const cartVos = res.data.data.record || []

        const promises = cartVos.map(async cart => {
          const skuRes = await productSkuApi.selectById(cart.skuId)
          cart.sku = skuRes.data.data.record
          return cart
        })
        this.cartList = await Promise.all(promises)
      } catch (err) {
        console.error('加载购物车失败', err)
        this.cartList = []
      }
    },

    async updateCart(cart) {
      try {
        await cartApi.update({
          id: cart.id,
          skuId: cart.skuId,
          quantity: cart.quantity,
          checked: cart.checked,
        })
      } catch (err) {

      }
    },

    async removeFromCart(cart) {
      try {
        // 先调用后端删除
        await cartApi.deleteById(cart.skuId)

        // 更新响应式数组，Vue 能检测到
        this.cartList = this.cartList.filter(item => item.skuId !== cart.skuId)

        this.$message.success('删除成功')
      } catch (err) {
        this.$message.error('删除失败')
        console.error(err)
      }
    },

    goToCheckout() {
      this.$router.push('/checkout')  // 跳转到结算页
    },
  }
}
</script>

<style scoped>
.cart-product-img {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.cart-footer {
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
