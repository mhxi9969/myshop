<template>
  <div class="my-orders-page">
    <h2>我的订单</h2>

    <el-table :data="orders" border style="width: 100%">
      <!-- 订单号 -->
      <el-table-column prop="id" label="订单号" width="200">
      </el-table-column>

      <!-- 状态 -->
      <el-table-column label="状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)">
            {{ statusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 总价 -->
      <el-table-column label="总价" width="120">
        <template slot-scope="scope">
          ¥{{ scope.row.totalAmount.toFixed(2) }}
        </template>
      </el-table-column>

      <!-- 下单时间 -->
      <el-table-column prop="createTime" label="下单时间" width="180">
      </el-table-column>

      <!-- 操作 -->
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="viewOrder(scope.row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import orderApi from "@/api/order/order";

export default {
  middleware: 'userauth',
  data() {
    return {
      orders: [],
    }
  },
  created() {
    this.selectAll()
  },
  methods: {
    statusText(status) {
      switch (status) {
        case 0:
          return '待支付'
        case 1:
          return '已支付'
        case 2:
          return '已取消'
        default:
          return '未知'
      }
    },
    statusType(status) {
      switch (status) {
        case 0:
          return 'warning'
        case 1:
          return 'success'
        case 2:
          return 'info'
        default:
          return 'default'
      }
    },
    viewOrder(id) {
      this.$router.push(`/myorder/${id}`)
    },
    selectAll() {
      orderApi.selectAll().then(res => {
        this.orders = res.data.data.record
      })
    }

  }
}
</script>

<style scoped>
.my-orders-page {
  padding: 20px;
}
</style>
  