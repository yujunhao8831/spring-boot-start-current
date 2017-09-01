package com.aidijing.security;

import com.aidijing.common.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil implements Serializable {

    private static final long   serialVersionUID   = - 3301605591108950415L;
    /** 用户名key **/
    private static final String CLAIM_KEY_USERNAME = "sub";
    /** 用户模型的用户代理(终端)key **/
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    /** token创建时间key **/
    private static final String CLAIM_KEY_CREATED  = "created";
    private static final String AUDIENCE_UNKNOWN   = "unknown";
    /** 用户模型的用户代理(终端)浏览器端 value **/
    private static final String AUDIENCE_WEB       = "web";
    /** 用户模型的用户代理(终端)手机端 value **/
    private static final String AUDIENCE_MOBILE    = "mobile";
    /** 用户模型的用户代理(终端)平板 value **/
    private static final String AUDIENCE_TABLET    = "tablet";

    /** 签名密钥 **/
    @Value( "${jwt.secret:#{ T(java.util.UUID).randomUUID().toString()}}" )
    private String  secret;
    /** token过期时间 **/
    @Value( "${jwt.expiration:3600}" )
    private Integer expiration;
    /** header key **/
    @Value( "${jwt.header:Authorization}" )
    private String  tokenHeaderKey;

    /**
     * 根据token得到用户名
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken ( String token ) {
        String username;
        try {
            final Claims claims = getClaimsFromToken( token );
            username = claims.getSubject();
        } catch ( Exception e ) {
            username = null;
        }
        return username;
    }

    /**
     * 得到token的开始时间(或者说创建时间)
     *
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken ( String token ) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken( token );
            created = new Date( ( Long ) claims.get( CLAIM_KEY_CREATED ) );
        } catch ( Exception e ) {
            created = null;
        }
        return created;
    }

    /**
     * 得到token过期时间
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken ( String token ) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken( token );
            expiration = claims.getExpiration();
        } catch ( Exception e ) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * @param token
     * @return
     */
    public String getAudienceFromToken ( String token ) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken( token );
            audience = ( String ) claims.get( CLAIM_KEY_AUDIENCE );
        } catch ( Exception e ) {
            audience = null;
        }
        return audience;
    }

    /**
     * @param token
     * @return
     */
    private Claims getClaimsFromToken ( String token ) {
        Claims claims;
        try {
            claims = Jwts.parser()
                         .setSigningKey( secret ) // 签名密钥
                         .parseClaimsJws( token ) // token
                         .getBody();
        } catch ( Exception e ) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成过期时间
     *
     * @return
     */
    private Date generateExpirationDate () {
        return DateUtils.addSecond( new Date() , expiration );
    }

    /**
     * token是否过期
     *
     * @param token
     * @return expiration > 当前时间
     */
    private Boolean isTokenExpired ( String token ) {
        final Date expiration = getExpirationDateFromToken( token );
        return expiration.before( new Date() );
    }

    /**
     * 当前时间 > 密码最后重置时间
     *
     * @param created
     * @param lastPasswordReset
     * @return created > lastPasswordReset
     */
    private Boolean isCreatedBeforeLastPasswordReset ( Date created , Date lastPasswordReset ) {
        return ( lastPasswordReset != null && created.before( lastPasswordReset ) );
    }

    /**
     * <p>终端</p>
     *
     * @param device : 模型的用户代理或设备提交当前请求
     * @return
     */
    private String generateAudience ( Device device ) {
        String audience = AUDIENCE_UNKNOWN;
        if ( device.isNormal() ) {
            audience = AUDIENCE_WEB;
        } else if ( device.isTablet() ) {
            audience = AUDIENCE_TABLET;
        } else if ( device.isMobile() ) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    /**
     * 是否忽略token过期(移动设备则会忽略)
     *
     * @param token
     * @return
     */
    private Boolean ignoreTokenExpiration ( String token ) {
        String audience = getAudienceFromToken( token );
        // 如果是移动设备则忽略
        return ( AUDIENCE_TABLET.equals( audience ) || AUDIENCE_MOBILE.equals( audience ) );
    }

    /**
     * 构建token
     *
     * @param userDetails
     * @param device
     * @return
     */
    public String generateToken ( UserDetails userDetails , Device device ) {
        Map< String, Object > claims = new HashMap<>();
        claims.put( CLAIM_KEY_USERNAME , userDetails.getUsername() );
        claims.put( CLAIM_KEY_AUDIENCE , generateAudience( device ) );
        claims.put( CLAIM_KEY_CREATED , new Date() );
        return generateToken( claims );
    }

    /**
     * 构建token
     *
     * @param claims
     * @return
     */
    String generateToken ( Map< String, Object > claims ) {
        return Jwts.builder()
                   .setClaims( claims )
                   .setExpiration( generateExpirationDate() )
                   .signWith( SignatureAlgorithm.HS512 , secret )
                   .compact();
    }

    /**
     * token是否可以刷新
     *
     * @param token
     * @param lastPasswordReset : 密码重置日期
     * @return
     */
    public Boolean canTokenBeRefreshed ( String token , Date lastPasswordReset ) {
        final Date created = getCreatedDateFromToken( token );
        return ! isCreatedBeforeLastPasswordReset( created , lastPasswordReset )
            && ( ! isTokenExpired( token ) || ignoreTokenExpiration( token ) );
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken ( String token ) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken( token );
            claims.put( CLAIM_KEY_CREATED , new Date() );
            refreshedToken = generateToken( claims );
        } catch ( Exception e ) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证token
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken ( String token , UserDetails userDetails ) {
        BasicJwtUser user     = ( BasicJwtUser ) userDetails;
        final String username = this.getUsernameFromToken( token );
        final Date   created  = this.getCreatedDateFromToken( token );
        return ( username.equals( user.getUsername() ) // 用户名校验
            && ! isTokenExpired( token )           // token有效期校验
            && ! isCreatedBeforeLastPasswordReset( created , user.getLastPasswordResetDate() ) // 密码是否被修改
        );
    }
}
