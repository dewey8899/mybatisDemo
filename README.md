# mybatisDemo
新建数据库platform

1、建表
CREATE TABLE `tb_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_mark` varchar(255) DEFAULT NULL,
  `service_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

2.CREATE TABLE `tb_report` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `agency_code` varchar(255) DEFAULT NULL,
    `agency_name` varchar(255) DEFAULT NULL,
    `service_code` varchar(255) DEFAULT NULL,
    `service_name` varchar(255) DEFAULT NULL,
    `brand_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '厂商品牌',
    `material_number` varchar(255) DEFAULT NULL,
    `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0',
    `delivery_quantity` int(11) NOT NULL DEFAULT '0' COMMENT '发货数量',
    `delivery_agency_amount` decimal(15,2) NOT NULL DEFAULT '0.00',
    `replenish_quantity` int(11) NOT NULL DEFAULT '0',
    `replenish_agency_amount` decimal(15,2) NOT NULL DEFAULT '0.00',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;
  3.把src/sql下的两个xls文件的数据导入到对应的表中后，即可执行程序Application.java类
  

