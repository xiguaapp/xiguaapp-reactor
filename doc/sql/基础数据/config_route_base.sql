/*
 Navicat Premium Data Transfer

 Source Server         : mysqlip
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : mysqlip:3306
 Source Schema         : reactor_xiguaapp

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 09/12/2020 23:22:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_route_base
-- ----------------------------
DROP TABLE IF EXISTS `config_route_base`;
CREATE TABLE `config_route_base` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `route_id` varchar(64) NOT NULL DEFAULT '' COMMENT '路由id',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态，1：启用，2：禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_del` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_routeid` (`route_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='路由配置表';

-- ----------------------------
-- Records of config_route_base
-- ----------------------------
BEGIN;
INSERT INTO `config_route_base` VALUES (1, 'story.get1.1', 1, '2019-04-09 19:15:58', '2019-04-09 19:16:54', NULL);
INSERT INTO `config_route_base` VALUES (2, 'alipay.story.get1.0', 1, '2019-04-09 19:19:57', '2019-04-19 14:45:33', NULL);
INSERT INTO `config_route_base` VALUES (3, 'alipay.story.find1.0', 1, '2019-04-11 09:29:55', '2019-04-11 09:35:02', NULL);
INSERT INTO `config_route_base` VALUES (4, 'alipay.book.story.get1.0', 1, '2019-04-16 10:23:44', '2019-04-16 10:23:44', NULL);
INSERT INTO `config_route_base` VALUES (5, 'spirngmvc.goods.get1.0', 2, '2019-04-16 10:24:08', '2019-04-16 10:24:08', NULL);
INSERT INTO `config_route_base` VALUES (6, 'alipay.category.get1.0', 1, '2019-05-06 16:50:39', '2019-05-20 17:01:48', NULL);
INSERT INTO `config_route_base` VALUES (7, 'permission.story.get1.0', 1, '2019-05-06 20:03:17', '2019-05-06 20:03:21', NULL);
INSERT INTO `config_route_base` VALUES (8, 'goods.add1.0', 1, '2019-05-13 17:23:00', '2019-05-13 17:23:11', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
