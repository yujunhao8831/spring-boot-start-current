package com.aidijing.manage.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.UserActionHistory;
import com.aidijing.common.PagingRequest;

/**
 * <p>
 * 后台管理用户历史记录操作表(MYISAM引擎) 服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface UserActionHistoryService extends IService<UserActionHistory> {


    PageInfo listPage ( PagingRequest pagingRequest );




}
