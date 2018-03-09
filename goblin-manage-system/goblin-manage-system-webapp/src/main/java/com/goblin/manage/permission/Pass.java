package com.goblin.manage.permission;

import java.lang.annotation.*;

/**
 * 放行,不需要登录,也不需要权限
 *
 * @author : 披荆斩棘
 * @date : 2017/6/25
 */
@Target( { ElementType.METHOD , ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Pass {
}
