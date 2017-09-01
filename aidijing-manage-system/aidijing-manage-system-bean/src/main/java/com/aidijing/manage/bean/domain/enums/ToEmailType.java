package com.aidijing.manage.bean.domain.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 收件箱类型(SYSTEM:系统邮箱,OTHER:其他邮箱[非系统邮箱]),默认为 : SYSTEM
 *
 * @author : 披荆斩棘
 * @date : 2017/8/7
 */
@Getter
public enum ToEmailType implements BaseEnumInterface< ToEmailType > {

    SYSTEM( "SYSTEM" , "系统邮箱" ),
    OTHER( "OTHER" , "其他邮箱[非系统邮箱])" );

    /** code **/
    private String code;
    /** 注释 **/
    private String comment;

    ToEmailType ( String code , String comment ) {
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
        for ( ToEmailType value : ToEmailType.values() ) {
            if ( value.getCode().equals( inputCode ) ) {
                return value.getComment();
            }
        }
        return null;
    }

    @Override
    public ToEmailType getEnum ( final String inputCode ) {
        if ( Objects.isNull( inputCode ) ) {
            return null;
        }
        for ( ToEmailType thisEnum : ToEmailType.values() ) {
            if ( thisEnum.getCode().equals( inputCode ) ) {
                return thisEnum;
            }
        }
        return null;
    }
}

