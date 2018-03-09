package com.goblin.manage.service;

import com.goblin.manage.bean.domain.User;

/**
 * @author : 披荆斩棘
 * @date : 2018/1/5
 */
public interface AggregationService {


	/**
	 * {@link com.goblin.config.TransactionalConfig#DEFAULT_REQUIRED_METHOD_RULE_TRANSACTION_ATTRIBUTES}
	 * 这里是受到事务管理的
	 */
	void saveUser( User user );

	/**
	 * 这里没有受到事务管理的,除非配置了into*的规则
	 */
	void intoUser( User user );

}
