<template>
  <el-upload
      :show-file-list="false"
      :action="uploadUrl"
      :with-credentials="true"
      :on-success="handleSuccess"
      :before-upload="beforeUpload"
      class="picture-uploader">
    <img v-if="value" :src="value" class="picture" />
    <i v-else class="el-icon-plus picture-uploader-icon"></i>
  </el-upload>
</template>

<script>
import service from '@/utils/request'

export default {
  name: 'UploadImage',
  props: {
    value: String  // Vue2 v-model 默认是 value
  },
  computed: {
    uploadUrl() {
      return service.defaults.baseURL + '/file/file/upload'
    }
  },
  methods: {
    handleSuccess(res) {
      this.$emit('input', res.data.url)
    },
    beforeUpload(file) {
      // 可加校验逻辑，例如文件类型、大小
      return true
    }
  }
}
</script>

<style scoped>
.picture-uploader {
  position: relative;
}

.picture-uploader-icon {
  position: absolute;
  top: 50%;
  left: 20%;
  transform: translate(-50%, -50%);
  font-size: 28px;
  color: #409EFF;
}

.picture {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block; /* 避免默认 inline 导致不显示 */
}

</style>
