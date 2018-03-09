package com.goblin.config;

import feign.Feign;
import feign.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * application.yml 示例 :
 * <pre>
 *      goblin:
 *        feign-ok-http-config:
 *        readTimeout: 60
 *        connectTimeout: 60
 *        writeTimeout: 180
 *      feign:
 *        httpclient:
 *          enabled: false
 *        okhttp:
 *          enabled: true
 * </pre>
 *
 * @author : 披荆斩棘
 * @date : 2017/8/4
 */
@Setter
@Getter
@ToString
@Configuration
@ConditionalOnClass( Feign.class )
@AutoConfigureBefore( FeignAutoConfiguration.class )
@ConfigurationProperties( prefix = "goblin.feign-ok-http-config" )
public class FeignOkHttpConfig {

    /** 设置新连接的默认读取超时,值0表示没有超时,单位秒 **/
    private int readTimeout    = 60;
    /** 连接超时时间,值0表示没有超时,单位秒 **/
    private int connectTimeout = 60;
    /** 设置新连接的默认写入超时,值0表示没有超时,单位秒 **/
    private int writeTimeout   = 180;

    @Bean
    public okhttp3.OkHttpClient okHttpClient () {
        return new okhttp3.OkHttpClient.Builder()
            .readTimeout( readTimeout , TimeUnit.SECONDS )
            .connectTimeout( connectTimeout , TimeUnit.SECONDS )
            .writeTimeout( writeTimeout , TimeUnit.SECONDS )
            .connectionPool( new ConnectionPool() )
            .build();
    }

    @Bean
    public Request.Options feignOptions () {
        return new Request.Options( connectTimeout * 1000 , readTimeout * 1000 );
    }
}
