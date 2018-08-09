package com.goblin.security.websocket;

import com.goblin.common.util.LogUtils;
import com.goblin.security.JwtTokenUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/26
 */
public class JwtWebSocketInterceptorAdapter implements ChannelInterceptor {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil       jwtTokenUtil;
	@Value( "${jwt.header:Authorization}" )
    private String             tokenHeader;


    @Override
    public Message< ? > preSend ( Message< ? > message , MessageChannel channel ) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor( message , StompHeaderAccessor.class );

        if ( ObjectUtils.notEqual( StompCommand.CONNECT , accessor.getCommand() ) ) {
            return message;
        }

        final String authToken = accessor.getFirstNativeHeader( tokenHeader );

        final String username = jwtTokenUtil.getUsernameFromToken( authToken );

        LogUtils.getLogger().debug( "authToken : {},username : {}" , authToken , username );

        if ( StringUtils.isEmpty( username ) ) {
            throw new AuthenticationCredentialsNotFoundException( "未授权" );
        }

        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            // 对于简单的验证，只需检查令牌的完整性即可。 您不必强制调用数据库。 由你自己决定
            // 是否查询数据看情况,目前是查询数据库
            UserDetails userDetails = this.userDetailsService.loadUserByUsername( username );
            if ( jwtTokenUtil.validateToken( authToken , userDetails ) ) {
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken( userDetails , null , userDetails.getAuthorities() );

                // authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

                LogUtils.getLogger().debug( "authToken : {},username : {}" , authToken , username );

                LogUtils.getLogger().debug( "该 " + username + "用户已认证WebSocket, 设置安全上下文" );

                SecurityContextHolder.getContext().setAuthentication( authentication );
                accessor.setUser( authentication );
            }
        }

        if ( Objects.isNull( accessor.getUser() ) ) {
            throw new AuthenticationCredentialsNotFoundException( "未授权" );
        }

        return message;
    }
}
