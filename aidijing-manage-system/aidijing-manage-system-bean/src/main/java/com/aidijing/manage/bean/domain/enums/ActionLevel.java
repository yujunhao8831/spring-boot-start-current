package com.aidijing.manage.bean.domain.enums;

import com.aidijing.manage.bean.domain.SystemLog;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Getter;

/**
 * 操作级别(FATAL_1 : 致命,能影响到应用 , ERROR_2 : 错误,会影响正常功能, WARN_3 : 日常警告 ,INFO_4 : 日常记录)
 * <p>
 * 对应字段 {@link SystemLog#actionLevel}
 */
@Getter
public enum ActionLevel implements IEnum {
	FATAL( "FATAL_1" , "致命,能影响到应用" ),
	ERROR( "ERROR_2" , "错误,会影响正常功能" ),
	WARN( "WARN_3" , "日常警告" ),
	INFO( "INFO_4" , "日常记录" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	ActionLevel ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}
}
