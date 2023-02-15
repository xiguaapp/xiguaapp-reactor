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

 Date: 09/12/2020 23:20:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_gray
-- ----------------------------
DROP TABLE IF EXISTS `config_gray`;
CREATE TABLE `config_gray` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `service_id` varchar(64) NOT NULL DEFAULT '',
  `user_key_content` text COMMENT '用户key，多个用引文逗号隔开',
  `name_version_content` text COMMENT '需要灰度的接口，goods.get1.0=1.2，多个用英文逗号隔开',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_del` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_serviceid` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务灰度配置';

-- ----------------------------
-- Records of config_gray
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
