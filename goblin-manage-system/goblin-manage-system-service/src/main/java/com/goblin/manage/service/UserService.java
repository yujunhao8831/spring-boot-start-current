package com.goblin.manage.service;

import com.goblin.common.PagingRequest;
import com.goblin.manage.bean.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

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

	PageInfo<User> listPage( PagingRequest pagingRequest );

	User get ( Long id );

	User update ( User user );

	boolean save ( User user );

	boolean delete ( Long id );

	User findByUsername ( String username );

	boolean isExist ( Long userId );

	boolean isNotExist ( Long userId );


	Future<Boolean> pay ();


}
