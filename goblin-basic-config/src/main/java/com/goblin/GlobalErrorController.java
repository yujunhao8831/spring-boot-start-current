package com.goblin;

import com.goblin.common.ResponseEntityPro;
import com.goblin.common.exception.*;
import com.goblin.common.util.LogUtils;
import lombok.Getter;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintDeclarationException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 统一异常处理
 *
 * <a href="https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web.html#mvc-ann-rest-spring-mvc-exceptions">default</a>
 *
 * @author : 披荆斩棘
 * @date : 2017/5/18
 */
@RestControllerAdvice
public class GlobalErrorController {


	@ExceptionHandler( ConstraintDeclarationException.class )
	public ResponseEntity constraintDeclarationException ( ConstraintDeclarationException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() );
	}

	@ExceptionHandler( ServletRequestBindingException.class )
	public ResponseEntity servletRequestBindingExceptionHandler ( ServletRequestBindingException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() + ",请检查参数名称是否符合格式." );
	}

	@ExceptionHandler( ValidatedIllegalArgumentException.class )
	public ResponseEntity validatedIllegalArgumentExceptionHandler ( ValidatedIllegalArgumentException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( Objects.requireNonNull( e.getBindingResult().getFieldError() ).getDefaultMessage() );
	}

	@ExceptionHandler( MethodArgumentNotValidException.class )
	public ResponseEntity methodArgumentNotValidExceptionHandler ( MethodArgumentNotValidException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( Objects.requireNonNull( e.getBindingResult().getFieldError() ).getDefaultMessage() );
	}

	@ExceptionHandler( HttpMessageNotReadableException.class )
	public ResponseEntity httpMessageNotReadableExceptionHandler ( HttpMessageNotReadableException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest(
			"Required request body is missing,请求体无法解析,请检查请求体格式的有效性(或请求体内参数格式有误导致无法解析). " );
	}

	@ExceptionHandler( MultipartException.class )
	public ResponseEntity multipartExceptionHandler ( MultipartException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() );
	}

	@ExceptionHandler( AuthenticationCredentialsNotFoundException.class )
	public ResponseEntity serviceErrorHandler ( AuthenticationCredentialsNotFoundException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.unauthorized( e.getMessage() );
	}

	@ExceptionHandler( UsernameNotFoundException.class )
	public ResponseEntity serviceErrorHandler ( UsernameNotFoundException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.unauthorized( e.getMessage() , e.getMessage() );
	}

	@ExceptionHandler( ResourceNotFoundException.class )
	public ResponseEntity serviceErrorHandler ( ResourceNotFoundException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() );
	}

	@ExceptionHandler( AuthenticationException.class )
	public ResponseEntity serviceErrorHandler ( AuthenticationException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.unauthorized( e.getMessage() , e.getMessage() );
	}

	@ExceptionHandler( ForbiddenException.class )
	public ResponseEntity forbiddenErrorHandler ( ForbiddenException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.forbidden( e.getMessage() );
	}

	@ExceptionHandler( HttpRequestMethodNotSupportedException.class )
	public ResponseEntity forbiddenErrorHandler ( HttpRequestMethodNotSupportedException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.status( HttpStatus.METHOD_NOT_ALLOWED , e.getMessage() );
	}

	@ExceptionHandler( CaptchaException.class )
	public ResponseEntity captchaErrorHandler ( CaptchaException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() );
	}

	@ExceptionHandler( ServiceException.class )
	public ResponseEntity serviceErrorHandler ( ServiceException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() );
	}

	@ExceptionHandler( DaoException.class )
	public ResponseEntity daoErrorHandler ( DaoException e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.badRequest( e.getMessage() );
	}

	@ExceptionHandler( { SQLException.class , DataAccessException.class } )
	public ResponseEntity sqlErrorHandler ( Throwable e ) {
		LogUtils.getLogger().error( "error" , e );
		return ResponseEntityPro.internalServerError(
			"服务器内部错误,EXCEPTION_CODE:" + ExceptionCode.SQL_EXCEPTION.getCode() );
	}


	@ExceptionHandler( Throwable.class )
	public ResponseEntity globalErrorHandler ( Throwable e ) {
		LogUtils.getLogger().error( "internalServerError" , e );
		return ResponseEntityPro.internalServerError( "internalServerError : " + e.getMessage() );
	}


	@Getter
	private enum ExceptionCode {
		/**
		 *
		 */
		SQL_EXCEPTION( "9001" , "SQL异常" );


		private String code;
		private String comment;

		ExceptionCode ( String code , String comment ) {
			this.code = code;
			this.comment = comment;
		}


	}

}
