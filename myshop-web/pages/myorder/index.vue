<template>
  <div class="my-orders-page">
    <h2>マイ注文</h2>

    <el-table :data="orders" border style="width: 100%">
      <!-- 订单号 -->
      <el-table-column prop="id" label="注文番号" width="200">
      </el-table-column>

      <!-- 状态 -->
      <el-table-column label="状態" width="120">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)">
            {{ statusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 总价 -->
      <el-table-column label="合計" width="120">
        <template slot-scope="scope">
          ¥{{ scope.row.totalAmount.toFixed(2) }}
        </template>
      </el-table-column>

      <!-- 下单时间 -->
      <el-table-column prop="createTime" label="注文日時" width="180">
      </el-table-column>

      <!-- 操作 -->
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="viewOrder(scope.row.id)">詳細を見る</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import orderApi from "@/api/order/order";

export default {
  middleware: 'user-auth',
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
          return '未払い'
        case 1:
          return '支払済み'
        case 2:
          return 'キャンセル済み'
        default:
          return '不明な状態'
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
  