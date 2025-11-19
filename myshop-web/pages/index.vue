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
    <!--轮播图 -->
  </div>
</template>


<script>
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



<style>
.banner-carousel {
  width: 70%;
  margin: 0 auto;
  /* 居中 */
}
</style>