package com.aidijing.common.filter;


import com.aidijing.common.util.LogUtils;
import com.aidijing.common.util.RequestUtils;
import org.apache.logging.log4j.ThreadContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求信息打印过滤器
 *
 * @author : 披荆斩棘
 * @date : 2017/1/17
 */
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter ( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException,
                                                                                                        ServletException {
        final BodyReaderWrapper wrapper = new BodyReaderWrapper( ( HttpServletRequest ) request );
        LogUtils.getLogger().info( RequestUtils.getRequestMessage( wrapper ) );
        chain.doFilter( wrapper, response );
        ThreadContext.clearAll();
    }

    @Override
    public void init ( FilterConfig filterConfig ) throws ServletException {
    }

    @Override
    public void destroy () {
    }

}
