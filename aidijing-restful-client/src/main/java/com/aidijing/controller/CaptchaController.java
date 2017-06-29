package com.aidijing.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.aidijing.ContextUtils;
import com.aidijing.common.GlobalConstant;
import com.aidijing.common.util.LogUtils;
import com.aidijing.permission.NotNeedPermission;
import com.aidijing.permission.Pass;
import com.aidijing.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码控制器
 *
 * @author 披荆斩棘
 * @since 2017-06-28
 */
@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private CaptchaService captchaService;


    /**
     * 登录验证码,不需要任何权限即可获取
     *
     * @param response
     * @param request
     */
    @Pass
    @GetMapping( "captcha/login" )
    public void captchaLogin ( HttpServletResponse response, HttpServletRequest request ) {
        printCaptcha( response, request.getRequestedSessionId() );
    }

    /**
     * 获取验证码
     *
     * @param response
     * @param request
     */
    @NotNeedPermission
    @GetMapping( "captcha" )
    public void captcha ( HttpServletResponse response, HttpServletRequest request ) {
        printCaptcha( response, GlobalConstant.CAPTCHA_REQUEST_HEADER_KEY + ":" + ContextUtils.getUserId() );
    }

    /**
     * 输出图片验证码,并存储验证码图片的值,其有效期 {@link com.aidijing.common.GlobalConstant#CAPTCHA_TIME_OUT_SECOND}
     *
     * @param response
     * @param captchaStoreKey : 验证码存储的key
     */
    private void printCaptcha ( HttpServletResponse response, String captchaStoreKey ) {
        final String text = defaultKaptcha.createText();

        captchaService.set( captchaStoreKey, text );

        if ( LogUtils.getLogger().isDebugEnabled() ) {
            LogUtils.getLogger().debug( "当前sessionId : {},验证码 : {}", captchaStoreKey, text );
        }

        response.setDateHeader( "Expires", 0 );
        response.setHeader( "Cache-Control", "no-store, no-cache, must-revalidate" );
        response.setHeader( "Pragma", "no-cache" );
        response.setContentType( MediaType.IMAGE_JPEG_VALUE );

        try ( ServletOutputStream out = response.getOutputStream() ) {
            ImageIO.write( defaultKaptcha.createImage( text ), "jpg", out );
        } catch ( IOException e ) {
            if ( LogUtils.getLogger().isDebugEnabled() ) {
                LogUtils.getLogger().debug( "验证码异常", e );
            }
        }
    }


}
