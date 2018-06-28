package com.goblin.manage;

import com.goblin.common.util.JsonUtils;
import com.goblin.manage.bean.domain.User;
import com.goblin.manage.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : 披荆斩棘
 * @date : 2017/12/28
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void save () throws Exception {
		User user = new User();
		user.setEmail( "yujunhao_8831@yahoo.com" )
			.setNickName( "披荆斩棘" )
			.setUsername( "admin" )
			.setEnabled( true )
			.setPassword( passwordEncoder.encode( "admin" ) );
		userService.save( user );

	}

	@Test
	public void name() {
		final User admin = userService.findByUsername("admin");

		System.err.println(admin);

		JsonUtils.toJson(admin);

	}
}

















