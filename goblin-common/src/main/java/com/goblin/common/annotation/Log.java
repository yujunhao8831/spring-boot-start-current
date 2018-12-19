package com.goblin.common.annotation;


import lombok.Getter;

import java.lang.annotation.*;

/**
 * @author : 披荆斩棘
 * @date : 2017/12/28
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Log {

	/**
	 * 如有异常是否进行报警.
	 *
	 * @return <code>boolean</code>
	 */
	boolean warn () default false;

	/**
	 * 描述.
	 */
	String description () default "";

	/**
	 * 通知类型(SMS:短信,MAIL:邮箱).
	 */
	NoticeType noticeType () default NoticeType.MAIL;

	@Getter
	enum NoticeType {
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

}
