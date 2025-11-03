<template>
  <div class="order-detail-page">
    <h2>订单详情</h2>

    <!-- 订单基本信息 -->
    <el-card class="order-info">
      <p><strong>订单号：</strong>{{ order.id }}</p>
      <p><strong>下单时间：</strong>{{ order.createTime }}</p>
      <p><strong>订单状态：</strong>{{ order.status | statusFilter }}</p>
      <p><strong>总金额：</strong>¥{{ order.totalAmount }}</p>
    </el-card>

    <h3 style="margin-top: 20px;">订单商品列表</h3>

    <!-- 子订单商品列表 -->
    <el-table :data="orderItems" border>
      <el-table-column label="商品图片" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.skuPicture" class="item-img" />
        </template>
      </el-table-column>
      <el-table-column prop="skuName" label="商品名称" />
      <el-table-column prop="skuPrice" label="单价" />
      <el-table-column prop="skuQuantity" label="数量" />
      <el-table-column label="小计">
        <template slot-scope="scope">
          ¥{{ (scope.row.skuPrice * scope.row.skuQuantity).toFixed(2) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import orderApi from "@/api/order/order";
import orderItemApi from "@/api/order/order-item";

export default {
  middleware: 'userauth',
  data() {
    return {
      order: {},
      orderItems: []
    }
  },
  created() {
    const id = this.$route.params.id
    this.getOrder(id)
    this.getOrderItems(id)
  },
  methods: {
    getOrder(id) {
      orderApi.selectById(id).then(res => {
        this.order = res.data.data.record;
      })
    },

    getOrderItems(id) {
      orderItemApi.selectAll(id).then(res => {
        this.orderItems = res.data.data.record;
      })
    }

  },
  filters: {
    payTypeFilter(type) {
      const map = { 1: '微信支付', 2: '支付宝', 3: 'PayPay' }
      return map[type] || '未支付'
    },
    statusFilter(status) {
      const map = { 0: '未支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消' }
      return map[status] || '未知状态'
    }
  }
}
</script>

<style scoped>
.order-detail-page {
  max-width: 1000px;
  margin: 20px auto;
}

.item-img {
  width: 80px;
  border-radius: 4px;
}
</style>