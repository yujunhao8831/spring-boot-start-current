package com.goblin.manage;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : 披荆斩棘
 * @date : 2017/12/28
 */
public class PasswordEncoderTest {

	@Test
	public void name () throws Exception {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


		for ( int i = 0 ; i < 10 ; i++ ) {
			System.err.println( passwordEncoder.encode( "123456" ) );
		}
	}
}
