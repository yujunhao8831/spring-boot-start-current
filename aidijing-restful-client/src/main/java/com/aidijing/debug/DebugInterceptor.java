package com.aidijing.debug;

import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 拦截被 {@link AppDebug} 注解的方法
 *
 * @author : 披荆斩棘
 * @date : 2016/12/1
 */
@Component
@Getter
@Setter
@ConfigurationProperties( prefix = "appDebug" )
public class DebugInterceptor implements MethodInterceptor {


    private volatile boolean debug = false;

    public Object invoke ( MethodInvocation invocation ) throws Throwable {
        if ( this.debug ) {
            return invocation.proceed();
        }
        return invocation.proceed();
    }


}
