package com.aidijing.manage.service.impl;

import com.aidijing.common.PagingRequest;
import com.aidijing.manage.bean.domain.SystemLog;
import com.aidijing.manage.mapper.SystemLogMapper;
import com.aidijing.manage.service.SystemLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-12-29
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {


    @Override
    public PageInfo<SystemLog> listPage( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber(), pagingRequest.getPageSize() );
        return new PageInfo<>( super.selectList( null ) );
    }

    @Async
	@Transactional(propagation = Propagation.NOT_SUPPORTED )
	@Override
	public void asyncSave ( SystemLog systemLog ) {
		super.insert( systemLog );
    }


}
