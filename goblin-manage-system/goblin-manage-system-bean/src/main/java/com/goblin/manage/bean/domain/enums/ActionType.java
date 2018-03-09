package com.goblin.manage.bean.domain.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;

/**
 * 操作类型(USER:用户操作,SYSTEM:系统操作[类似调度触发的])
 * @author : 披荆斩棘
 * @date : 2017/12/29
 * 对应字段 {@link com.goblin.manage.bean.domain.SystemLog#actionType}
 */
@Getter
public enum  ActionType implements IEnum {
	USER( "USER" , "用户操作" ),
	SYSTEM( "SYSTEM" , "系统操作[类似调度触发的]" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	ActionType ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}
}
