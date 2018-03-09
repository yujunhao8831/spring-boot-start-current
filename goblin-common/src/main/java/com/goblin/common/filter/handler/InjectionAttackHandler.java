package com.goblin.common.filter.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : 披荆斩棘
 * @date : 2017/1/17
 * 字符注入攻击判断
 */
public interface InjectionAttackHandler {

    /**
     * 是注入攻击(SQL,XSS,特殊字符注入)
     *
     * @param rawCharacters
     * @return
     */
    boolean isInjectionAttack ( String rawCharacters );

    boolean isInjectionAttack ( String parameters , String[] ignoreStrings );

    /**
     * 是SQL注入
     *
     * @param rawCharacters
     * @return
     */
    boolean isSqlInjectionAttack ( String rawCharacters );

    boolean isSqlInjectionAttack ( String rawCharacters , String[] ignoreStrings );

    /**
     * 是XSS攻击
     *
     * @param rawCharacters
     * @return
     */
    boolean isXSSInjectionAttack ( String rawCharacters );

    boolean isXSSInjectionAttack ( String rawCharacters , String[] ignoreStrings );

    /**
     * 是特殊字符注入
     *
     * @param rawCharacters
     * @return
     */
    boolean isSpecialCharactersInjectionAttack ( String rawCharacters );

    boolean isSpecialCharactersInjectionAttack ( String rawCharacters , String[] ignoreStrings );

    /**
     * 过滤(SQL,XSS,特殊字符注入)
     *
     * @param rawCharacters : 原字符
     * @return 过滤后的字符
     */
    String filter ( String rawCharacters );

    /**
     * 过滤SQL注入
     *
     * @param rawCharacters : 原字符
     * @return 过滤后的字符
     */
    String filterSqlInjection ( String rawCharacters );

    /**
     * 过滤XSS注入
     *
     * @param rawCharacters : 原字符
     * @return 过滤后的字符
     */
    String filterXSSInjection ( String rawCharacters );

    /**
     * 过滤特殊字符
     *
     * @param rawCharacters : 原字符
     * @return 过滤后的字符
     */
    String filterSpecialCharacters ( String rawCharacters );


    /**
     * 攻击处理
     *
     * @param request
     * @param response
     * @param parameters
     */
    void attackHandle ( HttpServletRequest request , HttpServletResponse response , String parameters ) throws
                                                                                                        IOException;


}
