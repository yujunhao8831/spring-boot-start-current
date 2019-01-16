package com.goblin.manage.bean.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.goblin.manage.bean.domain.PermissionResource;
import lombok.Getter;

/**
 * 资源类型(API:接口,MENU:菜单,BUTTON:按钮)
 * <p>
 * 对应字段 {@link PermissionResource#getResourceType()}
 */
@Getter
public enum ResourceType implements IEnum {

	/**
	 *
	 */
	API( "API" , "接口" ),
	MENU( "MENU" , "菜单" ),
	BUTTON( "BUTTON" , "按钮" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	ResourceType ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}

	/**
	 * {@link JsonValue} 指定序列化为该字段,也就是显示时以该字段显示
	 */
	@JsonValue
	public String getComment () {
		return comment;
	}
}
