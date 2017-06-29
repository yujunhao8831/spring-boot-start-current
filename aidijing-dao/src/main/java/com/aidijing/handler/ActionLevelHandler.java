package com.aidijing.handler;


import com.aidijing.domain.enums.ActionLevel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class ActionLevelHandler extends BaseTypeHandler< ActionLevel > {

    @Override
    public void setNonNullParameter ( PreparedStatement ps,
                                      int i,
                                      ActionLevel parameter,
                                      JdbcType jdbcType ) throws SQLException {
        ps.setString( i, parameter.getCode() );
    }

    @Override
    public ActionLevel getNullableResult ( ResultSet rs, String columnName ) throws SQLException {

        if ( Objects.isNull( rs.getObject( columnName ) ) ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( ActionLevel.values() ) ) {
            return null;
        }
        return ActionLevel.values()[0].getEnum( String.valueOf( rs.getObject( columnName ) ) );
    }

    @Override
    public ActionLevel getNullableResult ( ResultSet rs, int columnIndex ) throws SQLException {
        if ( rs.getObject( columnIndex ) == null ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( ActionLevel.values() ) ) {
            return null;
        }
        return ActionLevel.values()[0].getEnum( String.valueOf( rs.getObject( columnIndex ) ) );
    }

    @Override
    public ActionLevel getNullableResult ( CallableStatement cs, int columnIndex ) throws SQLException {
        if ( cs.getObject( columnIndex ) == null ) {
            return null;
        }
        if ( ArrayUtils.isEmpty( ActionLevel.values() ) ) {
            return null;
        }
        return ActionLevel.values()[0].getEnum( String.valueOf( cs.getObject( columnIndex ) ) );
    }
}
