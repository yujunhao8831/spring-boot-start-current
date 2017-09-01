/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost
 Source Database       : aidijing

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : utf-8

 Date: 09/01/2017 15:14:58 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `manage_mail_subscribe`
-- ----------------------------
DROP TABLE IF EXISTS `manage_mail_subscribe`;
CREATE TABLE `manage_mail_subscribe` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
   `to_email` varchar(128) NOT NULL COMMENT '收件箱',
   `to_email_type` varchar(18) NOT NULL DEFAULT 'SYSTEM' COMMENT '收件箱类型\n            (\n            SYSTEM:系统邮箱,OTHER:其他邮箱[非系统邮箱]\n            ),默认为 : SYSTEM',
   `email_subject` varchar(256) NOT NULL COMMENT '邮件主题',
   `email_text` text COMMENT '邮件内容',
   `email_attachment_url` varchar(1024) DEFAULT NULL COMMENT '邮件附件地址(多个用逗号'',''分隔)',
   `email_send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '邮件发送时间(默认:即刻发送)',
   `email_send_state` varchar(18) NOT NULL DEFAULT 'NOT_SEND' COMMENT '发送状态\n            (\n            NOT_SEND : 未发送,\n            SEND : 已发送,\n            FAIL_SEND : 发送失败,\n            FINAL_FAIL_SEND : 重试次数用完后,还是发送失败\n            )',
   `email_send_retry_number` tinyint(4) NOT NULL DEFAULT '3' COMMENT '失败重试次数',
   `email_send_retry_count` tinyint(4) NOT NULL DEFAULT '0' COMMENT '重试发送统计',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邮件订阅';

-- ----------------------------
--  Table structure for `manage_permission_resource`
-- ----------------------------
DROP TABLE IF EXISTS `manage_permission_resource`;
CREATE TABLE `manage_permission_resource` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父权限资源ID(0:表示root级)',
   `resource_depth` int(11) DEFAULT NULL COMMENT '资源深度',
   `permission_sort` int(11) DEFAULT NULL COMMENT '排序字段',
   `permission_name` varchar(128) NOT NULL COMMENT '权限名称',
   `resource_class` varchar(128) DEFAULT NULL COMMENT '资源样式class(前端class属性)',
   `resource_style` varchar(128) DEFAULT NULL COMMENT '资源样式style(前端style属性)',
   `resource_router_url` varchar(128) DEFAULT NULL COMMENT '资源路由URL(前端使用)',
   `resource_type` varchar(8) NOT NULL COMMENT '资源类型(API:接口,MENU:菜单,BUTTON:按钮)',
   `resource_api_uri` varchar(128) DEFAULT NULL COMMENT '资源API URI(非必须,api才有)',
   `resource_api_uri_methods` varchar(128) DEFAULT NULL COMMENT '资源API URI方法methods(GET,POST,DELETE,PUT,以'',''分割)',
   `resource_api_uri_options_fields` varchar(512) DEFAULT NULL COMMENT '资源API URI 显示字段列表\n            提供选择以'',''逗号分隔',
   `resource_api_protected_type` varchar(16) DEFAULT 'GROUP_ADMIN' COMMENT '资源API 保护方式(未确定)\n            GROUP_USER  : 组成员可见,及以上管理员可见\n            GROUP_ADMIN : 组管理员,及以上管理员可见\n            SUPER_ADMIN : 超级管理员,及以上管理员可见\n            ROOT : 仅ROOT可见\n            CUSTOM : 自定义(该接口保护方式在代码硬编码决定)\n            \n            默认 : GROUP_ADMIN',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   `category_code` varchar(8) DEFAULT NULL COMMENT '分类(C,R,U,D)冗余字段',
   `is_enabled` tinyint(1) DEFAULT '1' COMMENT '资源状态',
   `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
   `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='后台管理权限资源表';

-- ----------------------------
--  Records of `manage_permission_resource`
-- ----------------------------
BEGIN;
INSERT INTO `manage_permission_resource` VALUES ('25', '0', null, null, '/authentication', null, null, null, 'API', '/authentication', 'POST', null, 'GROUP_ADMIN', '2017-09-01 15:14:04', '2017-09-01 15:14:04', null, null, '1', null, null), ('26', '0', null, null, '/authentication', null, null, null, 'API', '/authentication', 'PUT', null, 'GROUP_ADMIN', '2017-09-01 15:14:04', '2017-09-01 15:14:04', null, null, '1', null, null), ('27', '0', null, null, '/demo', null, null, null, 'API', '/demo', 'GET', null, 'GROUP_ADMIN', '2017-09-01 15:14:04', '2017-09-01 15:14:04', null, null, '1', null, null), ('28', '0', null, null, '/injection', null, null, null, 'API', '/injection', 'GET', null, 'GROUP_ADMIN', '2017-09-01 15:14:04', '2017-09-01 15:14:04', null, null, '1', null, null), ('29', '0', null, null, '/error', null, null, null, 'API', '/error', 'GET', null, 'GROUP_ADMIN', '2017-09-01 15:14:04', '2017-09-01 15:14:04', null, null, '1', null, null), ('30', '0', null, null, '/error', null, null, null, 'API', '/error', 'GET', null, 'GROUP_ADMIN', '2017-09-01 15:14:04', '2017-09-01 15:14:04', null, null, '1', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `manage_role`
-- ----------------------------
DROP TABLE IF EXISTS `manage_role`;
CREATE TABLE `manage_role` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `role_name` varchar(32) NOT NULL COMMENT '角色名称',
   `role_name_code` varchar(64) NOT NULL COMMENT '角色名称code',
   `role_type` varchar(32) NOT NULL DEFAULT 'USER' COMMENT '角色类型(ROOT:根,SUPER_ADMIN:超级管理员,ADMIN:管理员,USER:普通用户)',
   `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '角色状态(1:激活,0:锁定)',
   `description` varchar(128) DEFAULT NULL COMMENT '描述',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   PRIMARY KEY (`id`),
   UNIQUE KEY `AK_manage_role_role_name_code_uk` (`role_name_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='后台管理角色表';

-- ----------------------------
--  Records of `manage_role`
-- ----------------------------
BEGIN;
INSERT INTO `manage_role` VALUES ('3', '普罗米修斯', 'ROOT', 'ROOT', '1', null, '2017-09-01 15:14:04', '2017-09-01 15:14:04', null);
COMMIT;

-- ----------------------------
--  Table structure for `manage_role_permission_resource`
-- ----------------------------
DROP TABLE IF EXISTS `manage_role_permission_resource`;
CREATE TABLE `manage_role_permission_resource` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `role_id` bigint(20) NOT NULL COMMENT '后台管理角色_id',
   `permission_resource_id` bigint(20) NOT NULL COMMENT '后台管理权限资源_id',
   `resource_api_uri_show_fields` varchar(512) NOT NULL DEFAULT '*' COMMENT '资源API URI 显示字段\n            (\n            "*":表示显示所有[默认"*"].\n            "-" + 字段名,表示排除某个字段.如果要排除多个以","进行分隔,比如: -username,-password.\n            字段名,表示只显示某个字段,如果只要显示某几个字段可以用","分隔,比如:username,password.\n            示例 : \n            * : 显示所有字段\n            -username,-password : 除了不显示username,password这2个字段,其他字段都显示\n            username,password : 只显示username,password这2个字段,都不显示\n            )\n            目前只是精确到角色,具体到用户,还需要后续思考',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='后台管理角色资源中间表';

-- ----------------------------
--  Records of `manage_role_permission_resource`
-- ----------------------------
BEGIN;
INSERT INTO `manage_role_permission_resource` VALUES ('13', '3', '25', '*'), ('14', '3', '26', '*'), ('15', '3', '27', '*'), ('16', '3', '28', '*'), ('17', '3', '29', '*'), ('18', '3', '30', '*');
COMMIT;

-- ----------------------------
--  Table structure for `manage_sms_subscribe`
-- ----------------------------
DROP TABLE IF EXISTS `manage_sms_subscribe`;
CREATE TABLE `manage_sms_subscribe` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
   `sms_phone` varchar(128) NOT NULL COMMENT '手机号码',
   `mobile_operators` varchar(32) NOT NULL DEFAULT 'UNDEFINED' COMMENT '运营商(CHINA_UNICOM:中国联通,CHINA_TELICOM:中国电信,CHINA_MOBILE:中国移动,UNDEFINED:未指定)',
   `sms_content` varchar(256) NOT NULL COMMENT '短信内容',
   `sms_send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '短信发送时间(默认:即刻发送)',
   `sms_send_state` varchar(18) NOT NULL DEFAULT 'NOT_SEND' COMMENT '发送状态\n            (\n            NOT_SEND : 未发送,\n            SEND : 已发送,\n            FAIL_SEND : 发送失败,\n            FINAL_FAIL_SEND : 重试次数用完后,还是发送失败\n            )',
   `sms_send_retry_number` tinyint(4) NOT NULL DEFAULT '3' COMMENT '失败重试次数',
   `sms_send_retry_count` tinyint(4) NOT NULL DEFAULT '0' COMMENT '重试发送统计',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信订阅表';

-- ----------------------------
--  Table structure for `manage_user`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user`;
CREATE TABLE `manage_user` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `username` varchar(32) NOT NULL COMMENT '用户名(登录名称)',
   `password` varchar(128) NOT NULL COMMENT '密码',
   `password_salt` varchar(32) DEFAULT '-1' COMMENT '盐(目前未用到,目前使用全局的)',
   `nick_name` varchar(128) DEFAULT NULL COMMENT '昵称',
   `real_name` varchar(128) DEFAULT NULL COMMENT '真实姓名',
   `email` varchar(128) DEFAULT NULL COMMENT '电子邮箱',
   `phone` varchar(18) DEFAULT NULL COMMENT '手机号码',
   `user_image_url` varchar(128) DEFAULT NULL COMMENT '用户头像',
   `last_password_reset_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '密码最后重置(修改)日期',
   `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
   `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '账户状态(1:激活,0:锁定)',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   PRIMARY KEY (`id`),
   UNIQUE KEY `AK_manage_user_username_uk` (`username`),
   UNIQUE KEY `manage_admin_user_username_uk_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='后台管理用户';

-- ----------------------------
--  Records of `manage_user`
-- ----------------------------
BEGIN;
INSERT INTO `manage_user` VALUES ('5', 'admin', '$2a$10$PCraATW//vUUYWZLpvqoQukDDAjlDfsuwsZfhAsqEi9gEp94s05DO', '-1', null, null, null, null, null, '2017-09-01 15:14:04', null, null, '2017-09-01 15:14:04', '2017-09-01 15:14:04', '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `manage_user_action_history`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user_action_history`;
CREATE TABLE `manage_user_action_history` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` bigint(20) NOT NULL COMMENT '后台管理用户ID',
   `user_real_name` varchar(128) DEFAULT NULL COMMENT '后台管理用户真实姓名',
   `action_level` varchar(128) DEFAULT NULL COMMENT '操作级别(FATAL_1 : 致命,能影响到应用 , ERROR_2 : 错误,会影响正常功能, WARN_3 : 日常警告 ,INFO_4 : 日常记录)',
   `action_type` varchar(12) DEFAULT NULL COMMENT '操作类型',
   `action_log` varchar(1024) DEFAULT NULL COMMENT '操作日志(也用于可以存储异常栈信息,或者运行的sql)',
   `action_ip_address` varchar(64) DEFAULT NULL COMMENT '操作ip地址',
   `action_description` varchar(128) DEFAULT NULL COMMENT '操作描述',
   `action_start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '动作开始时间',
   `action_end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '动作结束时间',
   `action_total_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '总执行时间',
   `action_class` varchar(128) DEFAULT NULL COMMENT '操作类',
   `action_method` varchar(128) DEFAULT NULL COMMENT '操作方法',
   `action_args` varchar(2048) DEFAULT NULL COMMENT '方法参数',
   `is_warn` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否警报(注意【强制】POJO 类的 Boolean 属性不能加 is，而数据库字段必须加 is_，要求在 resultMap 中 进行字段与属性之间的映射。)',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理用户历史记录操作表';

-- ----------------------------
--  Table structure for `manage_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `manage_user_role`;
CREATE TABLE `manage_user_role` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `user_id` bigint(20) NOT NULL COMMENT '后台管理用户_id',
   `role_id` bigint(20) NOT NULL COMMENT '后台管理角色_id',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='后台管理用户角色中间表';

-- ----------------------------
--  Records of `manage_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `manage_user_role` VALUES ('3', '5', '3');
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
   `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
   `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `remark` varchar(128) DEFAULT NULL COMMENT '备注',
   PRIMARY KEY (`id`),
   UNIQUE KEY `AK_system_config_config_key_uk` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

SET FOREIGN_KEY_CHECKS = 1;
