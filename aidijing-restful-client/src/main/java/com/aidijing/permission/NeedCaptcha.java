package com.aidijing.permission;

import com.aidijing.common.exception.CaptchaException;

import java.lang.annotation.*;

/**
 * 需要进行验证码校验
 * <p>
 * 注意 : 如果某个controller方法需要进行验证码校验,只需在这个方法上加入该注解 <p>
 * 请求这个方法时,在请求头中以 {@link com.aidijing.common.GlobalConstant#CAPTCHA_REQUEST_HEADER_KEY} 为key ,
 * 输入的<b style="color:red">验证码</b>为其值请求即可 <p>
 * 被注解的方法会被 {@link CaptchaValidateInterceptor} 拦截,进行验证码校验,
 * 如果验证码错误则会 {@link com.aidijing.common.exception.CaptchaException} 异常 <p>
 * 异常会被 {@link com.aidijing.GlobalErrorController#captchaErrorHandler(CaptchaException)} 处理
 * <p>
 *
 * @author : 披荆斩棘
 * @date : 2017/6/27
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface NeedCaptcha {
}
