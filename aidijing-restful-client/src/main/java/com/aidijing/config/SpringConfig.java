package com.aidijing.config;

import com.aidijing.common.GlobalConstant;
import com.aidijing.common.filter.RequestLoggingFilter;
import com.aidijing.common.util.JsonUtils;
import com.aidijing.permission.AdminPermissionInterceptor;
import com.aidijing.permission.CaptchaValidateInterceptor;
import com.github.pagehelper.PageRowBounds;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : 披荆斩棘
 * @date : 2017/5/10
 */
@Configuration
public class SpringConfig extends WebMvcConfigurerAdapter implements ErrorPageRegistrar {


    @Bean
    public RequestLoggingFilter requestLoggingFilter () {
        return new RequestLoggingFilter();
    }

    /**
     * 添加过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean () {
        // 过滤器注册
        FilterRegistrationBean  registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter encodingFilter   = new CharacterEncodingFilter();
        encodingFilter.setEncoding( StandardCharsets.UTF_8.displayName() );
        encodingFilter.setForceEncoding( true );
        // 字符过滤器 
        registrationBean.setFilter( encodingFilter );
        // 日志处理过滤器
        registrationBean.setFilter( requestLoggingFilter() );
        return registrationBean;
    }

    /**
     * 添加转换器
     *
     * @param registry
     */
    @Override
    public void addFormatters ( FormatterRegistry registry ) {
        // 从前台过来的数据转换成对应类型的转换器
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter () {
        return new MappingJackson2HttpMessageConverter( JsonUtils.getCustomizationMapper() );
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
    public void addArgumentResolvers ( List< HandlerMethodArgumentResolver > argumentResolvers ) {
        // 如果 Controller 方法中的参数有 PageRowBounds 则从前台获取数据组装, 如果没有传递则给设置一个默认值
        argumentResolvers.add( new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter ( MethodParameter parameter ) {
                return PageRowBounds.class.isAssignableFrom( parameter.getParameterType() );
            }

            @Override
            public Object resolveArgument ( MethodParameter parameter, ModelAndViewContainer mavContainer,
                                            NativeWebRequest request, WebDataBinderFactory factory ) throws Exception {
                int pageNum = NumberUtils.toInt( request.getParameter( GlobalConstant.PAGE_NUM_PARAM_NAME ) );
                if ( pageNum <= 0 ) {
                    pageNum = GlobalConstant.DEFAULT_PAGE_NUM;
                }
                int pageSize = NumberUtils.toInt( request.getParameter( GlobalConstant.PAGE_SIZE_PARAM_NAME ) );
                if ( pageSize <= 0 ) {
                    pageSize = GlobalConstant.DEFAULT_PAGE_SIZE;
                }
                return new PageRowBounds( pageNum, pageSize );
            }
        } );
    }

    @Override
    public void addReturnValueHandlers ( List< HandlerMethodReturnValueHandler > returnValueHandlers ) {
    }


    @Bean
    public CaptchaValidateInterceptor captchaInterceptor () {
        return new CaptchaValidateInterceptor();
    }

    @Bean
    public AdminPermissionInterceptor adminPermissionInterceptor () {
        return new AdminPermissionInterceptor();
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors ( InterceptorRegistry registry ) {
        registry.addInterceptor( adminPermissionInterceptor() ).addPathPatterns( "/**" );
        registry.addInterceptor( captchaInterceptor() ).addPathPatterns( "/**" );
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
                        HttpMethod.HEAD.name(),
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.TRACE.name()
                )
                .allowCredentials( true )
                // .allowedHeaders(  ) // Authorization
                // 允许的域名
                .allowedOrigins( "*" );
    }

    @Override
    public void registerErrorPages ( ErrorPageRegistry registry ) {
        registry.addErrorPages( new ErrorPage( HttpStatus.NOT_FOUND, "/404.html" ) );
        registry.addErrorPages( new ErrorPage( HttpStatus.UNAUTHORIZED, "/401.html" ) );
        registry.addErrorPages( new ErrorPage( Throwable.class, "/500.html" ) );
    }
}
