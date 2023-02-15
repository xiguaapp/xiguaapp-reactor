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

 Date: 19/10/2020 20:55:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for merchant_type
-- ----------------------------
DROP TABLE IF EXISTS `merchant_type`;
CREATE TABLE `merchant_type` (
  `id` bigint DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_del` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='企业类型';

SET FOREIGN_KEY_CHECKS = 1;
