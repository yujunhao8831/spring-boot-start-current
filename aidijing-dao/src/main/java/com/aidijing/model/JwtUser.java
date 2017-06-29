package com.aidijing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.aidijing.domain.Role;
import com.aidijing.vo.PermissionResourceVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * jwt对象
 */
@Data
@Accessors( chain = true )
@AllArgsConstructor
public class JwtUser implements UserDetails {

    private final Long                         id;
    private final String                       username;
    private final String                       password;
    private final String                       nickName;
    private final String                       realName;
    private final String                       email;
    private final String                       phone;
    private final Date                         lastPasswordResetDate;
    private final Long                         createManagerAdminUserId;
    private final Date                         createTime;
    private final Date                         updateTime;
    private final String                       remark;
    private final boolean                      enabled;
    private final List< Role >                 roles;
    private final List< PermissionResourceVO > permissionResource;


    @Override
    public String getUsername () {
        return this.username;
    }

    @Override
    public boolean isEnabled () {
        return this.enabled;
    }

    @JsonIgnore
    @Override
    public Collection< ? extends GrantedAuthority > getAuthorities () {
        if ( CollectionUtils.isEmpty( this.getRoles() ) ) {
            return null;
        }
        return this.getRoles().parallelStream()
                   .map( role -> new SimpleGrantedAuthority( role.getRoleNameCode() ) )
                   .collect( Collectors.toList() );
    }

    @Override
    public String getPassword () {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired () {
        return true;
    }

    @Override
    public boolean isAccountNonLocked () {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }


}
