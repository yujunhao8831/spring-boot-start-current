package com.aidijing.manage.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.aidijing.manage.bean.domain.UserRole;
import com.aidijing.common.PagingRequest;
import com.aidijing.manage.mapper.UserRoleMapper;
import com.aidijing.manage.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 后台管理用户角色中间表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl< UserRoleMapper, UserRole > implements UserRoleService {


    @Override
    public PageInfo listPage ( PagingRequest pagingRequest ) {
        PageHelper.startPage( pagingRequest.getPageNumber(), pagingRequest.getPageSize() );
        return new PageInfo( super.selectList( null ) );
    }

    @Override
    public List< UserRole > listByUserId ( Long userId ) {
        if ( Objects.isNull( userId ) ) {
            return Collections.EMPTY_LIST;
        }
        return super.selectList( new Condition().eq( "user_id", userId ) );
    }


}
