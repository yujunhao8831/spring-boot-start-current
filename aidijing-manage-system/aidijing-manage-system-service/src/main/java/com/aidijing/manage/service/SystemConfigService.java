package com.aidijing.manage.service;

import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.SystemConfig;
import com.aidijing.common.PagingRequest;

/**
 * <p>
 * 系统配置表服务类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
public interface SystemConfigService extends IService<SystemConfig> {

    PageInfo listPage ( PagingRequest pagingRequest );




}
