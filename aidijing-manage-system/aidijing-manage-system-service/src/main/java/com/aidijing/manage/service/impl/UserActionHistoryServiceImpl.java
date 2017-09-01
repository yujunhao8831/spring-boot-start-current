package com.aidijing.manage.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.UserActionHistory;
import com.aidijing.common.PagingRequest;
import com.aidijing.manage.mapper.UserActionHistoryMapper;
import com.aidijing.manage.service.UserActionHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台管理用户历史记录操作表(MYISAM引擎) 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class UserActionHistoryServiceImpl extends ServiceImpl<UserActionHistoryMapper, UserActionHistory> implements UserActionHistoryService {


    @Override
    public PageInfo listPage( PagingRequest pagingRequest) {
        PageHelper.startPage( pagingRequest.getPageNumber(), pagingRequest.getPageSize() );
        return new PageInfo( super.selectList( null ) );
    }


}
