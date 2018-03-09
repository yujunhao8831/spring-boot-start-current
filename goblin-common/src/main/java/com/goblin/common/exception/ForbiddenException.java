package com.goblin.common.exception;

/**
 * 权限
 *
 * @author : 披荆斩棘
 * @date : 2017/6/22
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException () {
        super();
    }

    public ForbiddenException ( String message ) {
        super( message );
    }

    public ForbiddenException ( String message, Throwable cause ) {
        super( message, cause );
    }

    public ForbiddenException ( Throwable cause ) {
        super( cause );
    }

    protected ForbiddenException ( String message,
                                   Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
