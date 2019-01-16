package com.goblin.manage.bean.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 修改时间
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-28
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "manage_mail_subscribe" )
public class MailSubscribe extends Model< MailSubscribe > {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId( value = "id", type = IdType.AUTO )
	private Long    id;
	/**
	 * 用户ID
	 */
	@TableField( "user_id" )
	private Long    userId;
	/**
	 * 收件箱
	 */
	@TableField( "to_email" )
	private String  toEmail;
	/**
	 * 收件箱类型(SYSTEM:系统邮箱,OTHER:其他邮箱[非系统邮箱]),
	 * 默认为 : SYSTEM
	 */
	@TableField( "to_email_type" )
	private String  toEmailType;
	/**
	 * 邮件主题
	 */
	@TableField( "email_subject" )
	private String  emailSubject;
	/**
	 * 邮件内容
	 */
	@TableField( "email_text" )
	private String  emailText;
	/**
	 * 邮件附件地址(多个用逗号','分隔)
	 */
	@TableField( "email_attachment_url" )
	private String  emailAttachmentUrl;
	/**
	 * 邮件发送时间(默认:即刻发送)
	 */
	@TableField( "email_send_time" )
	private Date    emailSendTime;
	/**
	 * 发送状态(  NOT_SEND : 未发送,SEND : 已发送,FAIL_SEND : 发送失败,
	 * FINAL_FAIL_SEND : 重试次数用完后,还是发送失败    )
	 */
	@TableField( "email_send_state" )
	private String  emailSendState;
	/**
	 * 失败重试次数
	 */
	@TableField( "email_send_retry_number" )
	private Boolean emailSendRetryNumber;
	/**
	 * 重试发送统计
	 */
	@TableField( "email_send_retry_count" )
	private Boolean emailSendRetryCount;
	/**
	 * 备注
	 */
	private String  remark;
	/**
	 * 创建时间
	 */
	@TableField( "create_time" )
	private Date    createTime;
	/**
	 * 修改时间
	 */
	@TableField( "update_time" )
	private Date    updateTime;


	@Override
	protected Serializable pkVal () {
		return this.id;
	}

}
