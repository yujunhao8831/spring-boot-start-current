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
 * 后台管理角色表
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-28
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "manage_role" )
public class Role extends Model< Role > {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId( value = "id", type = IdType.AUTO )
	private Long    id;
	/**
	 * 角色名称
	 */
	@TableField( "role_name" )
	private String  roleName;
	/**
	 * 角色名称code
	 */
	@TableField( "role_name_code" )
	private String  roleNameCode;
	/**
	 * 角色状态(1:激活,0:锁定)
	 */
	@TableField( "is_enabled" )
	private Boolean enabled;
	/**
	 * 描述
	 */
	private String  description;
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
	 * 备注
	 */
	private String  remark;


	@Override
	protected Serializable pkVal () {
		return this.id;
	}

}
