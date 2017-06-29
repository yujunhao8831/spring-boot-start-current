package com.aidijing.common.filter.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

/**
 * @author : 披荆斩棘
 * @date : 2017/1/17
 * 默认的注入攻击处理器
 */
public class DefaultInjectionAttackHandler implements InjectionAttackHandler {

    ///////////////////////////////////////////////////////////////////////////
    // XSS正则
    ///////////////////////////////////////////////////////////////////////////
    private static final String  EVENTS                           = "((?i)onload|onunload|onchange|onsubmit|onreset"
            + "|onselect|onblur|onfocus|onkeydown|onkeypress|onkeyup"
            + "|onclick|ondblclick|onmousedown|onmousemove|onmouseout|onmouseover|onmouseup)";
    private static final String  XSS_HTML_TAG                     = "(%3C)|(%3E)|[<>]+";
    private static final String  XSS_INJECTION                    = "((%22%20)|(%22\\s)|('%22)|(%22\\+))\\w.*|(\\s|%20)" + EVENTS + ".*|(%3D)|(%7C)";
    public static final  String  XSS_REGEX                        = XSS_HTML_TAG + "|" + XSS_INJECTION;
    ///////////////////////////////////////////////////////////////////////////
    // SQL注入正则
    ///////////////////////////////////////////////////////////////////////////
    private static final String  SQL_INJECTION_2                  = "('.+--)|(--)|(\\|)|(%7C)";
    private static final String  SQL_INJECTION_1                  = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(?i)(\\b(and|exec|count|chr|mid|master|or|truncate|char|declare|join|insert|select|delete|update|create|drop)\\b)";
    public static final  String  SQL_INJECTION_REGEX              = SQL_INJECTION_1 + "|" + SQL_INJECTION_2;
    ///////////////////////////////////////////////////////////////////////////
    // 特殊字符正则
    ///////////////////////////////////////////////////////////////////////////
    private static final String  SPECIAL_CHARACTERS_2             = "(0x0d)|(0x0a)";
    private static final String  SPECIAL_CHARACTERS_1             = "(<)|(>)|(/\\*)|(\\*/)|(')|(\\|)|(;)|(\\$)|(\\B\"\\B)|(\')|(\\\\\")|(\\(\\))|(\\B\\+\\B)|(,)";
    public static final  String  SPECIAL_CHARACTERS_REGEX         = SPECIAL_CHARACTERS_1 + "|" + SPECIAL_CHARACTERS_2;
    ///////////////////////////////////////////////////////////////////////////
    // SQL注入
    ///////////////////////////////////////////////////////////////////////////
    private static final Pattern SQL_INJECTION_REGEX_PATTERN      = Pattern.compile(
            SQL_INJECTION_REGEX,
            Pattern.CASE_INSENSITIVE
    );
    ///////////////////////////////////////////////////////////////////////////
    // XSS
    ///////////////////////////////////////////////////////////////////////////
    private static final Pattern XSS_REGEX_PATTERN                = Pattern.compile(
            XSS_REGEX,
            Pattern.CASE_INSENSITIVE
    );
    ///////////////////////////////////////////////////////////////////////////
    // 特殊字符
    ///////////////////////////////////////////////////////////////////////////
    private static final Pattern SPECIAL_CHARACTERS_REGEX_PATTERN = Pattern.compile(
            SPECIAL_CHARACTERS_REGEX,
            Pattern.CASE_INSENSITIVE
    );
    private static final String  EMPTY                            = "";

    private DefaultInjectionAttackHandler () {
    }

    public static DefaultInjectionAttackHandler getInstance () {
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
        return SQL_INJECTION_REGEX_PATTERN.matcher( rawCharacters ).find();
    }

    @Override
    public boolean isXSSInjectionAttack ( String rawCharacters ) {
        return XSS_REGEX_PATTERN.matcher( rawCharacters ).find();
    }

    @Override
    public boolean isSpecialCharactersInjectionAttack ( String rawCharacters ) {
        return SPECIAL_CHARACTERS_REGEX_PATTERN.matcher( rawCharacters ).find();
    }

    @Override
    public String filter ( String rawCharacters ) {
        return this.filterSpecialCharacters( this.filterXSSInjection( this.filterSqlInjection( rawCharacters ) ) );
    }

    @Override
    public String filterSqlInjection ( String rawCharacters ) {
        if ( null == rawCharacters ) {
            return rawCharacters;
        }
        return rawCharacters.replaceAll( SQL_INJECTION_REGEX, EMPTY );
    }

    @Override
    public String filterXSSInjection ( String rawCharacters ) {
        if ( null == rawCharacters ) {
            return rawCharacters;
        }
        return rawCharacters.replaceAll( XSS_REGEX, EMPTY );
    }

    @Override
    public String filterSpecialCharacters ( String rawCharacters ) {
        if ( null == rawCharacters ) {
            return rawCharacters;
        }
        return rawCharacters.replaceAll( SPECIAL_CHARACTERS_REGEX, EMPTY );
    }

    @Override
    public void attackHandle ( HttpServletRequest request, HttpServletResponse response, String parameters ) throws
                                                                                                             IOException {
        try ( PrintWriter out = response.getWriter() ) {
            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
            out.print( "请求内容包含非法字符,原请求内容:\n" + parameters );
        }
    }

    private static class SingletonDefaultInjectionAttackHandler {
        private static final DefaultInjectionAttackHandler instance = new DefaultInjectionAttackHandler();
    }


}
