
/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2022/10/01 下午5:57 >
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
 Source Schema         : reactor_xiguaapp

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 18/11/2020 17:38:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for isv_info
-- ----------------------------
DROP TABLE IF EXISTS `isv_info`;
CREATE TABLE `isv_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `app_key` varchar(100) NOT NULL COMMENT 'appKey',
  `pub_key` text NOT NULL COMMENT '公钥',
  `pri_key` text NOT NULL COMMENT '私钥',
  `secret` varchar(200) NOT NULL COMMENT 'secret',
  `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '1启用，2禁用',
  `sign_type` tinyint NOT NULL DEFAULT '1' COMMENT '1:RSA2,2:MD5',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_del` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_key` (`app_key`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='isv信息表';

SET FOREIGN_KEY_CHECKS = 1;
