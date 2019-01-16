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

/**
 * <p>
 * 角色和权限资源中间表
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-28
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
@TableName( "manage_role_permission_resource" )
public class RolePermissionResource extends Model< RolePermissionResource > {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId( value = "id", type = IdType.AUTO )
	private Long   id;
	/**
	 * 后台管理角色_id
	 */
	@TableField( "role_id" )
	private Long   roleId;
	/**
	 * 后台管理权限资源_id
	 */
	@TableField( "permission_resource_id" )
	private Long   permissionResourceId;
	/**
	 * 资源API URI 显示字段
	 * (
	 * "*":表示显示所有[默认"*"].
	 * "-" + 字段名,表示排除某个字段.如果要排除多个以","进行分隔,比如: -username,-password.
	 * 字段名,表示只显示某个字段,如果只要显示某几个字段可以用","分隔,比如:username,password.
	 * 示例 :
	 * : 显示所有字段
	 * -username,-password : 除了不显示username,password这2个字段,其他字段都显示
	 * username,password : 只显示username,password这2个字段,都不显示
	 * )
	 * 目前只是精确到角色,具体到用户,还需要后续思考
	 */
	@TableField( "resource_api_uri_show_fields" )
	private String resourceApiUriShowFields;


	@Override
	protected Serializable pkVal () {
		return this.id;
	}

}
