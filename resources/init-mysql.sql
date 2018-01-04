drop table if exists manage_mail_subscribe;

drop table if exists manage_permission_resource;

drop table if exists manage_role;

drop table if exists manage_role_permission_resource;

drop table if exists manage_sms_subscribe;

drop table if exists manage_user;

drop table if exists manage_user_role;

drop table if exists system_config;

drop table if exists system_log;

/*==============================================================*/
/* Table: manage_mail_subscribe                                 */
/*==============================================================*/
create table manage_mail_subscribe
(
  id                   bigint(20) not null auto_increment comment '主键',
  user_id              bigint(20) default NULL comment '用户ID',
  to_email             varchar(128) not null comment '收件箱',
  to_email_type        varchar(18) not null default 'SYSTEM' comment '收件箱类型(SYSTEM:系统邮箱,OTHER:其他邮箱[非系统邮箱]),
            默认为 : SYSTEM',
  email_subject        varchar(256) not null comment '邮件主题',
  email_text           text comment '邮件内容',
  email_attachment_url varchar(1024) default NULL comment '邮件附件地址(多个用逗号'',''分隔)',
  email_send_time      timestamp not null default CURRENT_TIMESTAMP comment '邮件发送时间(默认:即刻发送)',
  email_send_state     varchar(18) not null default 'NOT_SEND' comment '发送状态(  NOT_SEND : 未发送,SEND : 已发送,FAIL_SEND : 发送失败,
                   FINAL_FAIL_SEND : 重试次数用完后,还是发送失败    )',
  email_send_retry_number tinyint(4) not null default 3 comment '失败重试次数',
  email_send_retry_count tinyint(4) not null default 0 comment '重试发送统计',
  remark               varchar(128) default NULL comment '备注',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  primary key (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邮件订阅';

alter table manage_mail_subscribe comment '修改时间';

/*==============================================================*/
/* Table: manage_permission_resource                            */
/*==============================================================*/
create table manage_permission_resource
(
  id                   bigint(20) not null auto_increment comment '主键',
  parent_id            bigint(20) not null default 0 comment '父权限资源ID(0:表示root级)',
  resource_depth       int(11) default NULL comment '资源深度',
  permission_sort      int(11) default NULL comment '排序字段',
  permission_name      varchar(128) not null comment '权限名称',
  resource_class       varchar(128) default NULL comment '资源样式class(前端class属性)',
  resource_style       varchar(128) default NULL comment '资源样式style(前端style属性)',
  resource_router_url  varchar(128) default NULL comment '资源路由URL(前端使用)',
  resource_type        varchar(8) not null comment '资源类型(API:接口,MENU:菜单,BUTTON:按钮)',
  resource_api_uri     varchar(128) default NULL comment '资源API URI(非必须,api才有)',
  resource_api_uri_methods varchar(128) default NULL comment '资源API URI方法methods(GET,POST,DELETE,PUT,以'',''分割)',
  resource_api_uri_options_fields varchar(512) default NULL comment '资源API URI 显示字段列表           提供选择以'',''逗号分隔',
  is_enabled           tinyint(1) default 1 comment '资源状态',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  create_user_id       bigint(20) default NULL comment '创建人',
  update_user_id       bigint(20) default NULL comment '修改人',
  remark               varchar(128) default NULL comment '备注',
  primary key (id)
)
  ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='后台管理权限资源表';

alter table manage_permission_resource comment '权限资源';

/*==============================================================*/
/* Table: manage_role                                           */
/*==============================================================*/
create table manage_role
(
  id                   bigint(20) not null auto_increment comment '主键',
  role_name            varchar(32) not null comment '角色名称',
  role_name_code       varchar(64) not null comment '角色名称code',
  is_enabled           tinyint(1) not null default 1 comment '角色状态(1:激活,0:锁定)',
  description          varchar(128) default NULL comment '描述',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  remark               varchar(128) default NULL comment '备注',
  primary key (id),
  unique key AK_manage_role_role_name_code_uk (role_name_code)
)
  ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='后台管理角色表';

/*==============================================================*/
/* Table: manage_role_permission_resource                       */
/*==============================================================*/
create table manage_role_permission_resource
(
  id                   bigint(20) not null auto_increment comment '主键',
  role_id              bigint(20) not null comment '后台管理角色_id',
  permission_resource_id bigint(20) not null comment '后台管理权限资源_id',
  resource_api_uri_show_fields varchar(512) not null default '*' comment '资源API URI 显示字段
                        (
                        "*":表示显示所有[默认"*"].
                        "-" + 字段名,表示排除某个字段.如果要排除多个以","进行分隔,比如: -username,-password.
                        字段名,表示只显示某个字段,如果只要显示某几个字段可以用","分隔,比如:username,password.
                        示例 : 
                        * : 显示所有字段
                        -username,-password : 除了不显示username,password这2个字段,其他字段都显示
                        username,password : 只显示username,password这2个字段,都不显示
                        )
                        目前只是精确到角色,具体到用户,还需要后续思考',
  primary key (id)
)
  ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='后台管理角色资源中间表';

alter table manage_role_permission_resource comment '角色和权限资源中间表';

/*==============================================================*/
/* Table: manage_sms_subscribe                                  */
/*==============================================================*/
create table manage_sms_subscribe
(
  id                   bigint(20) not null auto_increment comment '主键',
  user_id              bigint(20) default NULL comment '用户ID',
  sms_phone            varchar(128) not null comment '手机号码',
  mobile_operators     varchar(32) not null default 'UNDEFINED' comment '运营商(CHINA_UNICOM:中国联通,CHINA_TELICOM:中国电信,CHINA_MOBILE:中国移动,UNDEFINED:未指定)',
  sms_content          varchar(256) not null comment '短信内容',
  sms_send_time        timestamp not null default CURRENT_TIMESTAMP comment '短信发送时间(默认:即刻发送)',
  sms_send_state       varchar(18) not null default 'NOT_SEND' comment '发送状态 
            ( NOT_SEND : 未发送,SEND : 已发送,
            FAIL_SEND : 发送失败,FINAL_FAIL_SEND : 重试次数用完后,还是发送失败 )',
  sms_send_retry_number tinyint(4) not null default 3 comment '失败重试次数',
  sms_send_retry_count tinyint(4) not null default 0 comment '重试发送统计',
  remark               varchar(128) default NULL comment '备注',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  primary key (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信订阅表';

alter table manage_sms_subscribe comment '短信订阅';

/*==============================================================*/
/* Table: manage_user                                           */
/*==============================================================*/
create table manage_user
(
  id                   bigint(20) not null auto_increment comment '主键',
  username             varchar(32) not null comment '用户名(登录名称)',
  password             varchar(128) not null comment '密码',
  password_salt        varchar(32) default '-1' comment '盐',
  nick_name            varchar(128) default NULL comment '昵称',
  real_name            varchar(128) default NULL comment '真实姓名',
  email                varchar(128) default NULL comment '电子邮箱',
  phone                varchar(18) default NULL comment '手机号码',
  user_image_url       varchar(128) default NULL comment '用户头像',
  last_password_reset_date timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '密码最后重置(修改)日期',
  create_user_id       bigint(20) default NULL comment '创建人',
  update_user_id       bigint(20) default NULL comment '修改人',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  is_enabled           tinyint(1) not null default 1 comment '账户状态(1:激活,0:锁定)',
  remark               varchar(128) default NULL comment '备注',
  primary key (id),
  unique key AK_manage_user_username_uk (username)
)
  ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='后台管理用户';

alter table manage_user comment '用户';

/*==============================================================*/
/* Table: manage_user_role                                      */
/*==============================================================*/
create table manage_user_role
(
  id                   bigint(20) not null auto_increment comment '主键',
  user_id              bigint(20) not null comment '后台管理用户_id',
  role_id              bigint(20) not null comment '后台管理角色_id',
  primary key (id)
)
  ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='后台管理用户角色中间表';

alter table manage_user_role comment '用户和角色中间表';

/*==============================================================*/
/* Table: system_config                                         */
/*==============================================================*/
create table system_config
(
  id                   bigint(20) not null auto_increment comment '主键',
  config_key           varchar(64) not null comment 'key',
  config_value         varchar(1024) not null comment 'value',
  config_description   varchar(256) default NULL comment '说明',
  create_user_id       bigint(20) default NULL comment '创建人',
  update_user_id       bigint(20) default NULL comment '修改人',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  remark               varchar(128) default NULL comment '备注',
  primary key (id),
  unique key AK_system_config_config_key_uk (config_key)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

alter table system_config comment '系统配置';

/*==============================================================*/
/* Table: system_log                                            */
/*==============================================================*/
create table system_log
(
  id                   bigint(20) not null auto_increment comment '主键',
  user_id              bigint(20) comment '后台管理用户ID',
  user_real_name       varchar(128) default NULL comment '后台管理用户真实姓名',
  action_log           text default NULL comment '操作日志(也用于可以存储异常栈信息,或者运行的sql) json',
  action_ip_address    varchar(64) default NULL comment '操作ip地址',
  action_description   varchar(128) default NULL comment '操作描述',
  action_start_time    timestamp not null default CURRENT_TIMESTAMP comment '动作开始时间',
  action_end_time      timestamp not null default CURRENT_TIMESTAMP comment '动作结束时间',
  action_total_time    bigint not null comment '总执行时间(微秒)',
  action_class         varchar(128) default NULL comment '操作类',
  action_method        varchar(128) default NULL comment '操作方法',
  action_args          varchar(2048) default NULL comment '方法参数',
  is_exception         tinyint(1) not null default 0 comment '是否异常',
  is_exception_warn    tinyint(1) not null default 0 comment '异常是否警报',
  notice_type          varchar(18) not null default 'MAIL' comment '通知类型(SMS:短信,MAIL:邮箱)',
  create_time          timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
  update_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
  primary key (id)
)
  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理用户历史记录操作表';

alter table system_log comment '系统日志表';
