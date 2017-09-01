package com.aidijing.manage.bean.domain.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 角色类型(root:根,super_admin:超级管理员,admin:组管理员,user:普通用户)
 * <p>
 * 对应字段 {@link com.aidijing.manage.bean.domain.Role#roleType}
 */
@Getter
public enum RoleType implements BaseEnumInterface< RoleType > {

    ROOT( "ROOT", "根" ),
    SUPER_ADMIN( "SUPER_ADMIN", "超级管理员" ),
    ADMIN( "ADMIN", "组管理员" ),
    USER( "USER", "普通用户" );

    /** code **/
    private String code;
    /** 注释 **/
    private String comment;

    RoleType ( String code, String comment ) {
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
        for ( RoleType value : RoleType.values() ) {
            if ( value.getCode().equals( inputCode ) ) {
                return value.getComment();
            }
        }
        return null;
    }

    @Override
    public RoleType getEnum ( final String inputCode ) {
        if ( Objects.isNull( inputCode ) ) {
            return null;
        }
        for ( RoleType thisEnum : RoleType.values() ) {
            if ( thisEnum.getCode().equals( inputCode ) ) {
                return thisEnum;
            }
        }
        return null;
    }
}
