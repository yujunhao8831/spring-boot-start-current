package com.goblin.response;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 后台管理用户
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-07-12
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
public class User {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
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
	 * 盐(目前未用到,目前使用全局的)
	 */
	private String  passwordSalt;
	/**
	 * 昵称
	 */
	private String  nickName;
	/**
	 * 真实姓名
	 */
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
	private String  userImageUrl;
	/**
	 * 密码最后重置(修改)日期
	 */
	private Date    lastPasswordResetDate;
	/**
	 * 创建人
	 */
	private Long    createUserId;
	/**
	 * 修改人
	 */
	private Long    updateUserId;
	/**
	 * 创建时间
	 */
	private Date    createTime;
	/**
	 * 修改时间
	 */
	private Date    updateTime;
	/**
	 * 账户状态(1:激活,0:锁定)
	 */
	private Boolean enabled;
	/**
	 * 备注
	 */
	private String  remark;

}
