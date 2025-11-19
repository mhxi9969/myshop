-- --------------------------------------------------------
-- 主机:                           192.168.1.201
-- 服务器版本:                        5.7.20 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 myshop_order 的数据库结构
DROP DATABASE IF EXISTS `myshop_order`;
CREATE DATABASE IF NOT EXISTS `myshop_order` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;
USE `myshop_order`;

-- 导出  表 myshop_order.order_order 结构
DROP TABLE IF EXISTS `order_order`;
CREATE TABLE IF NOT EXISTS `order_order` (
  `id` bigint(20) NOT NULL COMMENT '订单ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总金额',
  `status` int(11) DEFAULT '0' COMMENT '订单状态 0-待支付 1-已支付 2-已发货 3-已完成 4-已关闭',
  `receiver_name` varchar(100) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(255) DEFAULT NULL COMMENT '收货人地址',
  `trade_id` varchar(100) DEFAULT NULL COMMENT '交易号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `close_time` datetime DEFAULT NULL COMMENT '关闭时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 正在导出表  myshop_order.order_order 的数据：~6 rows (大约)
DELETE FROM `order_order`;
/*!40000 ALTER TABLE `order_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_order` ENABLE KEYS */;

-- 导出  表 myshop_order.order_order_item 结构
DROP TABLE IF EXISTS `order_order_item`;
CREATE TABLE IF NOT EXISTS `order_order_item` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `sku_name` varchar(255) DEFAULT NULL COMMENT 'SKU名称',
  `sku_picture` varchar(500) DEFAULT NULL COMMENT 'SKU图片',
  `sku_price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  `sku_quantity` int(11) DEFAULT NULL COMMENT '商品数量',
  `total_price` decimal(10,2) DEFAULT NULL COMMENT '总价',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 正在导出表  myshop_order.order_order_item 的数据：~6 rows (大约)
DELETE FROM `order_order_item`;
/*!40000 ALTER TABLE `order_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_order_item` ENABLE KEYS */;


-- 导出 myshop_product 的数据库结构
DROP DATABASE IF EXISTS `myshop_product`;
CREATE DATABASE IF NOT EXISTS `myshop_product` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;
USE `myshop_product`;

-- 导出  表 myshop_product.product_attr 结构
DROP TABLE IF EXISTS `product_attr`;
CREATE TABLE IF NOT EXISTS `product_attr` (
  `id` bigint(20) NOT NULL COMMENT '属性ID',
  `name` varchar(100) DEFAULT NULL COMMENT '属性名称',
  `category_id` bigint(20) DEFAULT NULL COMMENT '所属分类ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';

-- 正在导出表  myshop_product.product_attr 的数据：~4 rows (大约)
DELETE FROM `product_attr`;
/*!40000 ALTER TABLE `product_attr` DISABLE KEYS */;
INSERT INTO `product_attr` (`id`, `name`, `category_id`, `create_time`, `update_time`) VALUES
	(1180134303837851648, 'ストレージ', 1180134265061511168, '2025-10-27 11:33:28', '2025-11-03 22:01:14'),
	(1180134334913449984, 'カラー', 1180134265061511168, '2025-10-27 11:33:35', '2025-11-03 22:01:21'),
	(1182829296578859008, 'CPU', 1180629958180081664, '2025-11-03 22:02:27', '2025-11-03 22:02:27'),
	(1182829368393732096, 'グラフィックカード', 1180629958180081664, '2025-11-03 22:02:44', '2025-11-03 22:02:44');
/*!40000 ALTER TABLE `product_attr` ENABLE KEYS */;

-- 导出  表 myshop_product.product_attr_value 结构
DROP TABLE IF EXISTS `product_attr_value`;
CREATE TABLE IF NOT EXISTS `product_attr_value` (
  `id` bigint(20) NOT NULL COMMENT '属性值ID',
  `name` varchar(100) DEFAULT NULL COMMENT '属性值名称',
  `attr_id` bigint(20) DEFAULT NULL COMMENT '所属属性ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性值表';

-- 正在导出表  myshop_product.product_attr_value 的数据：~9 rows (大约)
DELETE FROM `product_attr_value`;
/*!40000 ALTER TABLE `product_attr_value` DISABLE KEYS */;
INSERT INTO `product_attr_value` (`id`, `name`, `attr_id`, `create_time`, `update_time`) VALUES
	(1180134360410624000, '256GB', 1180134303837851648, '2025-10-27 11:33:41', '2025-10-29 10:25:26'),
	(1180134385144434688, '512GB', 1180134303837851648, '2025-10-27 11:33:47', '2025-10-29 10:25:31'),
	(1180134411711156224, 'ブラック', 1180134334913449984, '2025-10-27 11:33:54', '2025-11-03 22:01:34'),
	(1180134428974911488, 'ホワイト', 1180134334913449984, '2025-10-27 11:33:58', '2025-11-03 22:01:39'),
	(1182829420445044736, 'Intel', 1182829296578859008, '2025-11-03 22:02:56', '2025-11-03 22:02:56'),
	(1182829498966609920, 'AMD', 1182829296578859008, '2025-11-03 22:03:15', '2025-11-03 22:03:15'),
	(1182829539961737216, '5090', 1182829368393732096, '2025-11-03 22:03:25', '2025-11-03 22:03:25'),
	(1182829558387314688, '5080', 1182829368393732096, '2025-11-03 22:03:29', '2025-11-03 22:03:29');
/*!40000 ALTER TABLE `product_attr_value` ENABLE KEYS */;

-- 导出  表 myshop_product.product_brand 结构
DROP TABLE IF EXISTS `product_brand`;
CREATE TABLE IF NOT EXISTS `product_brand` (
  `id` bigint(20) NOT NULL COMMENT '品牌ID',
  `name` varchar(100) DEFAULT NULL COMMENT '品牌名称',
  `picture` varchar(500) DEFAULT NULL COMMENT '品牌图片URL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品品牌表';

-- 正在导出表  myshop_product.product_brand 的数据：~2 rows (大约)
DELETE FROM `product_brand`;
/*!40000 ALTER TABLE `product_brand` DISABLE KEYS */;
INSERT INTO `product_brand` (`id`, `name`, `picture`, `create_time`, `update_time`) VALUES
	(1180133221678059520, 'apple', 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/c367124f-0eaa-4330-962b-6f3e4fdf689b-apple.jpg', '2025-10-27 11:29:10', '2025-11-19 19:54:03'),
	(1182833265640345600, 'DELL', 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/dac6e079-3ccb-4936-b6a3-e935658c5682-Snipaste_2025-11-02_14-20-11.jpg', '2025-11-03 22:18:13', '2025-11-03 22:18:13');
/*!40000 ALTER TABLE `product_brand` ENABLE KEYS */;

-- 导出  表 myshop_product.product_category 结构
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE IF NOT EXISTS `product_category` (
  `id` bigint(20) NOT NULL COMMENT '分类ID',
  `name` varchar(100) DEFAULT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级分类ID，0表示顶级分类',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 正在导出表  myshop_product.product_category 的数据：~3 rows (大约)
DELETE FROM `product_category`;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` (`id`, `name`, `parent_id`, `sort`, `create_time`, `update_time`) VALUES
	(1180134230244593664, 'デジタル', 0, 1, '2025-10-27 11:33:10', '2025-11-03 22:00:42'),
	(1180134265061511168, 'スマートフォン', 1180134230244593664, 1, '2025-10-27 11:33:19', '2025-11-03 22:01:03'),
	(1180629958180081664, 'パソコン', 1180134230244593664, 2, '2025-10-28 20:23:01', '2025-11-03 22:01:00');
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;

-- 导出  表 myshop_product.product_product_sku 结构
DROP TABLE IF EXISTS `product_product_sku`;
CREATE TABLE IF NOT EXISTS `product_product_sku` (
  `id` bigint(20) NOT NULL COMMENT 'SKU ID',
  `name` varchar(100) DEFAULT NULL COMMENT 'SKU名称',
  `spu_id` bigint(20) DEFAULT NULL COMMENT '所属SPU ID',
  `picture` varchar(500) DEFAULT NULL COMMENT 'SKU图片',
  `price` decimal(10,2) DEFAULT NULL COMMENT 'SKU价格',
  `stock` int(11) DEFAULT '0' COMMENT '库存数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';

-- 正在导出表  myshop_product.product_product_sku 的数据：~8 rows (大约)
DELETE FROM `product_product_sku`;
/*!40000 ALTER TABLE `product_product_sku` DISABLE KEYS */;
INSERT INTO `product_product_sku` (`id`, `name`, `spu_id`, `picture`, `price`, `stock`, `create_time`, `update_time`) VALUES
	(1180668431482097664, 'アップル Apple スマートフォン iPhone 17 ホワイト 256GB ', 1180655856363114496, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/31d39c6a-9f88-4031-bdc9-16e18f782bf1-Snipaste_2025-10-27_12-58-21.jpg', 100.00, 98, '2025-10-28 22:55:53', '2025-11-19 20:09:36'),
	(1180839419851182080, 'アップル Apple スマートフォン iPhone 17 ホワイト 512GB ', 1180655856363114496, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/4558fead-c6b7-4440-953d-ab741fc21b57-Snipaste_2025-10-27_12-58-21.jpg', 200.00, 97, '2025-10-29 10:15:20', '2025-11-19 19:18:01'),
	(1180839507763793920, 'アップル Apple スマートフォン iPhone 17 ブラック 256GB ', 1180655856363114496, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/1cdf5419-9ca7-47cc-938e-2509fe2728f7-Snipaste_2025-10-27_12-59-30.jpg', 100.00, 100, '2025-10-29 10:15:41', '2025-11-19 19:17:55'),
	(1180839622847107072, 'アップル Apple スマートフォン iPhone 17 ブラック 512GB ', 1180655856363114496, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/5257a9f1-75fd-403c-9df0-304639130509-Snipaste_2025-10-27_12-59-30.jpg', 200.00, 100, '2025-10-29 10:16:08', '2025-11-03 22:23:35'),
	(1182835343095894016, 'DELL デル ノートパソコン Intel 5080', 1182834903872573440, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/7708c303-5295-4c4c-8d93-c4da7a200221-Snipaste_2025-11-02_14-20-49.jpg', 100.00, 92, '2025-11-03 22:26:28', '2025-11-19 20:09:36'),
	(1182835431805423616, 'DELL デル ノートパソコン Intel 5090', 1182834903872573440, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/666e59c8-1653-469d-ba66-de5d7709e93a-Snipaste_2025-11-02_14-20-49.jpg', 200.00, 99, '2025-11-03 22:26:50', '2025-11-19 20:16:00'),
	(1182835523564212224, 'DELL デル ノートパソコン AMD 5080', 1182834903872573440, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/b9d87146-122d-4a2b-8265-0ae133fed111-Snipaste_2025-11-02_14-20-49.jpg', 100.00, 100, '2025-11-03 22:27:11', '2025-11-19 19:17:48'),
	(1182835635401134080, 'DELL デル ノートパソコン AMD 5090', 1182834903872573440, 'https://top-mhxi-myshop-product-images.s3.ap-northeast-1.amazonaws.com/product/26a5b85a-eaa3-479f-a76d-481d4a6e8ef7-Snipaste_2025-11-02_14-20-49.jpg', 200.00, 100, '2025-11-03 22:27:38', '2025-11-03 22:27:38');
/*!40000 ALTER TABLE `product_product_sku` ENABLE KEYS */;

-- 导出  表 myshop_product.product_product_spu 结构
DROP TABLE IF EXISTS `product_product_spu`;
CREATE TABLE IF NOT EXISTS `product_product_spu` (
  `id` bigint(20) NOT NULL COMMENT 'SPU ID',
  `name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `category1_id` bigint(20) DEFAULT NULL COMMENT '一级分类ID，冗余存储，方便查询',
  `category2_id` bigint(20) DEFAULT NULL COMMENT '二级分类ID',
  `status` int(11) DEFAULT NULL COMMENT '状态 0-下架 1-上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SPU表';

-- 正在导出表  myshop_product.product_product_spu 的数据：~2 rows (大约)
DELETE FROM `product_product_spu`;
/*!40000 ALTER TABLE `product_product_spu` DISABLE KEYS */;
INSERT INTO `product_product_spu` (`id`, `name`, `brand_id`, `category1_id`, `category2_id`, `status`, `create_time`, `update_time`) VALUES
	(1180655856363114496, 'iphone17', 1180133221678059520, 1180134230244593664, 1180134265061511168, 1, '2025-10-28 22:05:55', '2025-11-19 19:51:09'),
	(1182834903872573440, 'DELL NOTE', 1182833265640345600, 1180134230244593664, 1180629958180081664, 1, '2025-11-03 22:24:44', '2025-11-03 23:25:10');
/*!40000 ALTER TABLE `product_product_spu` ENABLE KEYS */;

-- 导出  表 myshop_product.product_sku_attr_value 结构
DROP TABLE IF EXISTS `product_sku_attr_value`;
CREATE TABLE IF NOT EXISTS `product_sku_attr_value` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `attr_id` bigint(20) DEFAULT NULL COMMENT '属性ID',
  `value_id` bigint(20) DEFAULT NULL COMMENT '属性值ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SKU属性值关联表';

-- 正在导出表  myshop_product.product_sku_attr_value 的数据：~18 rows (大约)
DELETE FROM `product_sku_attr_value`;
/*!40000 ALTER TABLE `product_sku_attr_value` DISABLE KEYS */;
INSERT INTO `product_sku_attr_value` (`id`, `sku_id`, `attr_id`, `value_id`, `create_time`, `update_time`) VALUES
	(1180668431507263488, 1180668431482097664, 1180134303837851648, 1180134360410624000, '2025-10-28 22:55:53', '2025-11-03 22:21:58'),
	(1180668431645675520, 1180668431482097664, 1180134334913449984, 1180134428974911488, '2025-10-28 22:55:54', '2025-11-03 22:21:58'),
	(1180839419876347904, 1180839419851182080, 1180134303837851648, 1180134385144434688, '2025-10-29 10:15:20', '2025-11-03 22:22:25'),
	(1180839419888930816, 1180839419851182080, 1180134334913449984, 1180134428974911488, '2025-10-29 10:15:20', '2025-11-03 22:22:25'),
	(1180839507772182528, 1180839507763793920, 1180134303837851648, 1180134360410624000, '2025-10-29 10:15:41', '2025-11-03 22:23:23'),
	(1180839507914788864, 1180839507763793920, 1180134334913449984, 1180134411711156224, '2025-10-29 10:15:41', '2025-11-03 22:23:23'),
	(1180839622851301376, 1180839622847107072, 1180134303837851648, 1180134385144434688, '2025-10-29 10:16:08', '2025-11-03 22:23:35'),
	(1180839622859689984, 1180839622847107072, 1180134334913449984, 1180134411711156224, '2025-10-29 10:16:08', '2025-11-03 22:23:35'),
	(1182835343116865536, 1182835343095894016, 1182829296578859008, 1182829420445044736, '2025-11-03 22:26:28', '2025-11-03 22:26:28'),
	(1182835343125254144, 1182835343095894016, 1182829368393732096, 1182829558387314688, '2025-11-03 22:26:28', '2025-11-03 22:26:28'),
	(1182835431813812224, 1182835431805423616, 1182829296578859008, 1182829420445044736, '2025-11-03 22:26:50', '2025-11-03 22:26:50'),
	(1182835431818006528, 1182835431805423616, 1182829368393732096, 1182829539961737216, '2025-11-03 22:26:50', '2025-11-03 22:26:50'),
	(1182835523568406528, 1182835523564212224, 1182829296578859008, 1182829498966609920, '2025-11-03 22:27:11', '2025-11-03 22:27:11'),
	(1182835523572600832, 1182835523564212224, 1182829368393732096, 1182829558387314688, '2025-11-03 22:27:11', '2025-11-03 22:27:11'),
	(1182835635405328384, 1182835635401134080, 1182829296578859008, 1182829498966609920, '2025-11-03 22:27:38', '2025-11-03 22:27:38'),
	(1182835635409522688, 1182835635401134080, 1182829368393732096, 1182829539961737216, '2025-11-03 22:27:38', '2025-11-03 22:27:38');
/*!40000 ALTER TABLE `product_sku_attr_value` ENABLE KEYS */;


-- 导出 myshop_user 的数据库结构
DROP DATABASE IF EXISTS `myshop_user`;
CREATE DATABASE IF NOT EXISTS `myshop_user` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;
USE `myshop_user`;

-- 导出  表 myshop_user.user_user 结构
DROP TABLE IF EXISTS `user_user`;
CREATE TABLE IF NOT EXISTS `user_user` (
  `id` bigint(20) NOT NULL COMMENT '用户ID',
  `name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `role` char(50) DEFAULT NULL COMMENT '角色 USER用户 ADMIN管理员',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 正在导出表  myshop_user.user_user 的数据：~2 rows (大约)
DELETE FROM `user_user`;
/*!40000 ALTER TABLE `user_user` DISABLE KEYS */;
INSERT INTO `user_user` (`id`, `name`, `password`, `email`, `phone`, `role`, `create_time`, `update_time`) VALUES
	(1182850641547104256, 'admin', '$2a$10$x4fbCJF9I9OK1u1E.4OGPOsJ5zz81lZd5WYkil0YELC1pecsngl/W', NULL, NULL, 'ADMIN', '2025-11-03 23:27:14', '2025-11-03 23:27:36');
/*!40000 ALTER TABLE `user_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
