package com.aidijing.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台管理用户
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Data
@Accessors(chain = true)
@TableName("manager_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private  Long  id;
    /**
     * 用户名(登录名称)
     */
	private  String  username;
    /**
     * 密码
     */
	private  String  password;
    /**
     * 昵称
     */
	@TableField("nick_name")
	private  String  nickName;
    /**
     * 真实姓名
     */
	@TableField("real_name")
	private  String  realName;
    /**
     * 电子邮箱
     */
	private  String  email;
    /**
     * 手机号码
     */
	private  String  phone;
    /**
     * 密码最后重置(修改)日期
     */
	@TableField("last_password_reset_date")
	private  Date  lastPasswordResetDate;
    /**
     * 创建人
     */
	@TableField("create_manager_admin_user_id")
	private  Long  createManagerAdminUserId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private  Date  createTime;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private  Date  updateTime;
    /**
     * 账户状态(1:激活,0:锁定)
     */
	@TableField("is_enabled")
	private  Boolean  enabled;
    /**
     * 备注
     */
	private  String  remark;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
