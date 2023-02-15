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
 Source Schema         : xiguaapp_pro

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 19/10/2020 18:39:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_api
-- ----------------------------
DROP TABLE IF EXISTS `system_api`;
CREATE TABLE `system_api` (
  `id` bigint NOT NULL COMMENT '接口ID',
  `api_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口编码',
  `api_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  `api_category` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'default' COMMENT '接口分类:default-默认分类',
  `api_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `request_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
  `content_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '响应类型',
  `service_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '服务ID',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `sort` bigint NOT NULL DEFAULT '0' COMMENT '优先级',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `is_auth` tinyint NOT NULL DEFAULT '1' COMMENT '是否需要认证: 0-无认证 1-身份认证 默认:1',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '方法名',
  PRIMARY KEY (`api_id`),
  UNIQUE KEY `api_code` (`api_code`) USING BTREE,
  UNIQUE KEY `api_id` (`api_id`) USING BTREE,
  KEY `id` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统资源-API接口';

SET FOREIGN_KEY_CHECKS = 1;
