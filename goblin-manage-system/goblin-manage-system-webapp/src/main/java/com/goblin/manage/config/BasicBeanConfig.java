package com.goblin.manage.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author pijingzhanji
 */
@Configuration
public class BasicBeanConfig implements ApplicationContextAware, ApplicationListener< ContextRefreshedEvent > {


	private ApplicationContext applicationContext;
	/**
	 * 所有 {@link org.springframework.web.bind.annotation.RequestMapping#value()} 值 <br/>
	 * 比如 : [[/authentication], [/authentication], [/api/user/{id}], [/api/user/{id}]]
	 */
	private List< Set< String > > requestMappingUris = new CopyOnWriteArrayList<>();
	/**
	 * {@link org.springframework.web.bind.annotation.RequestMapping} 原始信息
	 */
	private Map< RequestMappingInfo, HandlerMethod > handlerMethods;
	/**
	 * [{[/authentication]=[POST]}, {[/authentication]=[PUT]}, {[/captcha/login]=[GET]}, {[/captcha]=[GET]}, {[/api/user/{id}]=[GET]}, {[/api/user/{id}]=[PUT]}, {[/api/user/{id}]=[DELETE]}, {[/api/user]=[POST]}, {[/api/user/test]=[GET]}, {[/api/user]=[GET]}, {[/api/user/distributed-lock]=[GET]}, {[/api/user/async]=[GET]}, {[/api/user/list]=[GET]}, {[/v2/api-docs]=[GET]}, {[/swagger-resources/configuration/security]=[]}, {[/swagger-resources/configuration/ui]=[]}, {[/swagger-resources]=[]}, {[/error]=[]}, {[/error]=[]}]
	 */
	private List< Map< Set< String >, Set< RequestMethod > > > requestMappingInfos               = new ArrayList<>();
	/** 所有请求方法 **/
	private Set< RequestMethod >                               requestMethodsRequestConditionAll = new LinkedHashSet<>(
		Arrays.asList(
			RequestMethod.GET ,
			RequestMethod.HEAD ,
			RequestMethod.POST ,
			RequestMethod.PUT ,
			RequestMethod.PATCH ,
			RequestMethod.DELETE ,
			RequestMethod.OPTIONS ,
			RequestMethod.TRACE
		)
	);


	@Bean
	public AntPathMatcher antPathMatcher () {
		return new AntPathMatcher();
	}

	@Bean
	public List< Set< String > > requestMappingUris () {
		return requestMappingUris;
	}

	@Bean
	public Map< RequestMappingInfo, HandlerMethod > handlerMethods () {
		return handlerMethods;
	}

	@Bean
	public List< Map< Set< String >, Set< RequestMethod > > > requestMappingInfos () {
		return requestMappingInfos;
	}

	@Override
	public void setApplicationContext ( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent ( ContextRefreshedEvent event ) {
		final RequestMappingHandlerMapping requestMappingHandlerMapping =
			applicationContext.getBean( RequestMappingHandlerMapping.class );
		final Map< RequestMappingInfo, HandlerMethod > handlerMethods =
			requestMappingHandlerMapping.getHandlerMethods();

		this.handlerMethods = handlerMethods;

		handlerMethods.keySet().forEach( mappingInfo -> {
			Map< Set< String >, Set< RequestMethod > > mapping = Collections.singletonMap(
				mappingInfo.getPatternsCondition().getPatterns() ,
				this.getMethods( mappingInfo.getMethodsCondition().getMethods() )
			);
			requestMappingInfos.add( mapping );
		} );

		requestMappingUris.addAll(
			handlerMethods.keySet()
						  .parallelStream()
						  .map( mappingInfo -> mappingInfo.getPatternsCondition().getPatterns() )
						  .collect( Collectors.toList() )
		);

	}


	/**
	 * 因为如果支持所有方式请求的话默认是[],但是为了显示效果这里进行补全
	 *
	 * @param methods
	 * @return RequestMethod
	 */
	private Set< RequestMethod > getMethods ( Set< RequestMethod > methods ) {
		if ( ! CollectionUtils.isEmpty( methods ) ) {
			return methods;
		}
		return requestMethodsRequestConditionAll;
	}
}
