package com.aidijing.manage.bean.domain.enums;

import lombok.Getter;

import java.util.Objects;

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
public enum EmailSendState implements BaseEnumInterface< EmailSendState > {

	NOT_SEND( "NOT_SEND" , "未发送" ),
	SEND( "SEND" , "已发送" ),
	FAIL_SEND( "FAIL_SEND" , "发送失败" ),
	FINAL_FAIL_SEND( "FINAL_FAIL_SEND" , "重试次数用完后,还是发送失败" );

	/** code **/
	private String code;
	/** 注释 **/
	private String comment;

	EmailSendState ( String code , String comment ) {
		this.code = code;
		this.comment = comment;
	}

	@Override
	public boolean isEnumCode ( final String inputCode ) {
		return Objects.nonNull( getEnum( inputCode ) );
	}

	@Override
	public boolean isNotEnumCode ( final String inputCode ) {
		return ! isEnumCode( inputCode );
	}

	@Override
	public String getCodeComment ( final String inputCode ) {
		if ( Objects.isNull( inputCode ) ) {
			return null;
		}
		for ( EmailSendState value : EmailSendState.values() ) {
			if ( value.getCode().equals( inputCode ) ) {
				return value.getComment();
			}
		}
		return null;
	}

	@Override
	public EmailSendState getEnum ( final String inputCode ) {
		if ( Objects.isNull( inputCode ) ) {
			return null;
		}
		for ( EmailSendState thisEnum : EmailSendState.values() ) {
			if ( thisEnum.getCode().equals( inputCode ) ) {
				return thisEnum;
			}
		}
		return null;
	}
}
