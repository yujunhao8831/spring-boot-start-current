package com.aidijing;

import com.aidijing.common.ResponseEntity;
import com.aidijing.common.exception.CaptchaException;
import com.aidijing.common.exception.DaoException;
import com.aidijing.common.exception.ForbiddenException;
import com.aidijing.common.exception.ServiceException;
import com.aidijing.common.util.LogUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/18
 */
@RestControllerAdvice
public class GlobalErrorController {


    @ExceptionHandler( AuthenticationCredentialsNotFoundException.class )
    public ResponseEntity serviceErrorHandler ( AuthenticationCredentialsNotFoundException e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.unauthorized( e.getMessage() );
    }

    @ExceptionHandler( AuthenticationException.class )
    public ResponseEntity serviceErrorHandler ( AuthenticationException e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.unauthorized();
    }

    @ExceptionHandler( ForbiddenException.class )
    public ResponseEntity forbiddenErrorHandler ( ForbiddenException e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.unauthorized( e.getMessage() );
    }

    @ExceptionHandler( CaptchaException.class )
    public ResponseEntity captchaErrorHandler ( CaptchaException e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.fail( e.getMessage() );
    }

    @ExceptionHandler( ServiceException.class )
    public ResponseEntity serviceErrorHandler ( ServiceException e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.fail( e.getMessage() );
    }

    @ExceptionHandler( DaoException.class )
    public ResponseEntity daoErrorHandler ( DaoException e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.fail( e.getMessage() );
    }

    @ExceptionHandler( { SQLException.class , DataAccessException.class } )
    public ResponseEntity sqlErrorHandler ( Throwable e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.error( "服务器内部错误,EXCEPTION_CODE:" + ExceptionCode.SQL_EXCEPTION.getCode() );
    }


    @ExceptionHandler( Throwable.class )
    public ResponseEntity globalErrorHandler ( Throwable e ) {
        if ( LogUtils.getLogger().isErrorEnabled() ) {
            LogUtils.getLogger().error( "error", e );
        }
        return ResponseEntity.error( "error : " + e.getMessage() );
    }


    private enum ExceptionCode {
        SQL_EXCEPTION( "9001", "SQL异常" );


        private String code;
        private String comment;

        ExceptionCode ( String code, String comment ) {
            this.code = code;
            this.comment = comment;
        }

        /**
         * 判断传入的code是否是枚举中所定义的code
         *
         * @param code
         * @return
         */
        public static boolean isCode ( final String code ) {
            for ( ExceptionCode value : ExceptionCode.values() ) {
                if ( value.getCode().equalsIgnoreCase( code ) ) {
                    return true;
                }
            }
            return false;
        }

        public static String codeValue ( final String code ) {
            for ( ExceptionCode value : ExceptionCode.values() ) {
                if ( value.getCode().equalsIgnoreCase( code ) ) {
                    return value.getComment();
                }
            }
            return null;
        }

        public static boolean isNotCode ( final String code ) {
            return ! isCode( code );
        }

        public String getComment () {
            return comment;
        }

        public String getCode () {
            return code;
        }
    }

}
