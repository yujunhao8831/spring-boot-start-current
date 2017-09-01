package com.aidijing.manage.bean.domain.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 组编码
 * <p>
 * 对应字段 {@link com.aidijing.manage.bean.domain.Group#groupCode}
 */
@Getter
public enum GroupCode implements BaseEnumInterface< GroupCode > {

	SYSTEM( "SYSTEM" , "系统管理组" ),
	ROOT( "ROOT" , "ROOT" );

	/** code **/
	private String code;
	/** 注释 **/
	private String comment;

	GroupCode ( String code , String comment ) {
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
		for ( GroupCode value : GroupCode.values() ) {
			if ( value.getCode().equals( inputCode ) ) {
				return value.getComment();
			}
		}
		return null;
	}

	@Override
	public GroupCode getEnum ( final String inputCode ) {
		if ( Objects.isNull( inputCode ) ) {
			return null;
		}
		for ( GroupCode thisEnum : GroupCode.values() ) {
			if ( thisEnum.getCode().equals( inputCode ) ) {
				return thisEnum;
			}
		}
		return null;
	}
}
