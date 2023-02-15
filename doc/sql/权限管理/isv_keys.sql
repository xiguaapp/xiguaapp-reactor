
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

 Date: 18/11/2020 17:38:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for isv_keys
-- ----------------------------
DROP TABLE IF EXISTS `isv_keys`;
CREATE TABLE `isv_keys` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `app_key` varchar(128) NOT NULL DEFAULT '',
  `sign_type` tinyint NOT NULL DEFAULT '1' COMMENT '1:RSA2,2:MD5',
  `secret` varchar(200) NOT NULL DEFAULT '' COMMENT 'sign_type=2时使用',
  `key_format` tinyint NOT NULL DEFAULT '1' COMMENT '秘钥格式，1：PKCS8(JAVA适用)，2：PKCS1(非JAVA适用)',
  `public_key_isv` text NOT NULL COMMENT '开发者生成的公钥',
  `private_key_isv` text NOT NULL COMMENT '开发者生成的私钥（交给开发者）',
  `public_key_platform` text NOT NULL COMMENT '平台生成的公钥（交给开发者）',
  `private_key_platform` text NOT NULL COMMENT '平台生成的私钥',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_del` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appkey` (`app_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='ISV秘钥';

SET FOREIGN_KEY_CHECKS = 1;
