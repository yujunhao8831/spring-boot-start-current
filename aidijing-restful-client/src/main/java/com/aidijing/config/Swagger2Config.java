package com.aidijing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <a href="http://springfox.github.io/springfox/docs/current/">document</a>
 * <a href="https://github.com/swagger-api/swagger-core/wiki/Annotations">document</a>
 *
 * @author : 披荆斩棘
 * @date : 2017/5/21
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket docket () {
        return new Docket( DocumentationType.SWAGGER_2 )
                .apiInfo( apiInfo() )
                .select()
                .apis( RequestHandlerSelectors.basePackage( "com.aidijing" ) )
                .paths( PathSelectors.any() )
                .build();
    }

    private ApiInfo apiInfo () {
        return new ApiInfoBuilder()
                .title( "RESTful API" )
                .description( "api接口文档" )
                .contact( new Contact( "披荆斩棘", "https://yujunhao8831.github.io", "yujunhao_8831@yahoo.com" ) )
                .version( "1.0" )
                .build();
    }

}
