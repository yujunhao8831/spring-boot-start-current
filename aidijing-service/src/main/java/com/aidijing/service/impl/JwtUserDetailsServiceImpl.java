package com.aidijing.service.impl;

import com.aidijing.domain.User;
import com.aidijing.model.JwtUser;
import com.aidijing.service.PermissionResourceService;
import com.aidijing.service.RoleService;
import com.aidijing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@CacheConfig( cacheNames = "UserDetailsService.JwtUser.namespace" )
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService               userService;
    @Autowired
    private RoleService               roleService;
    @Autowired
    private PermissionResourceService permissionResourceService;

    @Override
    @Cacheable( key = "#username" )
    public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {
        User user = userService.findByUsername( username );
        if ( user == null ) {
            throw new UsernameNotFoundException( String.format( "该'%s'用户名不存在.", username ) );
        }
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickName(),
                user.getRealName(),
                user.getEmail(),
                user.getPhone(),
                user.getLastPasswordResetDate(),
                user.getCreateManagerAdminUserId(),
                user.getCreateTime(),
                user.getUpdateTime(),
                user.getRemark(),
                user.getEnabled(),
                roleService.getByUserId( user.getId() ),
                permissionResourceService.listUserPermission( user.getId() )
        );
    }


}










