package com.goblin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goblin.common.SimpleDateFormatPro;
import com.goblin.common.converter.StringToDateConverter;
import com.goblin.common.filter.RequestLoggingFilter;
import com.goblin.common.interceptor.InjectionAttackInterceptor;
import com.goblin.common.util.DateFormatStyle;
import com.goblin.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Spring 配置
 *
 * @author : 披荆斩棘
 * @date : 2017/5/10
 */
@Configuration
public class SpringConfig implements WebMvcConfigurer, ErrorPageRegistrar {


    @Autowired( required = false )
    private InjectionAttackInterceptor injectionAttackInterceptor;

    /**
     * <pre>
     *      goblin:
     *        filter:
     *          injection-attack-interceptor:
     *            enabled: true
     *
     * </pre>
     */
    @Bean
    @Order( Ordered.HIGHEST_PRECEDENCE )
    @ConditionalOnProperty( prefix = "goblin.filter.injection-attack-interceptor", name = "enabled", havingValue = "true" )
    public InjectionAttackInterceptor injectionAttackInterceptor () {
        return new InjectionAttackInterceptor();
    }


    /**
     * 添加过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean () {
        String[] excludeUrlPatterns = { "*.js" , "*.jpg" , "*.png" , "*.css" , "*.html" , "*.gif" };
        // 日志处理过滤器
        return new FilterRegistrationBean<>( new RequestLoggingFilter().setExcludeUrlPatterns( excludeUrlPatterns ) );
    }

    /**
     * 添加转换器
     *
     * @param registry
     */
    @Override
    public void addFormatters ( FormatterRegistry registry ) {
        // 从前台过来的数据转换成对应类型的转换器,但是对 @RequestBody 注解的参数无效
        registry.addConverter( new StringToDateConverter() );
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter () {
        ObjectMapper customizationMapper = JsonUtils.buildCustomizationMapper()
                                                    // 设置格式化解析,支持多种,针对 @RequestBody 参数解析时,前端传入不同种类时间格式(以字符串形式)时能够正常解析.
                                                    // 解析失败时会抛 HttpMessageNotReadableException 异常
                                                    .setDateFormat( new SimpleDateFormatPro( DateFormatStyle.getDateFormatStyles() ) );
        return new MappingJackson2HttpMessageConverter( customizationMapper );
    }

    /**
     * 消息转换
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters ( List< HttpMessageConverter< ? > > converters ) {
        // 默认转换器注册后, 插入自定义的请求响应转换器
        converters.add( new StringHttpMessageConverter( StandardCharsets.UTF_8 ) );
        converters.add( this.mappingJackson2HttpMessageConverter() );

    }


    @Override
    public void addReturnValueHandlers ( List< HandlerMethodReturnValueHandler > returnValueHandlers ) {
        // 返回值的处理,可以用来处理敏感数据的显示
    }

    @Override
    public void addInterceptors ( InterceptorRegistry registry ) {
        if ( Objects.nonNull( injectionAttackInterceptor ) ) {
            registry.addInterceptor( injectionAttackInterceptor ).addPathPatterns( "/**" );
        }
    }

    /**
     * cors跨域处理
     *
     * @param registry
     */
    @Override
    public void addCorsMappings ( CorsRegistry registry ) {
        registry.addMapping( "/**" )
                .allowedMethods(
                        HttpMethod.HEAD.name() ,
                        HttpMethod.GET.name() ,
                        HttpMethod.POST.name() ,
                        HttpMethod.PUT.name() ,
                        HttpMethod.DELETE.name() ,
                        HttpMethod.OPTIONS.name() ,
                        HttpMethod.PATCH.name() ,
                        HttpMethod.TRACE.name()
                )
                // 允许的域名
                .allowedOrigins( "*" );
    }


    @Override
    public void registerErrorPages ( ErrorPageRegistry registry ) {
        registry.addErrorPages( new ErrorPage( HttpStatus.NOT_FOUND , "/404" ) );
        registry.addErrorPages( new ErrorPage( HttpStatus.UNAUTHORIZED , "/401" ) );
        registry.addErrorPages( new ErrorPage( Throwable.class , "/500" ) );
    }



}
