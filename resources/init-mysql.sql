CREATE DATABASE  IF NOT EXISTS `goblin` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `goblin`;
-- MySQL dump 10.13  Distrib 8.0.12, for macos10.13 (x86_64)
--
-- Host: 127.0.0.1    Database: goblin
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `manage_mail_subscribe`
--

DROP TABLE IF EXISTS `manage_mail_subscribe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `manage_mail_subscribe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `to_email` varchar(128) NOT NULL COMMENT '收件箱',
  `to_email_type` varchar(18) NOT NULL DEFAULT 'SYSTEM' COMMENT '收件箱类型(SYSTEM:系统邮箱,OTHER:其他邮箱[非系统邮箱]),\n            默认为 : SYSTEM',
  `email_subject` varchar(256) NOT NULL COMMENT '邮件主题',
  `email_text` text COMMENT '邮件内容',
  `email_attachment_url` varchar(1024) DEFAULT NULL COMMENT '邮件附件地址(多个用逗号'',''分隔)',
  `email_send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '邮件发送时间(默认:即刻发送)',
  `email_send_state` varchar(18) NOT NULL DEFAULT 'NOT_SEND' COMMENT '发送状态(  NOT_SEND : 未发送,SEND : 已发送,FAIL_SEND : 发送失败,\n                   FINAL_FAIL_SEND : 重试次数用完后,还是发送失败    )',
  `email_send_retry_number` tinyint(4) NOT NULL DEFAULT '3' COMMENT '失败重试次数',
  `email_send_retry_count` tinyint(4) NOT NULL DEFAULT '0' COMMENT '重试发送统计',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='修改时间';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_mail_subscribe`
--

LOCK TABLES `manage_mail_subscribe` WRITE;
/*!40000 ALTER TABLE `manage_mail_subscribe` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_mail_subscribe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_permission_resource`
--

DROP TABLE IF EXISTS `manage_permission_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
  `resource_api_uri_options_fields` varchar(512) DEFAULT NULL COMMENT '资源API URI 显示字段列表           提供选择以'',''逗号分隔',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '资源状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='权限资源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_permission_resource`
--

LOCK TABLES `manage_permission_resource` WRITE;
/*!40000 ALTER TABLE `manage_permission_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_permission_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_role`
--

DROP TABLE IF EXISTS `manage_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `manage_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(32) NOT NULL COMMENT '角色名称',
  `role_name_code` varchar(64) NOT NULL COMMENT '角色名称code',
  `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '角色状态(1:激活,0:锁定)',
  `description` varchar(128) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_manage_role_role_name_code_uk` (`role_name_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='后台管理角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_role`
--

LOCK TABLES `manage_role` WRITE;
/*!40000 ALTER TABLE `manage_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_role_permission_resource`
--

DROP TABLE IF EXISTS `manage_role_permission_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `manage_role_permission_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '后台管理角色_id',
  `permission_resource_id` bigint(20) NOT NULL COMMENT '后台管理权限资源_id',
  `resource_api_uri_show_fields` varchar(512) NOT NULL DEFAULT '*' COMMENT '资源API URI 显示字段\n                        (\n                        "*":表示显示所有[默认"*"].\n                        "-" + 字段名,表示排除某个字段.如果要排除多个以","进行分隔,比如: -username,-password.\n                        字段名,表示只显示某个字段,如果只要显示某几个字段可以用","分隔,比如:username,password.\n                        示例 : \n                        * : 显示所有字段\n                        -username,-password : 除了不显示username,password这2个字段,其他字段都显示\n                        username,password : 只显示username,password这2个字段,都不显示\n                        )\n                        目前只是精确到角色,具体到用户,还需要后续思考',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='角色和权限资源中间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_role_permission_resource`
--

LOCK TABLES `manage_role_permission_resource` WRITE;
/*!40000 ALTER TABLE `manage_role_permission_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_role_permission_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_sms_subscribe`
--

DROP TABLE IF EXISTS `manage_sms_subscribe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `manage_sms_subscribe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `sms_phone` varchar(128) NOT NULL COMMENT '手机号码',
  `mobile_operators` varchar(32) NOT NULL DEFAULT 'UNDEFINED' COMMENT '运营商(CHINA_UNICOM:中国联通,CHINA_TELICOM:中国电信,CHINA_MOBILE:中国移动,UNDEFINED:未指定)',
  `sms_content` varchar(256) NOT NULL COMMENT '短信内容',
  `sms_send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '短信发送时间(默认:即刻发送)',
  `sms_send_state` varchar(18) NOT NULL DEFAULT 'NOT_SEND' COMMENT '发送状态 \n            ( NOT_SEND : 未发送,SEND : 已发送,\n            FAIL_SEND : 发送失败,FINAL_FAIL_SEND : 重试次数用完后,还是发送失败 )',
  `sms_send_retry_number` tinyint(4) NOT NULL DEFAULT '3' COMMENT '失败重试次数',
  `sms_send_retry_count` tinyint(4) NOT NULL DEFAULT '0' COMMENT '重试发送统计',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信订阅';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_sms_subscribe`
--

LOCK TABLES `manage_sms_subscribe` WRITE;
/*!40000 ALTER TABLE `manage_sms_subscribe` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_sms_subscribe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_user`
--

DROP TABLE IF EXISTS `manage_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `manage_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) NOT NULL COMMENT '用户名(登录名称)',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `password_salt` varchar(32) DEFAULT '-1' COMMENT '盐',
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
  UNIQUE KEY `AK_manage_user_username_uk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_user`
--

LOCK TABLES `manage_user` WRITE;
/*!40000 ALTER TABLE `manage_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_user_role`
--

DROP TABLE IF EXISTS `manage_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `manage_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '后台管理用户_id',
  `role_id` bigint(20) NOT NULL COMMENT '后台管理角色_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户和角色中间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_user_role`
--

LOCK TABLES `manage_user_role` WRITE;
/*!40000 ALTER TABLE `manage_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `manage_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_log`
--

DROP TABLE IF EXISTS `system_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '后台管理用户ID',
  `user_real_name` varchar(128) DEFAULT NULL COMMENT '后台管理用户真实姓名',
  `action_log` text COMMENT '操作日志(也用于可以存储异常栈信息,或者运行的sql) json',
  `action_ip_address` varchar(64) DEFAULT NULL COMMENT '操作ip地址',
  `action_description` varchar(128) DEFAULT NULL COMMENT '操作描述',
  `action_start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '动作开始时间',
  `action_end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '动作结束时间',
  `action_total_time` bigint(20) NOT NULL COMMENT '总执行时间(微秒)',
  `action_class` varchar(128) DEFAULT NULL COMMENT '操作类',
  `action_method` varchar(128) DEFAULT NULL COMMENT '操作方法',
  `action_args` varchar(2048) DEFAULT NULL COMMENT '方法参数',
  `is_exception` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否异常',
  `is_exception_warn` tinyint(1) NOT NULL DEFAULT '0' COMMENT '异常是否警报',
  `notice_type` varchar(18) NOT NULL DEFAULT 'MAIL' COMMENT '通知类型(SMS:短信,MAIL:邮箱)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_log`
--

LOCK TABLES `system_log` WRITE;
/*!40000 ALTER TABLE `system_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-11  9:32:36
