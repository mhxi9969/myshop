<template>
  <div class="payment-page">
    <h2>支付页面</h2>

    <!-- 订单信息 -->
    <el-card class="order-info" shadow="always">

      <h3>请支付</h3>
      <a :href="order.tradeId" target="_blank" class="pay-link">
        点击前往paypay支付
      </a>

      <h3>订单详情</h3>
      <el-table :data="orderItems" style="width: 100%; margin-bottom: 10px;">

        <el-table-column label="" width="120">
          <template #default="scope">
            <img :src="scope.row.skuPicture" class="cart-product-img" />
          </template>
        </el-table-column>


        <el-table-column label="商品名称">
          <template slot-scope="scope">
            {{ scope.row.skuName }}
          </template>
        </el-table-column>
        <el-table-column label="单价">
          <template slot-scope="scope">
            ¥{{ scope.row.skuPrice }}
          </template>
        </el-table-column>
        <el-table-column label="数量">
          <template slot-scope="scope">
            {{ scope.row.skuQuantity }}
          </template>
        </el-table-column>
        <el-table-column label="小计">
          <template slot-scope="scope">
            ¥{{ scope.row.totalPrice }}
          </template>
        </el-table-column>
      </el-table>

      <div class="total-price">
        总计：<span class="price">¥{{ order.totalAmount }}</span>
      </div>

      <h3>收货信息</h3>
      <p>姓名：{{ order.receiverName }}</p>
      <p>电话：{{ order.receiverPhone }}</p>
      <p>地址：{{ order.receiverAddress }}</p>




    </el-card>
  </div>
</template>

<script>
import orderApi from '@/api/order/order'
import orderItemApi from '@/api/order/order-item'

export default {
  data() {
    return {
      order: {},
      orderItems: [],
    }
  },
  created() {
    const id = this.$route.params.id

    this.selectById(id)
    this.selectItemsById(id)
    this.startPolling();

  },
  methods: {
    selectById(id) {
      orderApi.selectById(id)
        .then(res => {
          this.order = res.data.data.record
        })
    },

    selectItemsById(id) {
      orderItemApi.selectAll(id)
        .then(res => {
          this.orderItems = res.data.data.record
        })
    },

    startPolling() {
      this.timer = setInterval(() => {
        orderApi.poll(this.order.id).then(res => {
          const s = res.data.data.record
          console.log(s)
          if (s === 'success') {
            clearInterval(this.timer)  // 停止轮询
            this.$message.success('支付成功！')
            this.$router.push('/myorder')
          }
        }).catch(err => {
          console.error('轮询失败:', err)
        })
      }, 3000) // 每3秒轮询一次
    }



  }
}
</script>

<style>
.cart-product-img {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.total-price {
  text-align: right;
  font-weight: bold;
  margin-top: 15px;
  font-size: 18px;
}

.pay-link {
  text-decoration: none;   /* 去除下划线 */
  color: #409eff;          /* Element Plus 主色调蓝 */
  font-weight: 500;
}

.pay-link:hover {
  color: #66b1ff;          /* 悬停时变亮 */
  text-decoration: none;   /* 确保悬停也没有下划线 */
}
</style>