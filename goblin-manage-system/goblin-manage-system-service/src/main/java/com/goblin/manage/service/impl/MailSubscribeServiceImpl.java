package com.goblin.manage.service.impl;

import com.goblin.common.PagingRequest;
import com.goblin.manage.bean.domain.MailSubscribe;
import com.goblin.manage.mapper.MailSubscribeMapper;
import com.goblin.manage.service.MailSubscribeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 邮件订阅 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-08-07
 */
@Service
public class MailSubscribeServiceImpl extends ServiceImpl<MailSubscribeMapper, MailSubscribe> implements MailSubscribeService {

    @Override
    public PageInfo<MailSubscribe> listPage( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber(), pagingRequest.getPageSize() );
        return new PageInfo<>( super.selectList( null ) );
    }


}
