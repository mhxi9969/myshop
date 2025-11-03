<template>
  <div class="app-container">
    <!-- 查询条件表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="queryCondition.name" placeholder="品牌名字"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="selectByCondition()">查询</el-button>
        <el-button type="default" @click="resetData()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询条件表单 -->

    <!-- 数据展示 -->
    <el-button type="success" @click="open()">添加品牌</el-button>

    <el-table :data="brands" style="width: 100%">
      <el-table-column prop="id" label="品牌id" width="180"/>

      <el-table-column prop="name" label="品牌名" width="180"/>

      <el-table-column label="图片" width="180">
        <template slot-scope="scope"> <!-- scope表示表格对象 -->
          <img :src="scope.row.picture" width="40" height="40" class="head_pic" >
        </template>
      </el-table-column>

      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="edit(scope.row.id)">修改</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 数据展示 -->

    <!-- 分页条 -->
    <el-pagination
      :current-page="pageNum"
      :page-size="pageSize"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="selectByCondition" /> <!-- 分页插件，查询方法不要加括号 -->
    <!-- 分页条 -->

    <!-- 对话框 -->
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible" title="品牌" @close="cancel()">
      <el-form ref="brandForm" :model="brand" :rules="rules">
        <el-form-item :label-width="formLabelWidth" label="品牌名" prop="name">
          <el-input v-model="brand.name" autocomplete="off"/>
        </el-form-item>

        <el-form-item :label-width="formLabelWidth" label="品牌图片" prop="picture">
          <UploadImage v-model="brand.picture" />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 对话框 -->

  </div>
</template>

<script>
import brandApi from '@/api/product/brand'
import UploadImage from '@/components/upload-image.vue'


export default {
  layout: 'admin', // 使用 admin 布局
  components: { UploadImage: UploadImage },  //使用图片上传组件

  data() {
    return {
      queryCondition: {},

      brands: [],
      pageNum: 0, // 当前页
      pageSize: 0, // 每页记录数
      total: 0, // 总记录数

      brand: {
        name: '',
        picture: ''
      },
      dialogFormVisible: false,

      rules: { // 校验规则
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
    }
  },

  created() {
    this.selectByCondition()
  },
  watch: {
    brand: {
      handler(newVal) {
        console.log('brand object updated:', newVal)
      },
      deep: true
    }
  },
  methods: {
    // 条件查询带分页
    selectByCondition(current = 1) { // 查询，current = 1是默认页数
      this.pageNum = current
      brandApi.selectByCondition(this.pageNum, this.queryCondition)
        .then((response) => {
          this.brands = response.data.data.record.list // record是分页对象
          this.pageNum = response.data.data.record.pageNum // 把数据赋值，页面自动展示页码
          this.pageSize = response.data.data.record.pageSize
          this.total = parseInt(response.data.data.record.total) // 要转成数字类型
        })
        .catch(err => { // 没查到数据就会报错，就不会更新数据，这时要清空brands
          console.log(err)
          this.brands = []
        })
    },

    // 重置查询
    resetData() {
      this.queryCondition = {} // 把查询条件清空
      this.selectByCondition()
    },

    // 删除一个
    deleteById(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        brandApi.deleteById(id)
          .then((response) => {
            this.selectByCondition() // 删除后再次查询
          })
      })
    },

    // 打开添加页面
    open() {
      this.dialogFormVisible = true
    },

    // 打开修改页面，同时查询一个
    edit(id) {
      this.dialogFormVisible = true
      brandApi.selectById(id)
        .then((response) => {
          this.brand = response.data.data.record
        })
    },

    // 保存brand，根据是否有id，判断调用新增还是更新
    save() {
      // 校验数据
      this.$refs.brandForm.validate((valid) => {
        if (!valid) {
          return // 校验失败，阻止提交
        }

        if (!this.brand.id) { // 没有id则新增
          brandApi.insert(this.brand)
            .then((response) => {
              this.selectByCondition()
              this.brand = {} // 清空
              this.dialogFormVisible = false
            })
        } else { // 有id则更新
          brandApi.update(this.brand)
            .then((response) => {
              this.selectByCondition()
              this.brand = {} // 清空
              this.dialogFormVisible = false
            })
        }
      })
    },

    // 关闭添加或修改页面
    cancel() {
      this.dialogFormVisible = false
      this.brand = {} // 清空
    },


  }
}
</script>
