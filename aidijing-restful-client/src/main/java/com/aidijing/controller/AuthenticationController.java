package com.aidijing.controller;

import com.aidijing.common.ResponseEntity;
import com.aidijing.domain.User;
import com.aidijing.model.JwtUser;
import com.aidijing.permission.Pass;
import com.aidijing.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Value( "${jwt.header}" )
    private String                tokenHeaderKey;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil          jwtTokenUtil;
    @Autowired
    private UserDetailsService    userDetailsService;

    /**
     * 创建身份验证token
     *
     * @param user
     * @param device
     * @return
     * @throws AuthenticationException
     */
    @PostMapping( value = "authentication" )
    @Pass
    public ResponseEntity createAuthenticationToken ( @RequestBody User user,
                                                      Device device ) throws AuthenticationException {
        // 执行安全
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication( authentication );
        final UserDetails userDetails = ( UserDetails ) authentication.getPrincipal();
        final String      token       = jwtTokenUtil.generateToken( userDetails, device );
        // 返回
        return ResponseEntity.ok().add( "token", token );
    }

    /**
     * 刷新并认证token
     *
     * @param request
     * @return
     */
    @PutMapping( value = "authentication" )
    @Pass
    public ResponseEntity refreshAndGetAuthenticationToken ( HttpServletRequest request ) {
        String  token    = request.getHeader( tokenHeaderKey );
        String  username = jwtTokenUtil.getUsernameFromToken( token );
        JwtUser user     = ( JwtUser ) userDetailsService.loadUserByUsername( username );

        if ( jwtTokenUtil.canTokenBeRefreshed( token, user.getLastPasswordResetDate() ) ) {
            String refreshedToken = jwtTokenUtil.refreshToken( token );
            return ResponseEntity.ok().add( "token", refreshedToken );
        } else {
            return ResponseEntity.fail( "token 无法刷新,未认证通过" );
        }
    }

}
