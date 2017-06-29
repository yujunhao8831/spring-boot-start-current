package com.aidijing.handler;


import com.aidijing.domain.enums.ResourceType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class ResourceTypeHandler extends BaseTypeHandler< ResourceType > {

    @Override
    public void setNonNullParameter ( PreparedStatement ps,
                                      int i,
                                      ResourceType parameter,
                                      JdbcType jdbcType ) throws SQLException {
        ps.setString( i, parameter.getCode() );
    }

    @Override
    public ResourceType getNullableResult ( ResultSet rs, String columnName ) throws SQLException {

        if ( Objects.isNull( rs.getObject( columnName ) ) ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( ResourceType.values() ) ) {
            return null;
        }
        return ResourceType.values()[0].getEnum( String.valueOf( rs.getObject( columnName ) ) );
    }

    @Override
    public ResourceType getNullableResult ( ResultSet rs, int columnIndex ) throws SQLException {
        if ( rs.getObject( columnIndex ) == null ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( ResourceType.values() ) ) {
            return null;
        }
        return ResourceType.values()[0].getEnum( String.valueOf( rs.getObject( columnIndex ) ) );
    }

    @Override
    public ResourceType getNullableResult ( CallableStatement cs, int columnIndex ) throws SQLException {
        if ( cs.getObject( columnIndex ) == null ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( ResourceType.values() ) ) {
            return null;
        }
        return ResourceType.values()[0].getEnum( String.valueOf( cs.getObject( columnIndex ) ) );
    }
}
