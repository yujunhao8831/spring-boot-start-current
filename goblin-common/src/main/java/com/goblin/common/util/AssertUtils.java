package com.goblin.common.util;

import com.goblin.common.exception.*;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * <p>
 * 断言
 * 使用异常流程控制,异常控制流程比if else '状态码'控制可读性更强
 * </p>
 * <b style="color:red">
 * 注 : 异常的处理效率比条件分支低,内部系统可用异常形式控制流程,对外http/api接口则使用'错误码'.
 * </b>
 *
 * @author : 披荆斩棘
 * @date : 2017/6/3
 */
public abstract class AssertUtils {


	/**
	 * 如果条件为<code>true</code> throw {@link ResourceNotFoundException}
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws ResourceNotFoundException
	 */
	public static void assertResourceNotFoundIsTrue ( boolean condition , String message ) throws
																						   ResourceNotFoundException {
		if ( condition ) {
			throw new ResourceNotFoundException( message );
		}

	}


	/**
	 * 如果条件为<code>true</code> throw {@link CaptchaException}
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws CaptchaException
	 */
	public static void assertCaptchaIsTrue ( boolean condition , String message ) throws
																				  CaptchaException {
		if ( condition ) {
			throw new CaptchaException( message );
		}
	}

	/**
	 * 如果条件为<code>true</code> throw {@link ForbiddenException}
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws ForbiddenException
	 */
	public static void assertPermissionIsTrue ( boolean condition , String message ) throws ForbiddenException {
		if ( condition ) {
			throw new ForbiddenException( message );
		}
	}

	/**
	 * service 层断言
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws ServiceException
	 */
	public static void isTrue ( boolean condition , String message ) throws
																	 ServiceException {
		assertServiceException( condition , message );
	}


	public static void isTrue ( boolean condition , Throwable throwable ) throws Throwable {
		if ( condition ) {
			throw throwable;
		}
	}

	/**
	 * dao 层断言
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws DaoException
	 */
	public static void assertDaoIsTrue ( boolean condition , String message ) throws
																			  DaoException {
		daoServiceException( condition , message );
	}

	/**
	 * 断言
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws DaoException 如果条件为<code>true</code>,则会抛Service异常(异常为运行时异常,在Spring中会有统一异常处理)
	 */
	private static void assertServiceException ( boolean condition , String message ) {
		if ( condition ) {
			throw new ServiceException( message );
		}
	}

	/**
	 * 断言
	 *
	 * @param condition : 断言条件
	 * @param message   : 错误信息
	 * @throws DaoException 如果条件为<code>true</code>,则会抛Dao异常(异常为运行时异常,在Spring中会有统一异常处理)
	 */
	private static void daoServiceException ( boolean condition , String message ) {
		if ( condition ) {
			throw new DaoException( message );
		}
	}


	public static void bindingResult ( BindingResult result ) throws
															  ValidatedIllegalArgumentException {
		if ( Objects.isNull( result ) ) {
			return;
		}
		if ( result.hasErrors() ) {
			throw new ValidatedIllegalArgumentException( result );
		}

	}
}
