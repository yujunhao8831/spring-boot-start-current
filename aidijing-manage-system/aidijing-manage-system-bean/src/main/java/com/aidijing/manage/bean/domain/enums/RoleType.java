package com.aidijing.manage.bean.domain.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;

/**
 * 角色类型(root:根,super_admin:超级管理员,admin:组管理员,user:普通用户)
 * <p>
 * 对应字段 {@link com.aidijing.manage.bean.domain.Role#roleType}
 */
@Getter
public enum RoleType implements IEnum {

	ROOT( "ROOT" , "根" ),
	SUPER_ADMIN( "SUPER_ADMIN" , "超级管理员" ),
	ADMIN( "ADMIN" , "组管理员" ),
	USER( "USER" , "普通用户" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	RoleType ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}
}
