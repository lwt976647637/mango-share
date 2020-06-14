CREATE TABLE `t_sync_blogs` (
  `id` bigint(200) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_id` varchar(200) NOT NULL COMMENT '用户编码',
    `title` varchar(200) NOT NULL COMMENT '博文标题',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='博客同步表';


CREATE TABLE `t_blogs_category` (
  `id` bigint(200) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `category_type` varchar(200) NOT NULL COMMENT '博客分类',
    `sort` int(20) NOT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='博客分类表';