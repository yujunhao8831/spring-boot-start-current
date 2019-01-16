package com.goblin.manage.bean.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.goblin.manage.bean.domain.SystemLog;
import lombok.Getter;

/**
 * 通知类型(SMS:短信,MAIL:邮箱)
 *
 * @author : 披荆斩棘
 * @date : 2017/12/29
 * 对应字段 {@link SystemLog#getNoticeType()}
 */
@Getter
public enum NoticeType implements IEnum {
	/**
	 *
	 */
	SMS( "SMS" , "短信通知" ),
	MAIL( "MAIL" , "邮箱通知" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	NoticeType ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}
}
