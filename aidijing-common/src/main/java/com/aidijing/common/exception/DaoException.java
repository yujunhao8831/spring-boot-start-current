package com.aidijing.common.exception;

/**
 * dao异常
 *
 * @author : 披荆斩棘
 * @date : 2017/5/19
 */
public class DaoException extends RuntimeException {

    public DaoException () {
        super();
    }

    public DaoException ( String message ) {
        super( message );
    }

    public DaoException ( String message, Throwable cause ) {
        super( message, cause );
    }

    public DaoException ( Throwable cause ) {
        super( cause );
    }

    protected DaoException ( String message,
                             Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
