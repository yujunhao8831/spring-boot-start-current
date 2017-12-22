package com.aidijing.manage.service;

import com.aidijing.manage.bean.domain.User;
import com.baomidou.mybatisplus.service.IService;

import java.util.concurrent.Future;

/**
 * <p>
 * 后台管理用户 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface UserService extends IService< User > {


	User get ( Long id );

	User update ( User user );

	boolean save ( User user );

	boolean delete ( Long id );

	User findByUsername ( String username );

	boolean isExist ( Long userId );

	boolean isNotExist ( Long userId );


	Future<Boolean> pay ();

}
