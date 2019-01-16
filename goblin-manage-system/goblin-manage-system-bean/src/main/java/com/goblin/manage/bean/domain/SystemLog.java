package com.goblin.manage.bean.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.goblin.manage.bean.domain.enums.NoticeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-29
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "system_log" )
public class SystemLog extends Model< SystemLog > {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Long       id;
    /**
     * 后台管理用户ID
     */
    @TableField( "user_id" )
    private Long       userId;
    /**
     * 后台管理用户真实姓名
     */
    @TableField( "user_real_name" )
    private String     userRealName;
    /**
     * 操作日志(也用于可以存储异常栈信息,或者运行的sql)
     */
    @TableField( "action_log" )
    private String     actionLog;
    /**
     * 操作ip地址
     */
    @TableField( "action_ip_address" )
    private String     actionIpAddress;
    /**
     * 操作描述
     */
    @TableField( "action_description" )
    private String     actionDescription;
    /**
     * 动作开始时间
     */
    @TableField( "action_start_time" )
    private Date       actionStartTime;
    /**
     * 动作结束时间
     */
    @TableField( "action_end_time" )
    private Date       actionEndTime;
    /**
     * 总执行时间(微秒)
     */
    @TableField( "action_total_time" )
    private Long       actionTotalTime;
    /**
     * 操作类
     */
    @TableField( "action_class" )
    private String     actionClass;
    /**
     * 操作方法
     */
    @TableField( "action_method" )
    private String     actionMethod;
    /**
     * 方法参数
     */
    @TableField( "action_args" )
    private String     actionArgs;
    /**
     * 是否异常
     */
    @TableField( "is_exception" )
    private Boolean    exception;
    /**
     * 异常是否警报
     */
    @TableField( "is_exception_warn" )
    private Boolean    exceptionWarn;
    /**
     * 通知类型(SMS:短信,MAIL:邮箱)
     */
    @TableField( "notice_type" )
    private NoticeType noticeType;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 修改时间
     */
    @TableField( "update_time" )
    private Date       updateTime;


    @Override
    protected Serializable pkVal () {
        return this.id;
    }

}
