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
 * 短信订阅
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-28
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "manage_sms_subscribe" )
public class SmsSubscribe extends Model< SmsSubscribe > {

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
	 * 手机号码
	 */
	@TableField( "sms_phone" )
	private String  smsPhone;
	/**
	 * 运营商(CHINA_UNICOM:中国联通,CHINA_TELICOM:中国电信,CHINA_MOBILE:中国移动,UNDEFINED:未指定)
	 */
	@TableField( "mobile_operators" )
	private String  mobileOperators;
	/**
	 * 短信内容
	 */
	@TableField( "sms_content" )
	private String  smsContent;
	/**
	 * 短信发送时间(默认:即刻发送)
	 */
	@TableField( "sms_send_time" )
	private Date    smsSendTime;
	/**
	 * 发送状态
	 * ( NOT_SEND : 未发送,SEND : 已发送,
	 * FAIL_SEND : 发送失败,FINAL_FAIL_SEND : 重试次数用完后,还是发送失败 )
	 */
	@TableField( "sms_send_state" )
	private String  smsSendState;
	/**
	 * 失败重试次数
	 */
	@TableField( "sms_send_retry_number" )
	private Boolean smsSendRetryNumber;
	/**
	 * 重试发送统计
	 */
	@TableField( "sms_send_retry_count" )
	private Boolean smsSendRetryCount;
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
