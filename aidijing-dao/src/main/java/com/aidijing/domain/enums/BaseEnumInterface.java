package com.aidijing.domain.enums;

/**
 * @author : 披荆斩棘
 * @date : 2017/6/17
 */
public interface BaseEnumInterface < T > {

    /**
     * 校验这个code是否是枚举中所定义的code值
     *
     * @param inputCode
     * @return {@code true} 如果这个值<b style="color:red">匹配</b>枚举中所定义的code值.
     */
    boolean isEnumCode ( final String inputCode );

    /**
     * 校验这个code是否是枚举中所定义的code值
     *
     * @param inputCode
     * @return {@code true} 如果这个值<b style="color:red">不匹配</b>枚举中所定义的code值.
     */
    boolean isNotEnumCode ( final String inputCode );

    /**
     * 返回这个code所对应的注释(释义)
     *
     * @param inputCode
     * @return code所定义的注释 , <b style="color:red">如果输入值不匹配,则返回{@code null}</b>
     */
    String getCodeComment ( final String inputCode );

    /**
     * 得到这个code所对应的{@code enum}
     *
     * @param inputCode
     * @return 返回这个code所对应 {@code enum} , <b style="color:red">如果输入值不匹配,则返回{@code null}</b>
     */
    T getEnum ( final String inputCode );

}
