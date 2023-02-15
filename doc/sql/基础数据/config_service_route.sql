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

 Date: 09/12/2020 23:23:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_service_route
-- ----------------------------
DROP TABLE IF EXISTS `config_service_route`;
CREATE TABLE `config_service_route` (
  `id` varchar(128) NOT NULL DEFAULT '' COMMENT '路由id',
  `service_id` varchar(128) NOT NULL DEFAULT '',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '接口名',
  `version` varchar(64) NOT NULL DEFAULT '' COMMENT '版本号',
  `predicates` varchar(256) DEFAULT NULL COMMENT '路由断言（SpringCloudGateway专用）',
  `filters` varchar(256) DEFAULT NULL COMMENT '路由过滤器（SpringCloudGateway专用）',
  `uri` varchar(128) NOT NULL DEFAULT '' COMMENT '路由规则转发的目标uri',
  `path` varchar(128) NOT NULL DEFAULT '' COMMENT 'uri后面跟的path',
  `order_index` int NOT NULL DEFAULT '0' COMMENT '路由执行的顺序',
  `ignore_validate` tinyint NOT NULL DEFAULT '0' COMMENT '是否忽略验证，业务参数验证除外',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态，0：待审核，1：启用，2：禁用',
  `merge_result` tinyint NOT NULL DEFAULT '0' COMMENT '是否合并结果',
  `permission` tinyint NOT NULL DEFAULT '0' COMMENT '是否需要授权才能访问',
  `need_token` tinyint NOT NULL DEFAULT '0' COMMENT '是否需要token',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_del` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_serviceid` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路由配置';

-- ----------------------------
-- Records of config_service_route
-- ----------------------------
BEGIN;
INSERT INTO `config_service_route` VALUES ('bigdata.get1.0', 'story-service', 'bigdata.get', '1.0', '[]', '[]', 'lb://story-service', '/story/bigdata/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('category.get1.0', 'story-service', 'category.get', '1.0', '[]', '[]', 'lb://story-service', '/story/category/get/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('file.download1.0', 'story-service', 'file.download', '1.0', '[]', '[]', 'lb://story-service', '/download/file1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('file.upload1.0', 'story-service', 'file.upload', '1.0', '[]', '[]', 'lb://story-service', '/upload/file1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('file.upload21.0', 'story-service', 'file.upload2', '1.0', '[]', '[]', 'lb://story-service', '/upload/file2', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('file.upload31.0', 'story-service', 'file.upload3', '1.0', '[]', '[]', 'lb://story-service', '/upload/file3', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('goods.add1.0', 'story-service', 'goods.add', '1.0', '[]', '[]', 'lb://story-service', '/jsr303', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('goods.update1.0', 'story-service', 'goods.update', '1.0', '[]', '[]', 'lb://story-service', '/ex', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('open.auth.token.app1.0', 'sop-auth', 'open.auth.token.app', '1.0', '[]', '[]', 'lb://sop-auth', '/oauth2/fetchToken', 0, 0, 1, 1, 0, 0, '2020-09-06 20:07:19', '2020-09-06 20:07:19', NULL);
INSERT INTO `config_service_route` VALUES ('sdt.get4.0', 'story-service', 'sdt.get', '4.0', '[]', '[]', 'lb://story-service', '/story/get/v4', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.get.ignore1.0', 'story-service', 'story.get.ignore', '1.0', '[]', '[]', 'lb://story-service', '/story/get/ignore/v1', 0, 1, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.get.large1.0', 'story-service', 'story.get.large', '1.0', '[]', '[]', 'lb://story-service', '/story/get/large/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.get.permission1.0', 'story-service', 'story.get.permission', '1.0', '[]', '[]', 'lb://story-service', '/perm/get', 0, 0, 1, 1, 1, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.get.token1.0', 'story-service', 'story.get.token', '1.0', '[]', '[]', 'lb://story-service', '/token', 0, 0, 1, 1, 0, 1, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.get1.0', 'story-service', 'story.get', '1.0', '[]', '[]', 'lb://story-service', '/story/get/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.get2.0', 'story-service', 'story.get', '2.0', '[]', '[]', 'lb://story-service', '/story/get/v2', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.list1.0', 'story-service', 'story.list', '1.0', '[]', '[]', 'lb://story-service', '/story/list/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.oneparam1.0', 'story-service', 'story.oneparam', '1.0', '[]', '[]', 'lb://story-service', '/story/oneParam/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.oneparam1.1', 'story-service', 'story.oneparam', '1.1', '[]', '[]', 'lb://story-service', '/story/oneParam/v2', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.oneparam1.2', 'story-service', 'story.oneparam', '1.2', '[]', '[]', 'lb://story-service', '/story/oneParam/v3', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.param.bind1.0', 'story-service', 'story.param.bind', '1.0', '[]', '[]', 'lb://story-service', '/story/get/param/v1', 0, 0, 1, 0, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.system.param.get1.0', 'story-service', 'story.system.param.get', '1.0', '[]', '[]', 'lb://story-service', '/story/get/system/param/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('story.tree.get1.0', 'story-service', 'story.tree.get', '1.0', '[]', '[]', 'lb://story-service', '/story/tree/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
INSERT INTO `config_service_route` VALUES ('test.head1.0', 'story-service', 'test.head', '1.0', '[]', '[]', 'lb://story-service', '/story/get/header/v1', 0, 0, 1, 1, 0, 0, '2020-12-08 15:46:02', '2020-12-08 15:46:02', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
