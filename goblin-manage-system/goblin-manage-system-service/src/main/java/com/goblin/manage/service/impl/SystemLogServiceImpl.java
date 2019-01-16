package com.goblin.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goblin.common.PagingRequest;
import com.goblin.manage.bean.domain.SystemLog;
import com.goblin.manage.mapper.SystemLogMapper;
import com.goblin.manage.service.SystemLogService;
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
public class SystemLogServiceImpl extends ServiceImpl< SystemLogMapper, SystemLog > implements SystemLogService {


    @Override
    public PageInfo< SystemLog > listPage ( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber() , pagingRequest.getPageSize() );
        return new PageInfo<>( super.list() );
    }

    @Async
    @Transactional( propagation = Propagation.NOT_SUPPORTED )
    @Override
    public void asyncSave ( SystemLog systemLog ) {
        super.save( systemLog );
    }


}
