create database IF NOT EXISTS `db_pst`;

CREATE TABLE IF NOT EXISTS `orders`(
`order_id` bigint(20) unsigned AUTO_INCREMENT,
`order_date`  date,
`order_user`  int,
`order_status`  int,
`order_price`  double,
`payment_method`  int,
`logistics_company`  varchar(100),
`logistics_number`  varchar(100),
`logistics_price`  double,
`delete_flag`  int,
`order_context`  varchar(1000),
`recommended_order`  int,
`order_uuid`    varchar(40),
   PRIMARY KEY ( `order_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `order_detail`(
`order_id` bigint(20),
`goods_id` bigint(20),
`goods_number`  int
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `commodity`(
`goods_id` bigint(20) unsigned AUTO_INCREMENT,
`add_date`  date,
`goods_name`  varchar(100),
`goods_price`  double,
`goods_stock`  int,
`goods_sales_volume`  int,
`goods_describe`  varchar(1000),
`recommended_order`  int,
`delete_flag`  int,
`goods_url`  varchar(1000),
   PRIMARY KEY ( `goods_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `users`(
`user_id` bigint(20) unsigned AUTO_INCREMENT,
`user_pwd`  varchar(100),
`reg_date`  date,
`open_id`  varchar(30),
`user_tel_num`  varchar(100),
`weixin_id`  varchar(40),
`user_sex`  varchar(3),
`user_birthday`  date,
`user_type` int,
   PRIMARY KEY ( `user_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table users add wechat_avatar_url varchar(1000);
alter table users add wechat_city varchar(50);
alter table users add wechat_country varchar(100);
alter table users add wechat_nickname varchar(500);
alter table users add wechat_province varchar(30);
alter table users add gender tinyint;


CREATE TABLE IF NOT EXISTS `shopcart`(
`user_id` bigint(20) ,
`order_id` bigint(20) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `address`(
`addr_id` bigint(20) unsigned AUTO_INCREMENT,
`user_id`   bigint,
`sort`   int default 1,
`province_name` varchar(10),
`city_name`  varchar(10),
`region` varchar(200),
`detail_info` varchar(200),
`tel_number`  varchar(20),
`user_name`  varchar(50),
`postal_code` varchar(10),
`county_name` varchar(30),
`national_code` varchar(10),
    PRIMARY KEY ( `addr_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;









