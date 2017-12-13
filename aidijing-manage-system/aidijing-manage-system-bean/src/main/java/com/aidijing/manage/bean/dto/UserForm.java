package com.aidijing.manage.bean.dto;

import com.aidijing.common.util.ValidatedGroups;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户表单
 *
 * @author : 披荆斩棘
 * @date : 2017/7/5
 */
@Data
@Accessors( chain = true )
public class UserForm implements Serializable {

	@NotNull( message = "id不能为空", groups = ValidatedGroups.Update.class )
	private Long    id;
	/**
	 * 用户名(登录名称)
	 */
	@NotEmpty( message = "username不能为空",groups = ValidatedGroups.Save.class)
	@Size( min = 3, max = 32, message = "username长度必须在{min}和{max}之间" )
	private String  username;
	/**
	 * 密码
	 */
	@NotEmpty( message = "password不能为空" )
	@Length( min = 6, message = "password长度不能小于6位" )
	private String  password;
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
	@Email( message = "email格式不正确" )
	private String  email;
	/**
	 * 手机号码
	 */
	@Pattern( regexp = "^(1[0-9])\\d{9}$", message = "手机号格式不正确" )
	private String  phone;
	/**
	 * 用户头像
	 */
	private String  userImageUrl;
	/**
	 * 创建人
	 */
	private Long    createManagerAdminUserId;
	/**
	 * 账户状态(1:激活,0:锁定)
	 */
	private Boolean enabled;
	/**
	 * 备注
	 */
	private String  remark;


}
