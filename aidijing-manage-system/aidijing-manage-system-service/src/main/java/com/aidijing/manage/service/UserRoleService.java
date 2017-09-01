package com.aidijing.manage.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.UserRole;
import com.aidijing.common.PagingRequest;

import java.util.List;

/**
 * <p>
 * 后台管理用户角色中间表 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface UserRoleService extends IService<UserRole> {


    PageInfo listPage ( PagingRequest pagingRequest );


    List<UserRole> listByUserId ( Long userId );
}
