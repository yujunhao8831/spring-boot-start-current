package com.goblin.manage.permission;

import java.lang.annotation.*;

/**
 * 不需要权限,只需登录即可
 *
 * @author : 披荆斩棘
 * @date : 2017/6/25
 */
@Target( { ElementType.METHOD , ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface NotNeedPermission {
}
