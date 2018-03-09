package com.goblin.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author pijingzhanji
 */
@SpringBootApplication( scanBasePackages = "com.goblin" )
public class ManageSystemWebAppApplication extends SpringBootServletInitializer {

	public static void main ( String[] args ) {
		SpringApplication.run( ManageSystemWebAppApplication.class , args );
	}


	@Override
	protected SpringApplicationBuilder configure ( SpringApplicationBuilder application ) {
		return application.sources( SpringBootServletInitializer.class );
	}

}
