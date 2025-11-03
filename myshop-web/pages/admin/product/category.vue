<template>
  <div class="app-container">

    <!-- 一级分类数据展示 -->
    <el-button type="text" @click="open()">1次カテゴリを追加</el-button>
    <ul>
      <li v-for="c1 in categorys" :key="c1.id">
        {{ c1.name }}
        <el-button type="text" size="mini" icon="el-icon-edit" @click="edit(c1.id)">編集</el-button>
        <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteById(c1.id)">削除</el-button>
        <el-button type="text" size="mini" @click="toggleCategory2(c1)">
          {{ c1.showChildren ? '2次カテゴリを非表示' : '2次カテゴリを表示' }}
        </el-button>
        <el-button type="text" size="mini" @click="open2(c1)">2次カテゴリを追加</el-button>

        <!-- 二级级分类数据展示 -->
        <ul v-if="c1.showChildren">
          <li v-for="c2 in c1.list" :key="c2.id">
            {{ c2.name }}
            <el-button type="text" size="mini" icon="el-icon-edit" @click="edit2(c2.id)">編集</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteById2(c2)">削除</el-button>
            <el-button type="text" size="mini" @click="toggleCategory3(c2)">
              {{ c2.showChildren ? '属性名を非表示' : '属性名を表示' }}
            </el-button>
            <el-button type="text" size="mini" @click="open3(c2)">属性名を追加</el-button>

            <!-- 属性名数据展示 -->
            <ul v-if="c2.showChildren">
              <li v-for="c3 in c2.list" :key="c3.id">
                {{ c3.name }}
                <el-button type="text" size="mini" icon="el-icon-edit" @click="edit3(c3.id)">編集</el-button>
                <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteById3(c3)">削除</el-button>
                <el-button type="text" size="mini" @click="toggleCategory4(c3)">
                  {{ c3.showChildren ? '属性値を非表示' : '属性値を表示' }}
                </el-button>
                <el-button type="text" size="mini" @click="open4(c3)">属性値を追加</el-button>

                <!-- 属性值数据展示 -->
                <ul v-if="c3.showChildren">
                  <li v-for="c4 in c3.list" :key="c4.id">
                    {{ c4.name }}
                    <el-button type="text" size="mini" icon="el-icon-edit" @click="edit4(c4.id)">編集</el-button>
                    <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteById4(c4)">削除</el-button>
                  </li>
                </ul>
                <!-- 属性值数据展示 -->

              </li>
            </ul>
            <!-- 属性名数据展示 -->


          </li>
        </ul>
        <!-- 二级分类数据展示 -->

      </li>
    </ul>
    <!-- 一级分类数据展示 -->



    <!-- 一级分类对话框 -->
    <el-dialog :close-on-click-modal="false" title="1次カテゴリ" :visible.sync="dialogFormVisible" @close="cancel()">
      <el-form>
        <el-form-item label="1次カテゴリ名" :label-width="formLabelWidth">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="並び替え" :label-width="formLabelWidth">
          <el-input v-model="category.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">キャンセル</el-button>
        <el-button type="primary" @click="save()">確定</el-button>
      </div>
    </el-dialog>
    <!-- 一级分类对话框 -->


    <!-- 2级分类对话框 -->
    <el-dialog title="2次カテゴリ" :close-on-click-modal="false" :visible.sync="dialogFormVisible2" @close="cancel2()">
      <el-form>
        <el-form-item label="2次カテゴリ名" :label-width="formLabelWidth">
          <el-input v-model="category2.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="並び替え" :label-width="formLabelWidth">
          <el-input v-model="category2.sort" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel2()">キャンセル</el-button>
        <el-button type="primary" @click="save2()">確定</el-button>
      </div>
    </el-dialog>
    <!-- 2级分类对话框 -->

    <!-- 3级属性名对话框 -->
    <el-dialog title="属性名" :close-on-click-modal="false" :visible.sync="dialogFormVisible3" @close="cancel3()">
      <el-form>
        <el-form-item label="属性名" :label-width="formLabelWidth">
          <el-input v-model="attr.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel3()">キャンセル</el-button>
        <el-button type="primary" @click="save3()">確定</el-button>
      </div>
    </el-dialog>
    <!-- 3级属性名对话框 -->

    <!-- 4级属性值对话框 -->
    <el-dialog title="属性値" :close-on-click-modal="false" :visible.sync="dialogFormVisible4" @close="cancel4()">
      <el-form>
        <el-form-item label=" 属性値" :label-width="formLabelWidth">
          <el-input v-model="attrValue.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel4()">キャンセル</el-button>
        <el-button type="primary" @click="save4()">確定</el-button>
      </div>
    </el-dialog>
    <!-- 4级属性值对话框 -->
  </div>
</template>


<script>
import categoryApi from "@/api/product/category"
import attrApi from "@/api/product/attr"
import attrValueApi from "@/api/product/attr-value"


export default {
  layout: 'admin', // 使用 admin 布局
  data() {
    return {
      categorys: [],

      category: {},
      category2: {},
      attr: {},
      attrValue: {},

      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogFormVisible3: false,
      dialogFormVisible4: false,

    }
  },
  created() {  //页面刚创建时调用，根据是否有id，判断是新增还是修改
    this.selectAllCategory()
  },

  watch: {
    $route(to, from) {  //路径发生变化时调用init方法
      this.selectAllCategory()
    }
  },

  methods: {

    // 查询所有一级Category
    selectAllCategory() {
      categoryApi.selectAll()
        .then((response) => {
          this.categorys = response.data.data.record
        })
        .catch((err) => {   // 没查到数据就会报错，就不会更新数据，这时要清空categorys
          this.categorys = []
        })
    },

    // 显示2级分类
    toggleCategory2(c1) {
      if (!c1.showChildren) {
        this.$set(c1, 'showChildren', true) // 新增属性要用 $set
        this.selectAllCategory2(c1.id) // 去后台查二级分类
      } else {
        c1.showChildren = false
      }
    },



    // 查询所有二级Category
    selectAllCategory2(pid) {
      categoryApi.selectAll2(pid)
        .then((response) => {
          this.categorys.forEach(c => {
            if (c.id == pid) {
              // 用 $set 才能触发 Vue 响应式刷新
              this.$set(c, 'list', response.data.data.record);
            }
          })
        })
        .catch((err) => {   // 没查到数据就会报错，就不会更新数据，这时要清空categorys
          this.categorys.forEach(c => {
            if (c.id == pid) {
              // 用 $set 才能触发 Vue 响应式刷新
              this.$set(c, 'list', []);
            }
          })
        })
    },

    // 显示3级分类（属性名）
    toggleCategory3(c2) {
      if (!c2.showChildren) {
        this.$set(c2, 'showChildren', true)
        this.selectAllAttr(c2.id)
      } else {
        c2.showChildren = false
      }
    },

    // 查三级分类（属性名）
    selectAllAttr(category2Id) {
      attrApi.selectAll(category2Id)
        .then(response => {
          this.categorys.forEach(c1 => {
            if (c1.list) {
              c1.list.forEach(c2 => {
                if (c2.id === category2Id) {
                  this.$set(c2, 'list', response.data.data.record)
                }
              })
            }
          })
        })
        .catch((err) => {   // 没查到数据就会报错，就不会更新数据，这时要清空categorys
          this.categorys.forEach(c1 => {
            if (c1.list) {
              c1.list.forEach(c2 => {
                if (c2.id === category2Id) {
                  this.$set(c2, 'list', [])
                }
              })
            }
          })
        })
    },


    // 显示4级分类（属性值）
    toggleCategory4(c3) {
      if (!c3.showChildren) {
        this.$set(c3, 'showChildren', true)
        this.selectAllAttrValue(c3.id)
      } else {
        c3.showChildren = false
      }
    },

    // 查四级分类（属性值）
    selectAllAttrValue(attrId) {
      attrValueApi.selectAll(attrId)
        .then(response => {
          this.categorys.forEach(c1 => {
            if (c1.list) {
              c1.list.forEach(c2 => {
                if (c2.list) {
                  c2.list.forEach(c3 => {
                    if (c3.id === attrId) {
                      this.$set(c3, 'list', response.data.data.record)
                    }
                  })
                }
              })
            }
          })
        })
        .catch((err) => {   // 没查到数据就会报错，就不会更新数据，这时要清空categorys
          this.categorys.forEach(c1 => {
            if (c1.list) {
              c1.list.forEach(c2 => {
                if (c2.list) {
                  c2.list.forEach(c3 => {
                    if (c3.id === attrId) {
                      this.$set(c3, 'list', [])
                    }
                  })
                }
              })
            }
          })
        })
    },




    // 根据id删除Category
    deleteById(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        categoryApi.deleteById(id)
          .then((response) => {
            this.selectAllCategory()
          })
      })
    },

    // 根据id删除Category2
    deleteById2(c2) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        categoryApi.deleteById(c2.id)
          .then((response) => {
            this.selectAllCategory2(c2.parentId)
          })
      })
    },

    // 根据id删除属性名 (第3级)
    deleteById3(c3) {
      this.$confirm('此操作将永久删除该属性名以及它下面的所有属性值, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        attrApi.deleteById(c3.id).then(() => {
          this.selectAllAttr(c3.categoryId) // 刷新第3级分类列表
        })
      })
    },

    // 根据id删除属性值 (第4级)
    deleteById4(c4) {
      this.$confirm('此操作将永久删除该属性值, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        attrValueApi.deleteById(c4.id).then(() => {
          this.selectAllAttrValue(c4.attrId) // 刷新第4级列表
        })
      })
    },



    // 打开添加一级Category页面
    open() {
      this.dialogFormVisible = true
      this.category.parentId = 0  // 一级分类，父分类默认设置0
    },

    // 打开添加2级Category页面
    open2(c) {
      this.dialogFormVisible2 = true
      this.category2.parentId = c.id // 2级分类父分类id
      c.showChildren = true
    },

    // 打开添加3级 属性名 页面
    open3(c) {
      this.dialogFormVisible3 = true
      this.attr = {}                // 清空旧数据
      this.attr.categoryId = c.id      // 设置所属的二级分类id
      c.showChildren = true
    },

    // 打开添加4级 属性值 页面
    open4(c) {
      this.dialogFormVisible4 = true
      this.attrValue = {}           // 清空旧数据
      this.attrValue.attrId = c.id // 设置所属的属性名id
      c.showChildren = true
    },




    // 打开修改一级Category页面，同时查询一个
    edit(id) {
      this.dialogFormVisible = true
      categoryApi.selectById(id)
        .then((response) => {
          this.category = response.data.data.record
        })
    },

    // 打开修改2级Category页面，同时查询一个
    edit2(id) {
      this.dialogFormVisible2 = true
      categoryApi.selectById(id)
        .then((response) => {
          this.category2 = response.data.data.record
        })
    },

    // 打开修改3级 属性名 页面，同时查询一个
    edit3(id) {
      this.dialogFormVisible3 = true
      attrApi.selectById(id)
        .then((response) => {
          this.attr = response.data.data.record
        })
    },

    // 打开修改4级 属性值 页面，同时查询一个
    edit4(id) {
      this.dialogFormVisible4 = true
      attrValueApi.selectById(id)
        .then((response) => {
          this.attrValue = response.data.data.record
        })
    },



    // 保存一级Category，根据是否有id，判断调用新增还是更新
    save() {
      if (!this.category.id) {  // 没有id则新增
        categoryApi.insert(this.category)
          .then((response) => {
            this.selectAllCategory()
            this.category = {}  // 清空
            this.dialogFormVisible = false
          })
      } else {  // 有id则更新
        categoryApi.update(this.category)
          .then((response) => {
            this.selectAllCategory()
            this.category = {}  // 清空
            this.dialogFormVisible = false
          })
      }
    },

    // 保存2级Category，根据是否有id，判断调用新增还是更新
    save2() {
      if (!this.category2.id) {  // 没有id则新增
        categoryApi.insert(this.category2)
          .then((response) => {
            this.selectAllCategory2(this.category2.parentId)
            this.category2 = {}  // 清空
            this.dialogFormVisible2 = false
          })
      } else {  // 有id则更新
        categoryApi.update(this.category2)
          .then((response) => {
            this.selectAllCategory2(this.category2.parentId)
            this.category2 = {}  // 清空
            this.dialogFormVisible2 = false
          })
      }
    },

    // 保存3级 属性名，根据是否有id判断新增还是更新
    save3() {
      if (!this.attr.id) {  // 没有id → 新增
        attrApi.insert(this.attr)
          .then((response) => {
            this.selectAllAttr(this.attr.categoryId) // 重新查询这个二级分类下的属性名
            this.attr = {}  // 清空
            this.dialogFormVisible3 = false
          })
      } else {  // 有id → 更新
        attrApi.update(this.attr)
          .then((response) => {
            this.selectAllAttr(this.attr.categoryId)
            this.attr = {}
            this.dialogFormVisible3 = false
          })
      }
    },

    // 保存4级 属性值，根据是否有id判断新增还是更新
    save4() {
      if (!this.attrValue.id) { // 没有id → 新增
        attrValueApi.insert(this.attrValue)
          .then((response) => {
            this.selectAllAttrValue(this.attrValue.attrId) // 更新属性值列表
            this.attrValue = {}
            this.dialogFormVisible4 = false
          })
      } else { // 有id → 更新
        attrValueApi.update(this.attrValue)
          .then((response) => {
            this.selectAllAttrValue(this.attrValue.attrId)
            this.attrValue = {}
            this.dialogFormVisible4 = false
          })
      }
    },



    // 关闭添加或修改一级Category页面
    cancel() {
      this.dialogFormVisible = false
      this.category = {}  // 清空
    },

    // 关闭添加或修改2级Category页面
    cancel2() {
      this.dialogFormVisible2 = false
      this.category2 = {}  // 清空
    },

    // 关闭3级属性名窗口
    cancel3() {
      this.dialogFormVisible3 = false
      this.attr = {} // 清空
    },

    // 关闭4级属性值窗口
    cancel4() {
      this.dialogFormVisible4 = false
      this.attrValue = {} // 清空
    },

  }
}
</script>


<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
