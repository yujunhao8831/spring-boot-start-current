package com.goblin.common.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.spi.ValidationProvider;

/**
 * 用于本地测试,不用启动上下文
 *
 * @author : 披荆斩棘
 * @date : 2017/12/7
 */
public class HibernateValidatorPro {

    /**
     * 线程安全的
     *
     * @return {@link Validator}
     */
    public static Validator getHibernateValidator () {
        return Validation.byProvider( HibernateValidator.class )
						 .configure()
						 .buildValidatorFactory()
						 .getValidator();
    }

    /**
     * 线程安全的
     *
     * @return {@link Validator}
     */
    public static Validator getValidator ( Class< ? extends ValidationProvider > providerType ) {
        return Validation.byProvider( providerType )
						 .configure()
						 .buildValidatorFactory()
						 .getValidator();
    }


}
