package com.goblin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author : 披荆斩棘
 * @date : 2017/7/2
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure ( HttpSecurity http ) throws Exception {
		http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
	}
}
