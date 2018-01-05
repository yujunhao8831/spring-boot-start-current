package com.aidijing.manage.service.impl;

import com.aidijing.common.util.AssertUtils;
import com.aidijing.manage.bean.domain.User;
import com.aidijing.manage.service.AggregationService;
import com.aidijing.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 披荆斩棘
 * @date : 2018/1/5
 */
@Service
public class AggregationServiceImpl implements AggregationService {

	@Autowired
	private UserService userService;


	@Override
	public void saveUser ( User user ) {
		userService.save( user );
		AssertUtils.isTrue( true , "测试事务是否回滚(可传播),请查看数据库." );
	}

	@Override
	public void intoUser ( User user ) {
		userService.save( user );
		AssertUtils.isTrue( true , "测试事务是否回滚(可传播),请查看数据库." );
	}
}
