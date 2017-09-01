package com.aidijing.manage.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.SystemConfig;
import com.aidijing.common.PagingRequest;
import com.aidijing.manage.mapper.SystemConfigMapper;
import com.aidijing.manage.service.SystemConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置表(MYISAM引擎) 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {


    @Override
    public PageInfo listPage( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber(), pagingRequest.getPageSize() );
        return new PageInfo( super.selectList( null ) );
    }


}
