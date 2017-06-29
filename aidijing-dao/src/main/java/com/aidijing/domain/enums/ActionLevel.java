package com.aidijing.domain.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 操作级别(FATAL_1 : 致命,能影响到应用 , ERROR_2 : 错误,会影响正常功能, WARN_3 : 日常警告 ,INFO_4 : 日常记录)
 * <p>
 * 对应字段 {@link com.aidijing.domain.UserActionHistory#actionLevel}
 */
@Getter
public enum ActionLevel implements BaseEnumInterface< ActionLevel > {
    FATAL( "FATAL_1", "致命,能影响到应用" ),
    ERROR( "ERROR_2", "错误,会影响正常功能" ),
    WARN( "WARN_3", "日常警告" ),
    INFO( "INFO_4", "日常记录" );

    /** code **/
    private String code;
    /** 注释 **/
    private String comment;

    ActionLevel ( String code, String comment ) {
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
        for ( ActionLevel value : ActionLevel.values() ) {
            if ( value.getCode().equals( inputCode ) ) {
                return value.getComment();
            }
        }
        return null;
    }

    @Override
    public ActionLevel getEnum ( final String inputCode ) {
        if ( Objects.isNull( inputCode ) ) {
            return null;
        }
        for ( ActionLevel thisEnum : ActionLevel.values() ) {
            if ( thisEnum.getCode().equals( inputCode ) ) {
                return thisEnum;
            }
        }
        return null;
    }
}
