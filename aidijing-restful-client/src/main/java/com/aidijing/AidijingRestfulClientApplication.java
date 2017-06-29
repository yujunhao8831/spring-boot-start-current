package com.aidijing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 对于需要部署到传统servlet容器之中的应用，Boot提供了一种方式以编码的方式初始化Web配置。
 * 为了使用这一点，Boot提供了可选的WebApplicationInitializer，它会使用servlet容器来
 * 注册应用，这会通过Servlet 3.0 API以编码的方式注册servlet并且会用到ServletContext。
 * 通过提供SpringBootServletInitializer的子类，Boot应用能够使用嵌入的Spring上下文来
 * 注册配置，这个Spring上下文是在容器初始化的时候创建的。
 */
@SpringBootApplication
public class AidijingRestfulClientApplication extends SpringBootServletInitializer {

    public static void main ( String[] args ) {
        SpringApplication.run( AidijingRestfulClientApplication.class, args );
    }

    /**
     * Application类中被重写的configure方法就是使用嵌入式的Spring上下文注册应用的地方。
     * 在更为正式的场景之中，这个方法可能会用来注册Spring Java配置类，它会定义应用中所有
     * controller和服务的bean。
     *
     * @param application
     * @see <a href="http://www.infoq.com/cn/articles/microframeworks1-spring-boot">info</a>
     */
    protected SpringApplicationBuilder configure ( SpringApplicationBuilder application ) {
        return application.sources( AidijingRestfulClientApplication.class );
    }
}
