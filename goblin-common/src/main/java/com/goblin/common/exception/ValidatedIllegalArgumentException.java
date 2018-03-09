package com.goblin.common.exception;

import org.springframework.validation.BindingResult;

/**
 * 参数异常
 * {@link org.springframework.validation.Validator}
 *
 * @author : 披荆斩棘
 * @date : 2017/5/19
 */
public class ValidatedIllegalArgumentException extends RuntimeException {

	private final BindingResult bindingResult;

	public ValidatedIllegalArgumentException ( BindingResult bindingResult ) {
		super();
		this.bindingResult = bindingResult;
	}

	public ValidatedIllegalArgumentException ( String message ,
											   BindingResult bindingResult ) {
		super( message );
		this.bindingResult = bindingResult;
	}

	public ValidatedIllegalArgumentException ( String message ,
											   Throwable cause ,
											   BindingResult bindingResult ) {
		super( message , cause );
		this.bindingResult = bindingResult;
	}

	public ValidatedIllegalArgumentException ( Throwable cause ,
											   BindingResult bindingResult ) {
		super( cause );
		this.bindingResult = bindingResult;
	}

	protected ValidatedIllegalArgumentException ( String message ,
												  Throwable cause ,
												  boolean enableSuppression ,
												  boolean writableStackTrace ,
												  BindingResult bindingResult ) {
		super( message , cause , enableSuppression , writableStackTrace );
		this.bindingResult = bindingResult;
	}


	public BindingResult getBindingResult () {
		return bindingResult;
	}
}
