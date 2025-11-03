<template>
  <div class="product-search-page">

    <!-- 动态属性筛选 -->
    <div class="filter-bar" v-if="filterAttrs.length">
      <div class="filter-item" v-for="attr in filterAttrs" :key="attr.name">
        <span class="filter-name">{{ attr.name }}:</span>
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

    <!-- 价格排序 -->
    <div class="sort-bar">
      <el-button
          size="mini"
          :type="queryCondition.sortByPrice === 'asc' ? 'primary' : 'default'"
          @click="setPriceSort('asc')"
      >
        价格低到高
      </el-button>
      <el-button
          size="mini"
          :type="queryCondition.sortByPrice === 'desc' ? 'primary' : 'default'"
          @click="setPriceSort('desc')"
      >
        价格高到低
      </el-button>
    </div>

    <!-- 商品展示 -->
    <div class="ct-list">
      <el-row :gutter="20" class="product-list">
        <el-col :span="6" v-for="sku in skus" :key="sku.id">
          <nuxt-link :to="`/product/${sku.id}`">
            <el-card :body-style="{ padding: '10px' }" class="product-card">
              <img :src="sku.picture" class="product-img" />
              <div class="product-name">{{ sku.name }}</div>
              <div class="product-price">¥{{ sku.price }}</div>
            </el-card>
          </nuxt-link>
        </el-col>
      </el-row>
    </div>

    <!-- 分页 -->
    <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page.sync="pageNum"
        @current-change="selectByCondition"
    ></el-pagination>
  </div>
</template>

<script>
import searchApi from "@/api/search/Search";

export default {
  data() {
    return {
      queryCondition: {
        name: "",
        attrs: {},      // {"颜色": "白色", "存储": "256GB"}
        sortByPrice: ""    // "asc" 或 "desc"
      },
      skus: [],
      filterAttrs: [],     // [{ name: '颜色', values: ['黑色','白色'] }, { name: '存储', values: ['256GB','512GB'] }]
      pageNum: 1,
      pageSize: 0,
      total: 0
    };
  },
  created() {
    const s = this.$route.params.s  || '';
    this.queryCondition.name = s;
    this.selectByCondition();
  },
  methods: {

    // 点击属性选项
    selectAttr(attrName, value) {
      if (this.queryCondition.attrs[attrName] === value) {
        // 点击已选中则取消
        this.$delete(this.queryCondition.attrs, attrName);
      } else {
        this.$set(this.queryCondition.attrs, attrName, value);
      }
      this.pageNum = 1;
      this.selectByCondition();
    },

    // 设置价格排序
    setPriceSort(order) {
      if (this.queryCondition.sortByPrice === order) {
        this.queryCondition.sortByPrice = "";
      } else {
        this.queryCondition.sortByPrice = order;
      }
      this.pageNum = 1;
      this.selectByCondition();
    },

    // 查询
    selectByCondition(current = 1) {
      const esPage = current - 1; // ES分页从0开始
      const attrsList = Object.entries(this.queryCondition.attrs)
          .map(([k, v]) => `${k}:${v}`);

      const payload = {
        name: this.queryCondition.name,
        attrs: attrsList,
        sortByPrice: this.queryCondition.sortByPrice
      };

      searchApi.search(esPage, payload)
          .then((response) => {
            const record = response.data.data.record;
            this.skus = record.content;
            this.pageNum = record.number + 1;
            this.pageSize = record.size;
            this.total = parseInt(record.totalElements);

            // 动态生成属性过滤选项
            this.generateFilterAttrs(record.content);
          })
          .catch(() => {
            this.skus = [];
            this.filterAttrs = [];
          });
    },

    // 从返回数据生成动态属性筛选
    generateFilterAttrs(skus) {
      const attrMap = {};
      skus.forEach(sku => {
        sku.attrTOs.forEach(attr => {
          if (!attrMap[attr.name]) attrMap[attr.name] = new Set();
          attr.attrValueTOs.forEach(v => attrMap[attr.name].add(v.name));
        });
      });

      this.filterAttrs = Object.entries(attrMap).map(([name, valuesSet]) => ({
        name,
        values: Array.from(valuesSet)
      }));
    }

  }
};
</script>

<style>
.product-search-page {
  width: 85%;        /* 整个页面内容占屏幕90% */
  margin: 0 auto;    /* 居中 */
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
  width: 100%;       /* el-row 占满父容器 */
}

.product-list {
  width: 100%;       /* el-row 占满父容器 */
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
