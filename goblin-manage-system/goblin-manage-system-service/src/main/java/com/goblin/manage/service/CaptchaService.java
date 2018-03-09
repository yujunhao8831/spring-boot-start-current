package com.goblin.manage.service;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/27
 */
public interface CaptchaService {

    /**
     * 存储验证码
     *
     * @param captchaStoreKey : 未登录用户,使用sessionId标识
     * @param captcha         : 验证码
     */
    boolean set ( String captchaStoreKey , String captcha );

    /**
     * 验证验证码,如果验证码验证通过则会清除掉这个key
     *
     * @param sessionId    : 未登录用户,使用sessionId标识
     * @param inputCaptcha : 用户输入的验证码
     * @return 如果正确返回 <code>true</code>
     */
    boolean validate ( String sessionId , String inputCaptcha );

    /**
     * 清除验证码
     *
     * @param sessionId
     * @return 如果清除成功返回 <code>true</code>
     */
    boolean clear ( String sessionId );
}
