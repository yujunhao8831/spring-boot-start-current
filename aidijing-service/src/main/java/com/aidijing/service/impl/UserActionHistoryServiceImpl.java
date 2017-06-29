package com.aidijing.service.impl;

import com.aidijing.domain.UserActionHistory;
import com.aidijing.mapper.UserActionHistoryMapper;
import com.aidijing.service.UserActionHistoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;

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
    public PageInfo listPage( PageRowBounds pageRowBounds ) {
        PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
        return new PageInfo( super.selectList( null ) );
    }
    
    
}
