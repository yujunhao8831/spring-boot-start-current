package com.aidijing.common.exception;

/**
 * service异常
 *
 * @author : 披荆斩棘
 * @date : 2017/5/19
 */
public class ServiceException extends RuntimeException {

    public ServiceException () {
        super();
    }

    public ServiceException ( String message ) {
        super( message );
    }

    public ServiceException ( String message, Throwable cause ) {
        super( message, cause );
    }

    public ServiceException ( Throwable cause ) {
        super( cause );
    }

    protected ServiceException ( String message,
                                 Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
