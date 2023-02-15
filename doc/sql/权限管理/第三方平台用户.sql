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
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for open_user
-- ----------------------------
DROP TABLE IF EXISTS `open_user`;
CREATE TABLE `open_user` (
  `id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `open_id` varchar(50) default null comment '第三方登录标示id',
  `user_type` varchar(20) DEFAULT 'mobile' COMMENT '用户类型:mobile-普通用户',
  `from_type` varchar(20) default 'system' COMMENT '(用户来源：system-系统用户 qq weixin-微信 weibo-微博等)',
  `card_front_img` varchar(255) default null COMMENT '法人身份证正面',
  `card_reverse_img` varchar(255) default null COMMENT '法人身份证反面',
  `company_id` bigint DEFAULT NULL COMMENT '企业ID',
  `real_name` varchar(20) default null COMMENT'真实姓名',
  `city`   varchar (255) default null COMMENT'城市',
  `company_name`   varchar (255) default null COMMENT'企业名',
  `company_card`   varchar (255) default null COMMENT'企业社会编码',
  `company_img`   varchar (255) default null COMMENT'企业营业执照',
  `company_type`   varchar (255) default null COMMENT'企业类型',
  `birthday` datetime default null COMMENT'生日',
  `is_real`   tinyint(2) default null COMMENT'认证：0未认证 1已认证',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint DEFAULT '1' COMMENT '状态:0-禁用 1-正常 2-锁定',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户-移动用户信息';

SET FOREIGN_KEY_CHECKS = 1;
