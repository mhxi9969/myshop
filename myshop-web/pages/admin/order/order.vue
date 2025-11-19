<template>
  <div class="app-container">
    <!-- 查询条件表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="queryCondition.id" placeholder="user id"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="selectByCondition()">検索</el-button>
        <el-button type="default" @click="resetData()">リセット</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询条件表单 -->


    <!-- 数据展示 -->
    <el-table :data="orders" border style="width: 100%">
      <!-- 订单号 -->
      <el-table-column prop="id" label="注文番号" width="200">
      </el-table-column>

      <!-- user id -->
      <el-table-column prop="userId" label="user id" width="200">
      </el-table-column>

      <!-- 状态 -->
      <el-table-column label="状態" width="140">
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
          <el-button size="mini" type="primary" @click="edit(scope.row.id)">編集</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 数据展示 -->


    <!-- 分页条 -->
    <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page.sync="pageNum"
        style="padding: 30px 0; text-align: center;"
        @current-change="selectByCondition"
    >
    </el-pagination>
    <!-- 分页条 -->


    <!-- 对话框 -->
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible" title="SPU" @close="cancel()">
      <el-form>
        <el-form-item :label-width="formLabelWidth" label="SPU名">
          <el-input v-model="order.id" autocomplete="off"/>
        </el-form-item>


        <el-form-item :label-width="formLabelWidth" label="状態">
          <el-select v-model="order.status" placeholder="状態を選択してください">
            <el-option :value="0" label="未払い"></el-option>
            <el-option :value="1" label="支払済み"></el-option>
            <el-option :value="2" label="キャンセル済み"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">キャンセル</el-button>
        <el-button type="primary" @click="save()">確定</el-button>
      </div>
    </el-dialog>
    <!-- spu对话框 -->


  </div>
</template>


<script>
import orderApi from '@/api/order/order'

export default {
  layout: 'admin', // 使用 admin 布局

  data() {
    return {
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

      queryCondition: {},

      orders: [],
      pageNum: 0, // 当前页
      pageSize: 0, // 每页记录数
      total: 0, // 总记录数

      order: {},
      dialogFormVisible: false,
    }
  },

  created() {
    this.selectByCondition()
  },

  methods: {
    // 条件查询带分页
    selectByCondition(current = 1) { // 查询，current = 1是默认页数
      this.pageNum = current
      orderApi.selectByCondition(this.pageNum, this.queryCondition)
          .then((response) => {
            this.orders = response.data.data.record.list // record是分页对象
            this.pageNum = response.data.data.record.pageNum // 把数据赋值，页面自动展示页码
            this.pageSize = response.data.data.record.pageSize
            this.total = parseInt(response.data.data.record.total) // 要从字符串转成数字类型
          })
          .catch(err => { // 没查到数据就会报错，就不会更新数据，这时要清空brands
            this.orders = []
          })
    },

    // 重置查询
    resetData() {
      this.queryCondition = {} // 把查询条件清空
      this.selectByCondition()
    },


    // 打开修改页面，同时查询一个
    edit(id) {
      this.dialogFormVisible = true
      orderApi.selectById(id)
          .then((response) => {
            this.order = response.data.data.record
          })
    },

    save() {
      orderApi.update(this.order)
          .then((response) => {
            this.selectByCondition(this.pageNum)
            this.order = {} // 清空
            this.dialogFormVisible = false
          })

    },

    // 关闭添加或修改页面
    cancel() {
      this.dialogFormVisible = false
      this.order = {} // 清空
    }
  }
}
</script>
