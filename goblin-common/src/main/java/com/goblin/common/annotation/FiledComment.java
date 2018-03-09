package com.goblin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段注释
 *
 * @author : 披荆斩棘
 * @date : 2017/7/26
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface FiledComment {

    /**
     * 字段释义
     */
    String value () default "";
}
