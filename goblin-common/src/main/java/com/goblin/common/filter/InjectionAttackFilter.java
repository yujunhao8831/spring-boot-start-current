/**
 * Copyright (c) 2011 Andrew C Slocum
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
package com.goblin.common.filter;

import com.goblin.common.filter.handler.DefaultInjectionAttackHandler;
import com.goblin.common.filter.handler.InjectionAttackHandler;
import com.goblin.common.util.LogUtils;
import com.goblin.common.util.RequestUtils;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

/**
 * 改造 https://github.com/acslocum/InjectionAttackFilter 防范SQL注入和跨站脚本攻击
 * @see com.goblin.common.interceptor.InjectionAttackInterceptor
 * @author : 披荆斩棘
 * @date : 2016/10/10
 */
@Getter
@Setter
@Deprecated
public class InjectionAttackFilter implements Filter {

    private static final String    X_FRAME_VALUE           = "SAMEORIGIN";
    private static final String    X_FRAME_HEADER          = "X-FRAME-OPTIONS";
    private static       boolean   HAS_BODY_READER_WRAPPER = false;
    private static       LongAdder LONG_ADDER              = new LongAdder();
    /**
     * 不需要过滤的URI
     */
    private Set< String >          passUris;
    /**
     * 注入攻击处理器
     */
    private InjectionAttackHandler injectionAttackHandler;

    public InjectionAttackFilter () {
        // 注入SQL注入和跨站脚本攻击处理器
        this.injectionAttackHandler = DefaultInjectionAttackHandler.getInstance();
        this.passUris = Collections.EMPTY_SET;
    }

    public InjectionAttackFilter ( Set< String > passUris ,
                                   InjectionAttackHandler injectionAttackHandler ) {
        this.passUris = passUris;
        this.injectionAttackHandler = injectionAttackHandler;
    }

    @Override
    public void doFilter ( ServletRequest servletRequest , ServletResponse servletResponse ,
                           FilterChain filterChain ) throws IOException, ServletException {

        if ( this.pass( servletRequest ) ) {
            filterChain.doFilter( servletRequest , servletResponse );
            return;
        }

        if ( ! HAS_BODY_READER_WRAPPER && LONG_ADDER.intValue() <= 0 ) {
            LONG_ADDER.increment();
            HAS_BODY_READER_WRAPPER = servletRequest instanceof BodyReaderWrapper;
        }

        HttpServletRequest request;
        if ( ! HAS_BODY_READER_WRAPPER ) {
            request = new BodyReaderWrapper( ( HttpServletRequest ) servletRequest );
        } else {
            request = ( HttpServletRequest ) servletRequest;
        }

        HttpServletResponse response = ( HttpServletResponse ) servletResponse;

        final String parameters = RequestUtils.getRequestParameters( request );

        // 参数注入攻击处理
        if ( this.isInjectionAttack( parameters ) ) {
            LogUtils.getLogger().debug( "参数 {} 被判断为注入攻击" , parameters );
            this.injectionHandle( request , response , parameters );
            return;
        }
        // URI注入攻击处理
        if ( this.isInjectionAttack( request.getRequestURI() ) ) {
            LogUtils.getLogger().debug( "URI {} 被判断为注入攻击" , parameters );
            this.injectionHandle( request , response , request.getRequestURI() );
            return;
        }
        // iframe攻击处理
        this.filterClickJack( response );
        filterChain.doFilter( this.requestWrapper( request ) , response );
    }

    protected boolean pass ( ServletRequest servletRequest ) {
        final HttpServletRequest request = ( HttpServletRequest ) servletRequest;
        return passUris.contains( request.getRequestURI() );
    }

    /**
     * 请求包装
     *
     * @param request
     * @return
     */
    protected HttpServletRequest requestWrapper ( HttpServletRequest request ) {
        return request;
    }


    /**
     * 跨域攻击处理
     *
     * @param response
     */
    private void filterClickJack ( HttpServletResponse response ) {
        if ( ! response.containsHeader( X_FRAME_HEADER ) ) {
            /** 使用 X-Frame-Options 防止被iframe 造成跨域iframe 提交挂掉 **/
            response.addHeader( X_FRAME_HEADER , X_FRAME_VALUE );
        }
    }


    /**
     * 是注入攻击
     *
     * @param parameters
     * @return
     */
    private boolean isInjectionAttack ( final String parameters ) {
        return this.injectionAttackHandler.isInjectionAttack( parameters );
    }

    /**
     * 注入处理
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    protected void injectionHandle ( HttpServletRequest request , HttpServletResponse response ,
                                     String parameters ) throws IOException {
        this.injectionAttackHandler.attackHandle( request , response , parameters );
    }


    @Override
    public void init ( FilterConfig config ) throws ServletException {
    }

    @Override
    public void destroy () {
    }

}
