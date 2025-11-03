<template>
  <div class="product-detail-page">
    <el-card class="product-detail-card">
      <el-row :gutter="20">
        <!-- 左侧图片 -->
        <el-col :span="12">
          <img :src="productSku.picture" class="product-img" />
        </el-col>

        <!-- 右侧信息 -->
        <el-col :span="12">
          <h2>{{ productSku.name }}</h2>
          <p class="price">価格：¥{{ productSku.price }}</p>
          <p class="description">{{ productSku.description }}</p>

          <!-- 动态属性选择 -->
          <div class="filter-bar" v-if="filterAttrs.length">
            <div class="filter-item" v-for="attr in filterAttrs" :key="attr.name">
              <span class="filter-name">{{ attr.name }}:</span>
              <el-button
                  size="mini"
                  v-for="value in attr.values"
                  :key="value"
                  :type="selectedAttrs[attr.name] === value ? 'primary' : 'default'"
                  @click="selectAttr(attr.name, value)"
              >
                {{ value }}
              </el-button>
            </div>
          </div>

          <!-- 数量和加入购物车 -->
          <el-input-number v-model="count" :min="1" :max="100" label="数量"></el-input-number>
          <el-button type="primary" @click="toCart">カートに追加</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import productSkuApi from '@/api/product/product-sku'
import cartApi from '@/api/cart/cart'
import searchApi from "@/api/search/search";

export default {
  data() {
    return {
      productSku: {},        // 当前 SKU
      brotherSkus: [],       // 同 SPU 所有 SKU
      filterAttrs: [],       // 属性按钮列表
      selectedAttrs: {},     // 当前选中属性
      count: 1
    }
  },
  created() {
    this.loadSku(this.$route.params.id)
  },
  watch: {
    '$route.params.id'(newId) {
      this.loadSku(newId)
    }
  },
  methods: {

    async loadSku(id) {
      // 获取当前 SKU
      const res = await productSkuApi.selectByIdTree(id)
      this.productSku = res.data.data.record
      console.log(this.productSku)


      // 获取同 SPU 所有 SKU
      if (!this.productSku.spuId) return
      const skuRes = await searchApi.searchBySpuID(this.productSku.spuId)
      this.brotherSkus = skuRes.data.data.record || []

      // 初始化选中属性：使用当前 SKU 的实际值
      this.selectedAttrs = {}
      if (this.productSku.attrTOs) {
        this.productSku.attrTOs.forEach(attr => {
          this.selectedAttrs[attr.name] = attr.skuValueName   //用 skuValueName 初始化
        })

      }

      this.generateFilterAttrs()
    },

    // 构建所有可选属性值
    generateFilterAttrs() {
      const attrMap = {}
      this.brotherSkus.forEach(sku => {
        sku.attrTOs.forEach(attr => {
          if (!attrMap[attr.name]) attrMap[attr.name] = new Set()
          attr.attrValueTOs.forEach(v => attrMap[attr.name].add(v.name))
        })
      })
      this.filterAttrs = Object.entries(attrMap).map(([name, valuesSet]) => ({
        name,
        values: Array.from(valuesSet)
      }))
    },

    selectAttr(attrName, value) {
      // 更新选中属性
      this.selectedAttrs[attrName] = value

      // 查找匹配 SKU
      const matchSku = this.brotherSkus.find(sku => {
        const attrsObj = {}
        sku.attrTOs.forEach(a => {
          attrsObj[a.name] = a.skuValueName   // ✅ 关键改动
        })
        return Object.entries(this.selectedAttrs).every(([k, v]) => attrsObj[k] === v)
      })

      // 跳转到对应 SKU
      if (matchSku && matchSku.id !== this.productSku.id) {
        this.$router.push(`/product/${matchSku.id}`)
      } else {
        console.warn('未找到匹配 SKU', this.selectedAttrs)
      }
    },

    toCart() {
      if (!this.productSku.id) return
      const cartVo = {
        skuId: this.productSku.id,
        quantity: this.count,
        checked: 1
      }

      cartApi.insert(cartVo)
          .then(() => {
            this.$router.push('/cart')
          })
          .catch(() => {

          })
    }
  }
}
</script>



<style scoped>
.product-detail-page {
  width: 90%;
  margin: 0 auto;
}

.product-img {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  margin: 10px 0;
}

.description {
  margin-bottom: 15px;
}

.filter-bar {
  margin: 15px 0;
}

.filter-item {
  margin-bottom: 10px;
}

.filter-name {
  font-weight: bold;
  margin-right: 10px;
}
</style>
