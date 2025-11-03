const envFile = `.env.${process.env.NODE_ENV}`;
require('dotenv').config({ path: envFile });

export default {
  env: {
    API_BASE: process.env.NUXT_ENV_API_BASE
  },

  ssr: false,
  target: 'static',

  server: {
    port: 3000,
    host: 'localhost'
  },

  plugins: [
    { src: '~/plugins/nuxt-plugin.js', ssr: false }
  ],

  // 防止热更新卡顿
  // build: {
  //   cache: true,
  //   parallel: true,
  //   hardSource: true, // 开启硬盘缓存
  //
  //   watch: ['pages', 'layouts', 'components'], // 可选，监听特定目录
  //   extend(config, { isDev, isClient }) {
  //     if (isDev && isClient) {
  //       config.watchOptions = {
  //         aggregateTimeout: 1000,  // ms, 保存后等待时间，避免重复构建
  //         poll: 1000,     // 文件轮询间隔
  //         ignored: /node_modules/   // 忽略 node_modules
  //       }
  //     }
  //   }
  // },

  head: {
    title: 'myshop - 在线购物网站',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'keywords', name: 'keywords', content: '在线购物,手机,电脑,服装,家具' },
      { hid: 'description', name: 'description', content: 'myshop是国内领先的购物平台' }
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }]
  }
}
