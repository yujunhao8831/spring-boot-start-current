package com.aidijing.service.impl;

import com.aidijing.domain.Role;
import com.aidijing.mapper.RoleMapper;
import com.aidijing.service.RoleService;
import com.aidijing.service.UserRoleService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台管理角色表 服务实现类
 * </p>
 *
 * @author 披荆斩棘
 * @since 2017-06-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl< RoleMapper, Role > implements RoleService {


    @Autowired
    private UserRoleService userRoleService;

    @Override
    public PageInfo listPage ( PageRowBounds pageRowBounds ) {
        PageHelper.startPage( pageRowBounds.getOffset(), pageRowBounds.getLimit() );
        return new PageInfo( super.selectList( null ) );
    }


    @Override
    public List< Role > getByUserId ( Long userId ) {
        final List< Long > roleIds = userRoleService
                .selectObjs( new Condition().eq( "user_id", userId ).setSqlSelect( "id" ) );

        if ( CollectionUtils.isEmpty( roleIds ) ) {
            return null;
        }
        return this.selectBatchIds( roleIds );
    }

}
