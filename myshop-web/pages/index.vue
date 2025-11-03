<template>
  <div>
    <!--轮播图 -->
    <div class="banner-carousel">
      <el-carousel height="350px" :interval="3000" arrow="hover">
        <el-carousel-item v-for="item in bannerList" :key="item.id">
          <img :src="item.imgUrl" class="banner-image" @click="goLink(item.link)">
        </el-carousel-item>
      </el-carousel>
    </div>

  </div>
</template>


<script>
import productSkuApi from '@/api/product/productSku'

export default {
  data() {
    return {
      bannerList: [
        { id: 1, imgUrl: '/banners/1.jpg', link: '#' },
        { id: 2, imgUrl: '/banners/2.jpg', link: '#' },
        { id: 3, imgUrl: '/banners/3.jpg', link: '#' }
      ],

    }
  },
  created() {

  },
  methods: {

  }
}
</script>


<style scoped>
.product-list {
  width: 70%;
  margin: 0 auto;
  /* 居中 */
}


.banner-carousel {
  width: 70%;
  margin: 0 auto;
  /* 居中 */
}

#hot-product{
  margin-left: 15%;
  font-size: 20px;
  margin-bottom: 25px;
}
</style>