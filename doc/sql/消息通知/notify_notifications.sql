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

 Date: 28/10/2020 11:42:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notify_notifications
-- ----------------------------
DROP TABLE IF EXISTS `notify_notifications`;
CREATE TABLE `notify_notifications` (
  `id` bigint NOT NULL COMMENT 'ID',
  `subscribe_id` bigint DEFAULT NULL COMMENT '订阅者id',
  `subscribe_type` varchar(255) DEFAULT NULL COMMENT '订阅类型',
  `subscriber` varchar(255) DEFAULT NULL COMMENT '订阅者',
  `topic_provider` varchar(255) DEFAULT NULL COMMENT '主题标识',
  `topic_name` varchar(255) DEFAULT NULL COMMENT '主题名称',
  `message` text COMMENT '通知消息内容',
  `data_id` varchar(255) DEFAULT NULL COMMENT '数据id',
  `notify_time` datetime DEFAULT NULL COMMENT '通知时间',
  `state` varchar(10) DEFAULT NULL COMMENT '通知状态',
  `description` varchar(255) DEFAULT NULL COMMENT '说明',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息订阅者信息';

-- ----------------------------
-- Records of notify_notifications
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
