package com.goblin.common.exception;

/**
 * 资源不存在
 *
 * @author : 披荆斩棘
 * @date : 2017/7/10
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException () {
        super();
    }

    public ResourceNotFoundException ( String message ) {
        super( message );
    }

    public ResourceNotFoundException ( String message, Throwable cause ) {
        super( message, cause );
    }

    public ResourceNotFoundException ( Throwable cause ) {
        super( cause );
    }

    protected ResourceNotFoundException ( String message,
                                          Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
