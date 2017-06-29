package com.aidijing.config;

import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/10
 */
@Configuration
@MapperScan( "com.aidijing.mapper" )
public class MybatisPlusConfig {

    /**
     * mybatis-plus 性能分析拦截器<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor () {
        return new PerformanceInterceptor();
    }

}
