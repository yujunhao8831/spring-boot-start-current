package com.aidijing.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
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

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/22
 */
@Configuration
public class BeanConfig implements ApplicationContextAware, ApplicationListener< ContextRefreshedEvent > {


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
                    RequestMethod.GET,
                    RequestMethod.HEAD,
                    RequestMethod.POST,
                    RequestMethod.PUT,
                    RequestMethod.PATCH,
                    RequestMethod.DELETE,
                    RequestMethod.OPTIONS,
                    RequestMethod.TRACE
            )
    );


    /**
     * 验证码
     *
     * @return
     */
    @Bean
    public DefaultKaptcha captchaProducer () {
        Properties properties = new Properties();
        properties.put( Constants.KAPTCHA_BORDER, true );
        properties.put( Constants.KAPTCHA_BORDER_COLOR, Color.LIGHT_GRAY );
        properties.put( Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, Color.DARK_GRAY );
        properties.put( Constants.KAPTCHA_IMAGE_WIDTH, "160" );
        properties.put( Constants.KAPTCHA_IMAGE_HEIGHT, "50" );
        properties.put( Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "40" );
        properties.put( Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4" );
        properties.put( Constants.KAPTCHA_BACKGROUND_CLR_TO, Color.GRAY );
        properties.put( Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑" );
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config         config  = new Config( properties );
        kaptcha.setConfig( config );
        return kaptcha;
    }

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
            Map< Set< String >, Set< RequestMethod > > mapping = new HashMap();
            mapping.put(
                    mappingInfo.getPatternsCondition().getPatterns(),
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
