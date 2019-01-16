package com.goblin.config;

import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/10
 */
@Configuration
@ConditionalOnExpression( "${goblin.mybatis-plus.enabled:true}" )
public class MybatisPlusConfig {


	/**
	 * mybatis-plus 性能分析拦截器<br>
	 * 文档：http://mp.baomidou.com<br>
	 */
	@Bean
	@ConditionalOnExpression( "${goblin.mybatis-plus.performance-interceptor.enabled:false}" )
	public PerformanceInterceptor performanceInterceptor () {
		return new PerformanceInterceptor();
	}


	@Bean
	public LogicSqlInjector logicSqlInjector () {
		return new LogicSqlInjector();
	}



	@MapperScan( { "com.goblin.manage.mapper" } )
	@Configuration
	public class MybatisPlusMapperScan {
	}


}
