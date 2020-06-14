/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云
 Source Server Type    : MySQL
 Source Server Version : 50644
 Source Host           : 106.52.249.252:3306
 Source Schema         : db_novel

 Target Server Type    : MySQL
 Target Server Version : 50644
 File Encoding         : 65001

 Date: 01/06/2019 11:20:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for novel
-- ----------------------------
DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '小说表自增主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书名',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者名',
  `img` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面的链接',
  `collection` int(11) NULL DEFAULT NULL COMMENT '收藏数',
  `length` int(11) NULL DEFAULT NULL COMMENT '小说长度',
  `total_click` int(11) NULL DEFAULT NULL COMMENT '总点击数',
  `month_click` int(11) NULL DEFAULT NULL COMMENT '本月点击',
  `week_click` int(11) NULL DEFAULT NULL COMMENT '本周点击',
  `total_recommend` int(11) NULL DEFAULT NULL COMMENT '总推荐数',
  `month_recommend` int(11) NULL DEFAULT NULL COMMENT '本月推荐',
  `week_recommend` int(11) NULL DEFAULT NULL COMMENT '本周推荐',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '小说简介',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '小说评论',
  `novel_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小说详情链接',
  `chapter_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小说章节链接',
  `TYPE` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小说的类别：如武侠修真，都市言情',
  `last_update_chapter` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一章的章节名',
  `last_update_chapter_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一章的url',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '小说最后的更新时间',
  `STATUS` int(5) NULL DEFAULT NULL COMMENT '小说的状态：1 连载 2 完结',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名的首字母',
  `platform_id` int(5) NULL DEFAULT NULL COMMENT '小说平台的id',
  `add_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '这本小说存储到我们数据库的时间',
  `firstcontenturl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小说第一章内容',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`, `author`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 203676 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
