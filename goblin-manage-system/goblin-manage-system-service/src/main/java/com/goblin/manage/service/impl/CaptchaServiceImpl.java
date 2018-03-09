package com.goblin.manage.service.impl;

import com.goblin.cache.client.JedisClient;
import com.goblin.common.GlobalConstant;
import com.goblin.manage.service.CaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/27
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private JedisClient jedisClient;

    @Override
    public boolean set ( String captchaStoreKey, String captcha ) {
        return "ok".equalsIgnoreCase(
                jedisClient.setex( captchaStoreKey, captcha, GlobalConstant.CAPTCHA_TIME_OUT_SECOND )
        );
    }

    @Override
    public boolean validate ( String sessionId, String inputCaptcha ) {
        if ( StringUtils.isEmpty( inputCaptcha ) ) {
            return false;
        }
        if ( ! inputCaptcha.equalsIgnoreCase( this.jedisClient.get( sessionId ) ) ) {
            return false;
        }
        this.clear( sessionId );
        return true;
    }

    @Override
    public boolean clear ( String sessionId ) {
        return jedisClient.del( sessionId ) > 0;
    }
}
