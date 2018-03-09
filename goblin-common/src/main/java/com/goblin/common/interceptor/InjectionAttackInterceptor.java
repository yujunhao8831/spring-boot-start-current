package com.goblin.common.interceptor;

import com.goblin.common.annotation.PassInjectionAttackIntercept;
import com.goblin.common.filter.handler.DefaultInjectionAttackHandler;
import com.goblin.common.filter.handler.InjectionAttackHandler;
import com.goblin.common.util.LogUtils;
import com.goblin.common.util.RequestUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 注入攻击拦截器
 *
 * @author : 披荆斩棘
 * @date : 2017/8/29
 * @see PassInjectionAttackIntercept
 */
@Getter
@Setter
public class InjectionAttackInterceptor extends HandlerInterceptorAdapter {


	private InjectionAttackHandler injectionAttackHandler = DefaultInjectionAttackHandler.getInstance();

	@Override
	public boolean preHandle ( HttpServletRequest request , HttpServletResponse response , Object handler ) throws
																											Exception {
		if ( ! ( handler instanceof HandlerMethod ) ) {
			LogUtils.getLogger().debug( "! ( handler instanceof HandlerMethod )" );
			LogUtils.getLogger().debug( "handler : {}" , handler );
			return true;
		}

		HandlerMethod handlerMethod = ( HandlerMethod ) handler;

		final PassInjectionAttackIntercept passInjectionAttackIntercept =
			this.getHandlerAnnotation( handlerMethod , PassInjectionAttackIntercept.class );


		String[] ignoreStrings = null;
		if ( Objects.nonNull( passInjectionAttackIntercept ) ) {
			if ( ArrayUtils.isEmpty( passInjectionAttackIntercept.value() ) ) {
				LogUtils.getLogger().debug( "pass,不需要注入攻击拦截" );
				return true;
			}
			ignoreStrings = passInjectionAttackIntercept.value();
		}


		final String parameters = RequestUtils.getRequestParameters( request );
		LogUtils.getLogger().debug( "请求参数 : {} " , parameters );
		LogUtils.getLogger().debug( "ignoreStrings : {} " , Arrays.toString( ignoreStrings ) );


		// 参数注入攻击处理
		if ( this.injectionAttackHandler.isInjectionAttack( parameters , ignoreStrings ) ) {
			LogUtils.getLogger().debug( "参数 {} 被判断为注入攻击" , parameters );
			this.injectionAttackHandler.attackHandle( request , response , parameters );
			return false;
		}

		final Map< String, String > decodedUriVariables = ( Map< String, String > ) request.getAttribute( HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE );

		if ( MapUtils.isEmpty( decodedUriVariables ) ) {
			return true;
		}

		// URI PATH 注入攻击处理
		for ( String decodedUriVariable : decodedUriVariables.values() ) {
			if ( this.injectionAttackHandler.isInjectionAttack( decodedUriVariable , ignoreStrings ) ) {
				LogUtils.getLogger().debug( "URI {} 被判断为注入攻击" , parameters );
				this.injectionAttackHandler.attackHandle( request , response , decodedUriVariable );
				return false;
			}
		}
		return true;
	}

	private < T extends Annotation > T getHandlerAnnotation ( HandlerMethod handlerMethod ,
															  Class< T > clazz ) {
		T annotation = handlerMethod.getMethodAnnotation( clazz );
		if ( Objects.nonNull( annotation ) ) {
			return annotation;
		}
		return handlerMethod.getBeanType().getAnnotation( clazz );
	}
}
