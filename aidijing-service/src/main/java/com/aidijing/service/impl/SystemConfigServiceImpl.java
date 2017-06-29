package com.aidijing.service.impl;

import com.aidijing.domain.SystemConfig;
import com.aidijing.mapper.SystemConfigMapper;
import com.aidijing.service.SystemConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;

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
    public PageInfo listPage( PageRowBounds pageRowBounds ) {
        PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
        return new PageInfo( super.selectList( null ) );
    }
    
    
}
