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
 * 用户
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-28
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "manage_user" )
public class User extends Model< User > {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId( value = "id", type = IdType.AUTO )
	private Long    id;
	/**
	 * 用户名(登录名称)
	 */
	private String  username;
	/**
	 * 密码
	 */
	private String  password;
	/**
	 * 盐
	 */
	@TableField( "password_salt" )
	private String  passwordSalt;
	/**
	 * 昵称
	 */
	@TableField( "nick_name" )
	private String  nickName;
	/**
	 * 真实姓名
	 */
	@TableField( "real_name" )
	private String  realName;
	/**
	 * 电子邮箱
	 */
	private String  email;
	/**
	 * 手机号码
	 */
	private String  phone;
	/**
	 * 用户头像
	 */
	@TableField( "user_image_url" )
	private String  userImageUrl;
	/**
	 * 密码最后重置(修改)日期
	 */
	@TableField( "last_password_reset_date" )
	private Date    lastPasswordResetDate;
	/**
	 * 创建人
	 */
	@TableField( "create_user_id" )
	private Long    createUserId;
	/**
	 * 修改人
	 */
	@TableField( "update_user_id" )
	private Long    updateUserId;
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
	/**
	 * 账户状态(1:激活,0:锁定)
	 */
	@TableField( "is_enabled" )
	private Boolean enabled;
	/**
	 * 备注
	 */
	private String  remark;


	@Override
	protected Serializable pkVal () {
		return this.id;
	}

}
