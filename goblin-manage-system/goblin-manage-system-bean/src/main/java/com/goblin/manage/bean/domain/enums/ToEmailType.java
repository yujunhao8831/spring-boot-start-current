package com.goblin.manage.bean.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.Getter;

/**
 * 收件箱类型(SYSTEM:系统邮箱,OTHER:其他邮箱[非系统邮箱]),默认为 : SYSTEM
 *
 * @author : 披荆斩棘
 * @date : 2017/8/7
 */
@Getter
public enum ToEmailType implements IEnum {

	/**
	 *
	 */
	SYSTEM( "SYSTEM" , "系统邮箱" ),
	OTHER( "OTHER" , "其他邮箱[非系统邮箱])" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	ToEmailType ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}
}

