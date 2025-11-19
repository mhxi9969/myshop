<template>
  <div class="app-container">

    <!-- 查询条件表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="queryCondition.name" placeholder="SPU名"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="selectByCondition()">検索</el-button>
        <el-button type="default" @click="resetData()">リセット</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询条件表单 -->


    <!-- SPU数据展示 -->
    <el-button type="success" @click="open()">SPU追加</el-button>

    <el-table :data="spus" style="width: 100%" @expand-change="handleExpandChange">

      <!-- 展开SKU数据 -->
      <el-table-column type="expand">
        <template slot-scope="props">
          <div>
            <!-- 加载中 -->
            <div v-if="props.row.loading" style="text-align:center; padding:10px;">...</div>

            <!-- SKU 表格 -->
            <el-table v-else-if="props.row.skuList && props.row.skuList.length > 0" :data="props.row.skuList"
                      size="mini">
              <el-table-column prop="id" label="SKU id"/>
              <el-table-column prop="name" label="SKU名"/>
              <el-table-column label="SKU画像">
                <template slot-scope="scope">
                  <img :src="scope.row.picture" width="40" height="40" class="head_pic">
                </template>
              </el-table-column>
              <el-table-column prop="price" label="SKU価格"/>
              <el-table-column prop="stock" label="SKU在庫"/>

              <!-- 动态生成属性列，这个不好写，要注意-->
              <!--props.row.skuList[0]表示第一个sku，从中取出属性名，生成栏-->
              <el-table-column v-for="(attr, index) in props.row.skuList[0].attrTOs" :key="index" :label="attr.name">
                <!--scope.row.attrTOs表示对于一个sku，遍历取出每一个属性值-->
                <template slot-scope="scope">{{ scope.row.attrTOs[index].skuValueName }}</template>
              </el-table-column>
              <!-- 动态生成属性列 -->

              <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                  <el-button type="primary" size="mini" icon="el-icon-edit" @click="edit2(scope.row.id)">編集
                  </el-button>
                  <el-button
                      type="danger"
                      size="mini"
                      icon="el-icon-delete"
                      @click="deleteById2(scope.row.id, scope.row.spuId)">削除
                  </el-button>
                </template>
              </el-table-column>

            </el-table>

            <el-button type="success" size="mini" style="margin-top: 8px" @click="open2(props.row)">
              SKU追加
            </el-button>
          </div>
        </template>
      </el-table-column>
      <!-- 展开SKU数据 -->


      <el-table-column prop="id" label="SPU id" width="180"/>
      <el-table-column prop="name" label="SPU名" width="180"/>
      <el-table-column :formatter="formatBrandName" prop="brandId" label="ブランド" width="180"/>
      <el-table-column :formatter="formatcategory1Name" prop="category1Id" label="1次カテゴリ" width="180"/>
      <el-table-column :formatter="formatcategory2Name" prop="category2Id" label="2次カテゴリ" width="180"/>
      <el-table-column label="状態" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 1" style="color: green">販売中</span>
          <span v-else style="color: red">販売停止</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="edit(scope.row.id)">編集</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteById(scope.row.id)">削除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <!-- SPU数据展示 -->


    <!-- 分页条 -->
    <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page.sync="pageNum"
        style="padding: 30px 0; text-align: center;"
        @current-change="selectByCondition"
    ></el-pagination>
    <!-- 分页条 -->


    <!-- spu对话框 -->
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible" title="SPU" @close="cancel()">
      <el-form>
        <el-form-item :label-width="formLabelWidth" label="SPU名">
          <el-input v-model="spu.name" autocomplete="off"/>
        </el-form-item>

        <!-- 选择品牌 -->
        <el-form-item :label-width="formLabelWidth" label="ブランド名">
          <el-select v-model="spu.brandId">
            <!-- value是提交的值，是品牌id  label是展示的值，是品牌名-->
            <el-option v-for="b in brands" :key="b.id" :value="b.id" :label="b.name"/>
          </el-select>
        </el-form-item>

        <!-- 选择一级分类 -->
        <el-form-item :label-width="formLabelWidth" label="1次カテゴリ">
          <el-select v-model="spu.category1Id" @change="handleCategory1Change"> <!-- 不要加括号 -->
            <el-option v-for="c1 in category1s" :key="c1.id" :value="c1.id" :label="c1.name"/>
          </el-select>
        </el-form-item>

        <!-- 选择二级分类 -->
        <el-form-item :label-width="formLabelWidth" label="2カテゴリ">
          <el-select v-model="spu.category2Id">
            <el-option v-for="c2 in category2sNow" :key="c2.id" :value="c2.id" :label="c2.name"/>
          </el-select>
        </el-form-item>

        <!-- 改变spu状态，新增spu时不显示 -->
        <el-form-item v-if="spu.id" :label-width="formLabelWidth" label="状態">
          <el-select v-model="spu.status" placeholder="状態を選択してください">
            <el-option :value="1" label="販売中"></el-option>
            <el-option :value="0" label="販売停止"></el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">キャンセル</el-button>
        <el-button type="primary" @click="save()">確定</el-button>
      </div>
    </el-dialog>
    <!-- spu对话框 -->


    <!-- sku对话框 -->
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible2" title="SKU" @close="cancel2()">
      <el-form>
        <el-form-item :label-width="formLabelWidth" label="SKU名">
          <el-input v-model="sku.name" autocomplete="off"/>
        </el-form-item>

        <el-form-item :label-width="formLabelWidth" label="SKU画像">
          <UploadImage v-model="sku.picture"/>
        </el-form-item>

        <el-form-item :label-width="formLabelWidth" label="SKU価格">
          <el-input v-model="sku.price" autocomplete="off"/>
        </el-form-item>

        <el-form-item :label-width="formLabelWidth" label="SKU在庫">
          <el-input v-model="sku.stock" autocomplete="off"/>
        </el-form-item>

        <!--动态渲染属性-->
        <el-form-item
            v-for="attr in sku.attrTOs"
            :key="attr.id"
            :label="attr.name"
            :label-width="formLabelWidth"
        >
          <el-select v-model="attr.skuValueId" placeholder="请选择">
            <el-option
                v-for="value in attr.attrValueTOs"
                :key="value.id"
                :label="value.name"
                :value="value.id"
            />
          </el-select>
        </el-form-item>
        <!--动态渲染属性-->
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel2()">キャンセル</el-button>
        <el-button type="primary" @click="save2()">確定</el-button>
      </div>
    </el-dialog>
    <!-- sku对话框 -->

  </div>
</template>


<script>
import brandApi from '@/api/product/brand'
import categoryApi from '@/api/product/category'
import productSpuApi from '@/api/product/product-spu'
import productSkuApi from '@/api/product/product-sku'
import attrApi from '@/api/product/attr'
import UploadImage from "@/components/upload-image.vue";


export default {
  components: {UploadImage: UploadImage},

  layout: 'admin', // 使用 admin 布局

  data() {
    return {

      queryCondition: {},

      spus: [],
      pageNum: 0, // 当前页
      pageSize: 0, // 每页记录数
      total: 0, // 总记录数

      spu: {status: 0},  //新增或修改的spu对象
      dialogFormVisible: false,

      brands: [],  // 备选的品牌
      category1s: [], // 备选的1级分类
      category2s: [], // 备选的所有2级分类
      category2sNow: [],  //备选1级分类下的2级分类

      sku: {  //新增或修改的sku对象
        attrTOs: []
      },
      dialogFormVisible2: false,
    }
  },

  created() {
    this.selectByCondition()
    this.selectAllBrands()
    this.selectAllcategory1s()
    this.selectAllcategory2s()
  },

  methods: {
    // 条件查询带分页
    selectByCondition(current = 1) { // 查询，current = 1是默认页数
      this.pageNum = current
      productSpuApi.selectByCondition(this.pageNum, this.queryCondition)
          .then((response) => {
            this.spus = response.data.data.record.list // record是分页对象

            // 初始化空数组，否则第一次展开sku时无法响应
            this.spus.forEach(item => {
              this.$set(item, 'skuList', [])
              this.$set(item, 'loading', false)
            })

            this.pageNum = response.data.data.record.pageNum // 把数据赋值，页面自动展示页码
            this.pageSize = response.data.data.record.pageSize
            this.total = parseInt(response.data.data.record.total) // 要转成数字类型
          })
          .catch((err) => { // 没查到数据就会报错，就不会更新数据，这时要清空
            this.spus = []
          })
    },

    // 重置查询
    resetData() {
      this.queryCondition = {} // 把查询条件清空
      this.selectByCondition()
    },

    // 展开spu的子sku，同时查询sku
    handleExpandChange(row) {
      this.selectAllSku(row.id)
    },

    // 根据spu id查询所有sku
    selectAllSku(pid) {
      productSkuApi.selectAllTreeBySpuId(pid)
          .then((response) => {
            this.spus.forEach(c => {
              if (c.id == pid) {
                // 用 $set 才能触发 Vue 响应式刷新
                this.$set(c, 'skuList', response.data.data.record)
              }
            })
          })
          .catch((err) => { // 没查到数据就会报错，就不会更新数据，这时要清空spus的skuList
            this.spus.forEach(c => {
              if (c.id == pid) {
                // 用 $set 才能触发 Vue 响应式刷新
                this.$set(c, 'skuList', [])
              }
            })
          })
    },

    // 删除一个spu
    deleteById(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(() => {
            productSpuApi.deleteById(id)
                .then((response) => {
                  this.selectByCondition() // 删除后再次查询
                })
          })
    },

    // 删除一个sku
    deleteById2(id, pid) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        productSkuApi.deleteById(id)
            .then((response) => {
              this.selectAllSku(pid) // 删除后再次查询
            })
      })
    },

    // 打开添加spu页面
    open() {
      this.dialogFormVisible = true
      this.spu.status = 0
    },

    // 打开添加sku页面
    open2(spu) {
      this.dialogFormVisible2 = true
      this.sku.spuId = spu.id
      attrApi.selectAllTree(spu.category2Id)
          .then((response) => {
            this.sku.attrTOs = response.data.data.record
          })
    },

    // 打开修改spu页面，同时查询一个
    edit(id) {
      this.dialogFormVisible = true
      productSpuApi.selectById(id)
          .then((response) => {
            this.spu = response.data.data.record
          })
    },

    // 打开修改sku页面，同时查询一个
    edit2(id) {
      this.dialogFormVisible2 = true
      productSkuApi.selectByIdTree(id)
          .then((response) => {
            this.sku = response.data.data.record
          })
    },

    // 保存spu，根据是否有id，判断调用新增还是更新
    save() {
      if (!this.spu.id) { // 没有id则新增
        productSpuApi.insert(this.spu)
            .then((response) => {
              this.selectByCondition()
              this.spu = {} // 清空
              this.dialogFormVisible = false
            })
      } else { // 有id则更新
        productSpuApi.update(this.spu)
            .then((response) => {
              this.selectByCondition()
              this.spu = {} // 清空
              this.dialogFormVisible = false
            })
      }
    },

    // 保存sku，根据是否有id，判断调用新增还是更新
    save2() {
      if (!this.sku.id) { // 没有id则新增
        productSkuApi.insert(this.sku)
            .then((response) => {
              this.selectAllSku(this.sku.spuId)
              this.sku = {attrTOs: []} // 清空
              this.dialogFormVisible2 = false
            })
      } else { // 有id则更新
        productSkuApi.update(this.sku)
            .then((response) => {
              this.selectAllSku(this.sku.spuId)
              this.sku = {attrTOs: []}// 清空
              this.dialogFormVisible2 = false
            })
      }
    },

    // 关闭添加或修改spu页面
    cancel() {
      this.dialogFormVisible = false
      this.spu = {} // 清空
    },

    // 关闭添加或修改sku页面
    cancel2() {
      this.dialogFormVisible2 = false
      this.sku = {attrTOs: []}  // 清空，注意要保留数组
    },


    // 查询所有品牌
    selectAllBrands() {
      brandApi.selectAll()
          .then((response) => {
            this.brands = response.data.data.record
          })
    },

    // 根据品牌id，显示品牌名
    formatBrandName(row, column, cellValue) {
      // 找到brand对象，它的id等于cellValue
      const brand = this.brands.find(b => b.id === cellValue)
      // 返回brand的名字
      return brand ? brand.name : '未知品牌'
    },

    // 查询所有1次カテゴリ
    selectAllcategory1s() {
      categoryApi.selectAll()
          .then((response) => {
            this.category1s = response.data.data.record
          })
    },

    // 根据1次カテゴリid，显示1次カテゴリ名
    formatcategory1Name(row, column, cellValue) {
      // 找到category1对象，它的id等于cellValue
      const category1 = this.category1s.find(b => b.id === cellValue)
      // 返回category1的名字
      return category1 ? category1.name : '未知1次カテゴリ'
    },

    // 查询所有2级分类，为了根据2级分类id，显示2级分类名字用
    selectAllcategory2s() {
      categoryApi.all2()
          .then((response) => {
            this.category2s = response.data.data.record
          })
    },

    // 根据2级分类id，显示2级分类名
    formatcategory2Name(row, column, cellValue) {
      // 找到category2对象，它的id等于cellValue
      const category2 = this.category2s.find(b => b.id === cellValue)
      // 返回category2的名字
      return category2 ? category2.name : '未知2カテゴリ'
    },

    // 根据1级分类id，查询2级分类
    handleCategory1Change(id) {
      // 强制清空2カテゴリ值
      this.$set(this.spu, 'category2Id', null)

      categoryApi.selectAll2(id)
          .then((response) => {
            this.category2sNow = response.data.data.record
          })
    }

  }
}
</script>
