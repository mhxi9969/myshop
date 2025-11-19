<template>
  <div class="cart-page">
    <!-- 表格 -->
    <el-table :data="cartList" style="width: 100%">
      <!-- 勾选框列 -->
      <el-table-column label="選択" width="60" align="center">
        <template #default="scope">
          <el-checkbox v-model="scope.row.checked" :true-label="1" :false-label="0" @change="updateCart(scope.row)"></el-checkbox>
        </template>
      </el-table-column>

      <el-table-column label="商品画像" width="120">
        <template #default="scope">
          <img :src="scope.row.sku.picture" class="cart-product-img" />
        </template>
      </el-table-column>

      <el-table-column prop="sku.name" label="商品名" width="200"></el-table-column>
      <el-table-column prop="sku.price" label="単価" width="100"></el-table-column>

      <el-table-column label="数量" width="120">
        <template #default="scope">
          <el-input-number v-model="scope.row.quantity" :min="1" @change="updateCart(scope.row)" size="mini"
            style="width: 90px" />
        </template>
      </el-table-column>

      <el-table-column label="小計" width="120">
        <template #default="scope">
          ¥{{ (scope.row.quantity * scope.row.sku.price).toFixed(2) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" size="mini" @click="removeFromCart(scope.row)">削除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表格 -->



    <div class="cart-footer">
      <div class="total-price">
        合計：<span class="price">¥{{ totalPrice.toFixed(2) }}</span>
      </div>
      <el-button type="primary" @click="goToCheckout" :disabled="cartList.length === 0">
        レジへ進む
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

        // 对每个购物项，查找对应的sku详细信息展示
        const list = cartVos.map(async item => {
          const skuRes = await productSkuApi.selectById(item.skuId)
          item.sku = skuRes.data.data.record
          return item
        })
        // 等待完成后，赋值给购物车
        this.cartList = await Promise.all(list)
      } catch (err) {
        this.cartList = []
      }
    },

    // 更新
    async updateCart(item) {
      try {
        await cartApi.update({
          id: item.id,
          skuId: item.skuId,
          quantity: item.quantity,
          checked: item.checked,
        })
      } catch (err) {
      }
    },

    async removeFromCart(cart) {
      try {
        // 先调用后端削除
        await cartApi.deleteById(cart.skuId)
        // 更新响应式数组，Vue 能检测到
        this.cartList = this.cartList.filter(item => item.skuId !== cart.skuId)
      } catch (err) {
        console.error(err)
      }
    },

    // 去结算
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
