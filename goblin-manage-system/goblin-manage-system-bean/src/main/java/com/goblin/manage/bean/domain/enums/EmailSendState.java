package com.goblin.manage.bean.domain.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.Getter;

/**
 * 发送状态
 * (
 * NOT_SEND : 未发送,
 * SEND : 已发送,
 * FAIL_SEND : 发送失败,
 * FINAL_FAIL_SEND : 重试次数用完后,还是发送失败
 * )
 *
 * @author : 披荆斩棘
 * @date : 2017/8/7
 */
@Getter
public enum EmailSendState implements IEnum {

	/**
	 *
	 */
	NOT_SEND( "NOT_SEND" , "未发送" ),
	SEND( "SEND" , "已发送" ),
	FAIL_SEND( "FAIL_SEND" , "发送失败" ),
	FINAL_FAIL_SEND( "FINAL_FAIL_SEND" , "重试次数用完后,还是发送失败" );

	/** 数据库存储值 **/
	private String value;
	/** 相应注释 **/
	private String comment;

	EmailSendState ( String value , String comment ) {
		this.value = value;
		this.comment = comment;
	}


}
