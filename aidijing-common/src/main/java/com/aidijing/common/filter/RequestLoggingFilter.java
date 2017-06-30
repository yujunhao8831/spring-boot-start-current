package com.aidijing.common.filter;


import com.aidijing.common.util.LogUtils;
import com.aidijing.common.util.RequestUtils;
import org.apache.commons.lang3.StringUtils;
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
        if ( LogUtils.getLogger().isInfoEnabled() ) {
            String requestMessage = RequestUtils.getRequestMessage( wrapper );
            if ( ! LogUtils.getLogger().isDebugEnabled() ) {
                requestMessage = StringUtils.replaceAll(
                        requestMessage,
                        "(password=\\[([\\S\\s])*\\])|(\"password\":\"([\\S\\s])*\")",
                        "enable password protection, if not debug so do not see"
                );
            }
            LogUtils.getLogger().info( requestMessage );
        }
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
