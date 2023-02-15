/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2022/10/01 下午5:32 >
 *
 *     Send:1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 Navicat Premium Data Transfer

 Source Server         : mysqlip
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : mysqlip:3306
 Source Schema         : msg

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 28/10/2020 11:42:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notify_subscribers
-- ----------------------------
DROP TABLE IF EXISTS `notify_subscribers`;
CREATE TABLE `notify_subscribers` (
  `id` bigint NOT NULL COMMENT '主键',
  `subscriber_type` varchar(255) DEFAULT NULL COMMENT '订阅者类型 如 user',
  `subscriber` bigint DEFAULT NULL COMMENT '订阅者ID',
  `topic_provier` varchar(100) DEFAULT NULL COMMENT '主题标识 如device_alam',
  `subscriber_name` varchar(100) DEFAULT NULL COMMENT '订阅名称',
  `topic_name` varchar(100) DEFAULT NULL COMMENT '主题名称',
  `topic_config` text COMMENT '订阅配置 根据主题标识不同而不同',
  `description` varchar(255) DEFAULT NULL COMMENT '说明',
  `state` varchar(10) DEFAULT NULL COMMENT '订阅状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of notify_subscribers
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
