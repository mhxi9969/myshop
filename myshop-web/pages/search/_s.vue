<template>
  <div class="product-search-page">

    <!-- 动态属性筛选 -->
    <div class="filter-bar" v-if="filterAttrs.length">
      <!-- 生成多个属性名 -->
      <div class="filter-item" v-for="attr in filterAttrs" :key="attr.name">
        <span class="filter-name">{{ attr.name }}:</span>

        <!-- 生成多个属性值（选项） -->
        <!-- 如果已经选中属性值，则显示为primary -->
        <el-button
            size="mini"
            :type="queryCondition.attrs[attr.name] === value ? 'primary' : 'default'"
            v-for="value in attr.values"
            :key="value"
            @click="selectAttr(attr.name, value)"
        >
          {{ value }}
        </el-button>
      </div>
    </div>
    <!-- 动态属性筛选 -->


    <!-- 价格排序 -->
    <div class="sort-bar">
      <el-button
          size="mini"
          :type="queryCondition.sortByPrice === 'asc' ? 'primary' : 'default'"
          @click="setPriceSort('asc')"
      >
        価格の安い順
      </el-button>

      <el-button
          size="mini"
          :type="queryCondition.sortByPrice === 'desc' ? 'primary' : 'default'"
          @click="setPriceSort('desc')"
      >
        価格の高い順
      </el-button>
    </div>
    <!-- 价格排序 -->


    <!-- 商品展示 -->
    <div class="ct-list">
      <el-row :gutter="20" class="product-list">
        <el-col :span="6" v-for="sku in skus" :key="sku.id">
          <nuxt-link :to="`/product/${sku.id}`">
            <el-card :body-style="{ padding: '10px' }" class="product-card">
              <img :src="sku.picture" class="product-img"/>
              <div class="product-name">{{ sku.name }}</div>
              <div class="product-price">¥{{ sku.price }}</div>
            </el-card>
          </nuxt-link>
        </el-col>
      </el-row>
    </div>
    <!-- 商品展示 -->


    <!-- 分页 -->
    <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page.sync="pageNum"
        @current-change="selectByCondition"
    >
    </el-pagination>
    <!-- 分页 -->
  </div>
</template>


<script>
import searchApi from "@/api/search/search";

export default {
  data() {
    return {
      queryCondition: {
        name: "",
        attrs: {},      // {"颜色": "白色", "存储": "256GB"}
        sortByPrice: ""    // "asc" 或 "desc"
      },

      skus: [],  // sku列表
      filterAttrs: [],     // 属性名和属性的所有值，[{ name: '颜色', values: ['黑色','白色'] }, { name: '存储', values: ['256GB','512GB'] }]
      pageNum: 1,
      pageSize: 0,
      total: 0
    };
  },

  created() {
    const s = this.$route.params.s || '';  // 从路径中取出搜索词
    this.queryCondition.name = s;  // 设置condition的查询词
    this.selectByCondition();
  },

  methods: {
    // 点击属性选项，改变condition并查询
    selectAttr(attrName, value) {
      // attrs 是一个map
      // 点击已选中，则在map中取消一个属性名和属性值
      if (this.queryCondition.attrs[attrName] === value) {
        this.$delete(this.queryCondition.attrs, attrName);
        // 如果没选中，则在map中设置一个属性名和属性值
      } else {
        this.$set(this.queryCondition.attrs, attrName, value);
      }
      // 再次查询
      this.pageNum = 1;
      this.selectByCondition();
    },

    // 设置价格排序
    setPriceSort(order) {
      // 如果已选中，则取消
      if (this.queryCondition.sortByPrice === order) {
        this.queryCondition.sortByPrice = "";
        // 如果没有选中，则设置价格排序
      } else {
        this.queryCondition.sortByPrice = order;
      }
      // 再次查询
      this.pageNum = 1;
      this.selectByCondition();
    },

    // 查询sku
    selectByCondition(current = 1) {
      const esPage = current - 1; // ES分页从0开始

      // 从查询条件中取出属性名和属性值，转成一个string数组，属性名:属性值
      const attrsList = Object.entries(this.queryCondition.attrs)
          .map(([k, v]) => `${k}:${v}`)

      // 重新构建condition
      const condition = {
        name: this.queryCondition.name,
        attrs: attrsList,
        sortByPrice: this.queryCondition.sortByPrice
      }

      searchApi.search(esPage, condition)
          .then((response) => {
            const record = response.data.data.record;
            this.skus = record.content;
            this.pageNum = record.number + 1;
            this.pageSize = record.size;
            this.total = parseInt(record.totalElements);  //string转成int

            // 根据所有返回的sku，动态生成属性选项
            this.generateFilterAttrs(record.content);
          })
          .catch(() => {  // 如果出错了，则清空
            this.skus = [];
            this.filterAttrs = [];
          });
    },


    // 从返回的sku数据生成动态属性筛选
    generateFilterAttrs(skus) {
      // 属性名和属性的所有值，{ '颜色': ['黑色','白色'] , '存储': ['256GB','512GB'] }
      const attrMap = {}
      // 遍历每个sku
      skus.forEach(sku => {
        // 每个sku有多个属性名，每个属性名有多个属性值（选项）
        sku.attrTOs.forEach(attr => {
          // 如果发现一个还没加入的属性名，新建一个set
          if (!attrMap[attr.name]) {
            attrMap[attr.name] = new Set()
          }
          // 把属性名对应的属性值（选项）加入set中
          attr.attrValueTOs.forEach(v => attrMap[attr.name].add(v.name));
        })
      })

      this.filterAttrs = Object.entries(attrMap).map(([name, valuesSet]) => ({
        name: name,
        values: Array.from(valuesSet)
      }))
    }

  }
};
</script>

<style>
.product-search-page {
  width: 85%; /* 整个页面内容占屏幕90% */
  margin: 0 auto; /* 居中 */
}

.filter-bar,
.sort-bar {
  margin-bottom: 15px;
}

.filter-item {
  margin-bottom: 5px;
}

.filter-name {
  font-weight: bold;
  margin-right: 5px;
}

.ct-list {
  width: 100%; /* el-row 占满父容器 */
}

.product-list {
  width: 100%; /* el-row 占满父容器 */
}

.product-card {
  text-align: center;
  margin-bottom: 30px; /* 下方留出间距 */
}

.product-img {
  max-width: 100%;
  max-height: 150px;
  display: block;
  margin: 0 auto 10px;
}

.product-name {
  font-weight: bold;
  margin: 10px 0 5px;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  color: #f56c6c;
  font-size: 15px;
}

.el-pagination {
  text-align: center;
  margin: 20px 0;
}

</style>
