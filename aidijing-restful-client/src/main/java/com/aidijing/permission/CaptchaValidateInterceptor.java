package com.aidijing.permission;

import com.aidijing.common.GlobalConstant;
import com.aidijing.common.util.AssertUtils;
import com.aidijing.common.util.LogUtils;
import com.aidijing.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 验证码校验拦截器 
 * <p>
 * 请求头中key {@link GlobalConstant#CAPTCHA_REQUEST_HEADER_KEY} ,value 为输入的验证码值
 *  {@link NeedCaptcha}
 * @author : 披荆斩棘
 * @date : 2017/6/14
 */
public class CaptchaValidateInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private CaptchaService captchaService;

    @Override
    public boolean preHandle ( HttpServletRequest request, HttpServletResponse response, Object handler ) throws
                                                                                                          Exception {
        if ( ! ( handler instanceof HandlerMethod ) ) {
            return false;
        }

        HandlerMethod handlerMethod = ( HandlerMethod ) handler;

        final NeedCaptcha needCaptcha = handlerMethod.getMethodAnnotation( NeedCaptcha.class );
        

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "needCaptcha : {}", needCaptcha );
        }

        if ( Objects.isNull( needCaptcha ) ) {
            return true;
        }

        final String sessionId    = request.getRequestedSessionId();
        final String inputCaptcha = request.getHeader( GlobalConstant.CAPTCHA_REQUEST_HEADER_KEY );

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "sessionId : {},inputCaptcha : {}", sessionId, inputCaptcha );
        }

        AssertUtils.assertCaptchaIsTrue( ! captchaService.validate( sessionId, inputCaptcha ), "验证码错误" );

        return true;
    }


}

















