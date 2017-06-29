package com.aidijing.common.filter.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : 披荆斩棘
 * @date : 2017/1/17
 * 对于注入攻击不做任何处理
 */
public class InjectionAttackPassHandle implements InjectionAttackHandler {


    private InjectionAttackPassHandle () {
    }

    public static InjectionAttackPassHandle getInstance () {
        return SingletonDefaultInjectionAttackHandler.instance;
    }

    @Override
    public boolean isInjectionAttack ( String rawCharacters ) {
        return this.isSqlInjectionAttack( rawCharacters ) ||
                this.isXSSInjectionAttack( rawCharacters ) ||
                this.isSpecialCharactersInjectionAttack( rawCharacters );
    }

    @Override
    public boolean isSqlInjectionAttack ( String rawCharacters ) {
        return false;
    }

    @Override
    public boolean isXSSInjectionAttack ( String rawCharacters ) {
        return false;
    }

    @Override
    public boolean isSpecialCharactersInjectionAttack ( String rawCharacters ) {
        return false;
    }

    @Override
    public String filter ( String rawCharacters ) {
        return rawCharacters;
    }

    @Override
    public String filterSqlInjection ( String rawCharacters ) {
        return rawCharacters;
    }

    @Override
    public String filterXSSInjection ( String rawCharacters ) {
        return rawCharacters;
    }

    @Override
    public String filterSpecialCharacters ( String rawCharacters ) {
        return rawCharacters;
    }

    @Override
    public void attackHandle ( HttpServletRequest request, HttpServletResponse response, String parameters ) throws
                                                                                                             IOException {
        return;
    }

    private static class SingletonDefaultInjectionAttackHandler {
        private static final InjectionAttackPassHandle instance = new InjectionAttackPassHandle();
    }


}
