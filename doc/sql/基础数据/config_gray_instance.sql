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

 Date: 09/12/2020 23:20:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_gray_instance
-- ----------------------------
DROP TABLE IF EXISTS `config_gray_instance`;
CREATE TABLE `config_gray_instance` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `instance_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'instance_id',
  `service_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'service_id',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0：禁用，1：启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_del` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instanceid` (`instance_id`) USING BTREE,
  KEY `idx_serviceid` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开启灰度服务器实例';

-- ----------------------------
-- Records of config_gray_instance
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
