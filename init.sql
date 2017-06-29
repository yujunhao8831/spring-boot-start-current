/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost
 Source Database       : blog

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : utf-8

 Date: 06/23/2017 18:39:49 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `manager_permission_resource`
-- ----------------------------
DROP TABLE IF EXISTS `manager_permission_resource`;
CREATE TABLE `manager_permission_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) NOT NULL COMMENT '父权限资源ID(0:表示root级)',
  `resource_depth` int(11) DEFAULT NULL COMMENT '资源深度',
  `permission_sort` int(11) DEFAULT NULL COMMENT '排序字段',
  `permission_name` varchar(128) NOT NULL COMMENT '权限名称',
  `resource_class` varchar(128) DEFAULT NULL COMMENT '资源样式class(前端class属性)',
  `resource_style` varchar(128) DEFAULT NULL COMMENT '资源样式style(前端style属性)',
  `resource_router_url` varchar(128) DEFAULT NULL COMMENT '资源路由URL(前端使用)',
  `resource_type` varchar(8) NOT NULL COMMENT '资源类型(API:接口,MENU:菜单,BUTTON:按钮)',
  `resource_api_uri` varchar(128) DEFAULT NULL COMMENT '资源API URI(非必须,api才有)',
  `resource_api_uri_methods` varchar(128) DEFAULT NULL COMMENT '资源API URI方法methods(GET,POST,DELETE,PUT,以'',''分割)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `category_code` varchar(8) DEFAULT NULL COMMENT '分类(C,R,U,D)冗余字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='后台管理权限资源表';

-- ----------------------------
--  Records of `manager_permission_resource`
-- ----------------------------
BEGIN;
INSERT INTO `manager_permission_resource` VALUES ('1', '0', null, '1', '用户管理', null, null, null, 'MENU', null, 'GET', '2017-06-21 09:29:45', '2017-06-21 09:29:45', null, 'C'), ('2', '1', null, '1', '用户列表', null, null, null, 'MENU', null, 'GET', '2017-06-21 09:29:45', '2017-06-21 09:29:45', null, 'C'), ('3', '2', null, '1', '查看用户', null, null, null, 'API', '/api/user', 'GET', '2017-06-21 09:29:45', '2017-06-22 16:42:53', null, 'C'), ('4', '2', null, '2', '查看用户', null, null, null, 'API', '/api/user/list', 'GET', '2017-06-21 09:29:45', '2017-06-21 11:29:13', null, 'C'), ('5', '2', null, '3', '查看用户', null, null, null, 'API', '/api/user/{id}', 'GET', '2017-06-21 09:29:45', '2017-06-22 17:08:18', null, 'C'), ('6', '2', null, '5', '删除用户', null, null, null, 'API', '/api/user/{id}', 'DELETE', '2017-06-21 09:29:45', '2017-06-21 11:29:13', null, 'C');
COMMIT;

-- ----------------------------
--  Table structure for `manager_role`
-- ----------------------------
DROP TABLE IF EXISTS `manager_role`;
CREATE TABLE `manager_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `role_name_code` varchar(32) NOT NULL COMMENT '角色名称core',
  `description` varchar(128) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_role_role_name_code_uk` (`role_name_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='后台管理角色表';

-- ----------------------------
--  Records of `manager_role`
-- ----------------------------
BEGIN;
INSERT INTO `manager_role` VALUES ('1', '超级管理员', 'admin', null, '2017-06-21 09:36:20', '2017-06-22 14:27:07', null);
COMMIT;

-- ----------------------------
--  Table structure for `manager_role_permission_resource`
-- ----------------------------
DROP TABLE IF EXISTS `manager_role_permission_resource`;
CREATE TABLE `manager_role_permission_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '后台管理角色_id',
  `permission_resource_id` bigint(20) NOT NULL COMMENT '后台管理权限资源_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='后台管理角色资源中间表';

-- ----------------------------
--  Records of `manager_role_permission_resource`
-- ----------------------------
BEGIN;
INSERT INTO `manager_role_permission_resource` VALUES ('1', '1', '1'), ('2', '1', '2'), ('3', '1', '3'), ('4', '1', '4'), ('5', '1', '5'), ('6', '1', '6');
COMMIT;

-- ----------------------------
--  Table structure for `manager_user`
-- ----------------------------
DROP TABLE IF EXISTS `manager_user`;
CREATE TABLE `manager_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) NOT NULL COMMENT '用户名(登录名称)',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nick_name` varchar(128) DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(128) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(128) DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(18) DEFAULT NULL COMMENT '手机号码',
  `last_password_reset_date` datetime DEFAULT NULL COMMENT '密码最后重置(修改)日期',
  `create_manager_admin_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_enabled` tinyint(1) NOT NULL COMMENT '账户状态(1:激活,0:锁定)',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `manager_admin_user_username_uk_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='后台管理用户';

-- ----------------------------
--  Records of `manager_user`
-- ----------------------------
BEGIN;
INSERT INTO `manager_user` VALUES ('1', 'admin', '$2a$10$tPOCtMrQ/HisOCCO.zMbTu33BvOeE3kf1Bm9sH2OZ4XawI6ngpjnW', '披荆斩棘', null, null, null, '2017-06-20 09:50:35', null, '2017-06-20 09:50:42', '2017-06-20 09:50:42', '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `manager_user_action_history`
-- ----------------------------
DROP TABLE IF EXISTS `manager_user_action_history`;
CREATE TABLE `manager_user_action_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '后台管理用户ID',
  `user_real_name` varchar(128) DEFAULT NULL COMMENT '后台管理用户真实姓名',
  `action_level` varchar(128) DEFAULT NULL COMMENT '操作级别(FATAL_1 : 致命,能影响到应用 , ERROR_2 : 错误,会影响正常功能, WARN_3 : 日常警告 ,INFO_4 : 日常记录)',
  `action_type` varchar(12) DEFAULT NULL COMMENT '操作类型',
  `action_log` varchar(1024) DEFAULT NULL COMMENT '操作日志(也用于可以存储异常栈信息,或者运行的sql)',
  `action_ip_address` varchar(64) DEFAULT NULL COMMENT '操作ip地址',
  `action_description` varchar(128) DEFAULT NULL COMMENT '操作描述',
  `is_warn` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否警报(注意【强制】POJO 类的 Boolean 属性不能加 is，而数据库字段必须加 is_，要求在 resultMap 中 进行字段与属性之间的映射。)',
  `action_start_time` datetime DEFAULT NULL COMMENT '动作开始时间',
  `action_end_time` datetime DEFAULT NULL COMMENT '动作结束时间',
  `action_total_time` datetime DEFAULT NULL COMMENT '总执行时间',
  `action_class` varchar(128) DEFAULT NULL COMMENT '操作类',
  `action_method` varchar(128) DEFAULT NULL COMMENT '操作方法',
  `action_args` varchar(2048) DEFAULT NULL COMMENT '方法参数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=256 DEFAULT CHARSET=utf8 COMMENT='后台管理用户历史记录操作表(MYISAM引擎)';

-- ----------------------------
--  Table structure for `manager_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `manager_user_role`;
CREATE TABLE `manager_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '后台管理用户_id',
  `role_id` bigint(20) NOT NULL COMMENT '后台管理角色_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='后台管理用户角色中间表';

-- ----------------------------
--  Records of `manager_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `manager_user_role` VALUES ('1', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `system_config`
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_key` varchar(64) NOT NULL COMMENT 'key',
  `config_value` varchar(1024) NOT NULL COMMENT 'value',
  `config_description` varchar(256) DEFAULT NULL COMMENT '说明',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='系统配置表(MYISAM引擎)';

SET FOREIGN_KEY_CHECKS = 1;
