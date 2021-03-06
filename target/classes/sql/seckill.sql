-- 数据库脚本

-- 创建数据库
create database seckill;

-- 使用数据库

use seckill

-- 创建 秒杀库存表
CREATE database seckill;

use seckill;

CREATE table seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开启时间',
`end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='描述库存表';

insert into seckill(name,number,start_time,end_time)
values
('1000秒杀iPhone6s',100,'2016-06-15 00:00:00','2016-6-20 00:00:00');
('500秒杀iPhone6s',200,'2016-06-15 00:00:00','2016-6-20 00:00:00');
('300秒杀iPhone6s',300,'2016-06-15 00:00:00','2016-6-20 00:00:00');
('200秒杀iPhone6s',400,'2016-06-15 00:00:00','2016-6-20 00:00:00');





create table success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`start` tinyint NOT NULL DEFAULT -1 COMMENT '状态提示:-1无效 0：成功 1：已付款',
`create_time` timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
