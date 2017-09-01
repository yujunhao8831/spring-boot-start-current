package com.aidijing.manage.bean.handler;


import com.aidijing.manage.bean.domain.enums.RoleType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class RoleTypeHandler extends BaseTypeHandler< RoleType > {

    @Override
    public void setNonNullParameter ( PreparedStatement ps,
                                      int i,
                                      RoleType parameter,
                                      JdbcType jdbcType ) throws SQLException {
        ps.setString( i, parameter.getCode() );
    }

    @Override
    public RoleType getNullableResult ( ResultSet rs, String columnName ) throws SQLException {

        if ( Objects.isNull( rs.getObject( columnName ) ) ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( RoleType.values() ) ) {
            return null;
        }
        return RoleType.values()[0].getEnum( String.valueOf( rs.getObject( columnName ) ) );
    }

    @Override
    public RoleType getNullableResult ( ResultSet rs, int columnIndex ) throws SQLException {
        if ( rs.getObject( columnIndex ) == null ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( RoleType.values() ) ) {
            return null;
        }
        return RoleType.values()[0].getEnum( String.valueOf( rs.getObject( columnIndex ) ) );
    }

    @Override
    public RoleType getNullableResult ( CallableStatement cs, int columnIndex ) throws SQLException {
        if ( cs.getObject( columnIndex ) == null ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( RoleType.values() ) ) {
            return null;
        }
        return RoleType.values()[0].getEnum( String.valueOf( cs.getObject( columnIndex ) ) );
    }
}
