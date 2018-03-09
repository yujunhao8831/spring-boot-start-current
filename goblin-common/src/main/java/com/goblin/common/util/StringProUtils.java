package com.goblin.common.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.*;


/**
 * @author : 披荆斩棘
 * @date : 2017/7/26
 */
public final class StringProUtils {
    public final static String  SPRIT                = "/";
    /* V_2V */
    static              boolean separatorBeforeDigit = false;
    /* V2_V */
    static              boolean separatorAfterDigit  = true; // false might not work totally fine e.g. ToHyphenCaseActionTest


    /**
     * {@link ObjectUtils#defaultIfNull(Object , Object)}
     */
    public static String getString ( Object object ) {
        return ObjectUtils.defaultIfNull( object , StringUtils.EMPTY ).toString();
    }

    /**
     * <p>
     * 第一个首字母小写,之后字符大小写的不变<br>
     * StringUtils.firstCharToLower( "UserService" )     = userService
     * StringUtils.firstCharToLower( "UserServiceImpl" ) = userServiceImpl
     * </p>
     *
     * @param rawString 需要处理的字符串
     * @return
     */
    public static String firstCharToLower ( String rawString ) {
        return prefixToLower( rawString , 1 );
    }

    /**
     * <p>
     * 第一个首字母大写,之后字符大小写的不变<br>
     * StringUtils.firstCharToLower( "userService" )     = UserService
     * StringUtils.firstCharToLower( "userServiceImpl" ) = UserServiceImpl
     * </p>
     *
     * @param rawString 需要处理的字符串
     * @return
     */
    public static String firstCharToUpperCase ( String rawString ) {
        return prefixToUpperCase( rawString , 1 );
    }

    /**
     * <p>
     * 前n个首字母大写,之后字符大小写的不变
     * </p>
     *
     * @param rawString 需要处理的字符串
     * @param index     多少个字符(从左至右)
     * @return
     */
    public static String prefixToUpperCase ( String rawString , int index ) {
        String beforeChar = StringUtils.substring( rawString , 0 , index ).toUpperCase();
        String afterChar  = StringUtils.substring( rawString , index , rawString.length() );
        return beforeChar + afterChar;
    }

    /**
     * <p>
     * 前n个首字母小写,之后字符大小写的不变
     * </p>
     *
     * @param rawString 需要处理的字符串
     * @param index     多少个字符(从左至右)
     * @return
     */
    public static String prefixToLower ( String rawString , int index ) {
        String beforeChar = StringUtils.substring( rawString , 0 , index ).toLowerCase();
        String afterChar  = StringUtils.substring( rawString , index , rawString.length() );
        return beforeChar + afterChar;
    }

    /**
     * <p>
     * 删除字符前缀之后,首字母小写,之后字符大小写的不变<br>
     * StringUtils.removePrefixAfterPrefixToLower( "isUser", 2 )     = user
     * StringUtils.removePrefixAfterPrefixToLower( "isUserInfo", 2 ) = userInfo
     * </p>
     *
     * @param rawString 需要处理的字符串
     * @param index     删除多少个字符(从左至右)
     * @return
     */
    public static String removePrefixAfterPrefixToLower ( String rawString , int index ) {
        return prefixToLower( StringUtils.substring( rawString , index , rawString.length() ) , 1 );
    }

    /**
     * <p>
     * 驼峰转连字符<br>
     * StringUtils.camelToHyphen( "managerAdminUserService" ) = manager-admin-user-service
     * </p>
     *
     * @param input
     * @return 以'-'分隔
     * @see <a href="https://github.com/krasa/StringManipulation">document</a>
     */
    public static String camelToHyphen ( String input ) {
        return wordsToHyphenCase( wordsAndHyphenAndCamelToConstantCase( input ) );
    }


    public static String camelToText ( String s ) {
        StringBuilder buf      = new StringBuilder();
        char          lastChar = ' ';
        for ( char c : s.toCharArray() ) {
            char nc = c;
            if ( isUpperCase( nc ) && ! isUpperCase( lastChar ) ) {
                if ( lastChar != ' ' && isLetterOrDigit( lastChar ) ) {
                    buf.append( " " );
                }
                nc = Character.toLowerCase( c );
            } else if ( ( separatorAfterDigit && isDigit( lastChar ) && ! isDigit( c ) )
                || ( separatorBeforeDigit && isDigit( c ) && ! isDigit( lastChar ) ) ) {
                if ( lastChar != ' ' ) {
                    buf.append( " " );
                }
                nc = Character.toLowerCase( c );
            }

            if ( lastChar != ' ' || c != ' ' ) {
                buf.append( nc );
            }
            lastChar = c;
        }
        return buf.toString();
    }


    private static boolean startsWithLetter ( String word ) {
        return word.length() > 0 && isLetter( word.charAt( 0 ) );
    }

    private static boolean isNotQuote ( String word ) {
        return ! "\"".equals( word ) && ! "\'".equals( word );
    }

    public static String wordsToConstantCase ( String s ) {
        StringBuilder buf = new StringBuilder();

        char lastChar = 'a';
        for ( char c : s.toCharArray() ) {
            if ( isWhitespace( lastChar ) && ( ! isWhitespace( c ) && '_' != c ) &&
                buf.length() > 0 && buf.charAt( buf.length() - 1 ) != '_' ) {
                buf.append( "_" );
            }
            if ( ! isWhitespace( c ) ) {
                buf.append( Character.toUpperCase( c ) );

            }
            lastChar = c;
        }
        if ( isWhitespace( lastChar ) ) {
            buf.append( "_" );
        }


        return buf.toString();

    }

    public static String wordsAndHyphenAndCamelToConstantCase ( String s ) {
        boolean _betweenUpperCases = false;
        boolean containsLowerCase  = containsLowerCase( s );

        StringBuilder buf          = new StringBuilder();
        char          previousChar = ' ';
        char[]        chars        = s.toCharArray();
        for ( int i = 0 ; i < chars.length ; i++ ) {
            char    c                                 = chars[i];
            boolean isUpperCaseAndPreviousIsUpperCase = isUpperCase( previousChar ) && isUpperCase( c );
            boolean isUpperCaseAndPreviousIsLowerCase = isLowerCase( previousChar ) && isUpperCase( c );
            // boolean isLowerCaseLetter = !isWhitespace(c) && '_' != c && !isUpperCase(c);
            // boolean isLowerCaseAndPreviousIsWhitespace = isWhitespace(lastChar) && isLowerCaseLetter;
            boolean previousIsWhitespace   = isWhitespace( previousChar );
            boolean lastOneIsNotUnderscore = buf.length() > 0 && buf.charAt( buf.length() - 1 ) != '_';
            boolean isNotUnderscore        = c != '_';
            //  ORIGINAL      if (lastOneIsNotUnderscore && (isUpperCase(c) || isLowerCaseAndPreviousIsWhitespace)) {


            //camelCase handling - add extra _
            if ( lastOneIsNotUnderscore && ( isUpperCaseAndPreviousIsLowerCase || previousIsWhitespace
                || ( _betweenUpperCases && containsLowerCase && isUpperCaseAndPreviousIsUpperCase ) ) ) {
                buf.append( "_" );
            } else if ( ( separatorAfterDigit && isDigit( previousChar ) && isLetter( c ) )
                || ( separatorBeforeDigit && isDigit( c ) && isLetter( previousChar ) ) ) { // extra _ after number
                buf.append( '_' );
            }

            if ( shouldReplace( c ) && lastOneIsNotUnderscore ) {
                buf.append( '_' );
            } else if ( ! isWhitespace( c ) && ( isNotUnderscore || lastOneIsNotUnderscore ) ) {
                // uppercase anything, do not add whitespace, do not add _ if there was previously
                buf.append( Character.toUpperCase( c ) );
            }

            previousChar = c;
        }
        if ( isWhitespace( previousChar ) ) {
            buf.append( "_" );
        }


        return buf.toString();
    }

    private static boolean shouldReplace ( char c ) {
//		return !isLetterOrDigit(c) && !isSlash(c) && lastOneIsNotUnderscore && !isNotBorderQuote(c, i, chars);       //replace special chars to _ (not quotes, no double _)
        return c == '.' || c == '_' || c == '-';
    }

    private static boolean isSlash ( char c ) {
        return c == '\\' || c == '/';
    }

    private static boolean isNotBorderQuote ( char actualChar , int i , char[] chars ) {
        if ( chars.length - 1 == i ) {
            char firstChar = chars[0];
            if ( isQuote( actualChar ) && isQuote( firstChar ) ) {
                return true;
            }
        }
        return false;
    }

    private static boolean isQuote ( char actualChar ) {
        return actualChar == '\'' || actualChar == '\"';
    }

    public static String toDotCase ( String s ) {
        StringBuilder buf = new StringBuilder();

        char lastChar = ' ';
        for ( char c : s.toCharArray() ) {
            boolean isUpperCaseAndPreviousIsLowerCase = isLowerCase( lastChar ) && isUpperCase( c );
            boolean previousIsWhitespace              = isWhitespace( lastChar );
            boolean lastOneIsNotUnderscore            = buf.length() > 0 && buf.charAt( buf.length() - 1 ) != '.';
            if ( lastOneIsNotUnderscore && ( isUpperCaseAndPreviousIsLowerCase || previousIsWhitespace ) ) {
                buf.append( "." );
            } else if ( ( separatorAfterDigit && isDigit( lastChar ) && isLetter( c ) )
                || ( separatorBeforeDigit && isDigit( c ) && isLetter( lastChar ) ) ) {
                buf.append( "." );
            }

            if ( c == '.' ) {
                buf.append( '.' );
            } else if ( c == '-' ) {
                buf.append( '.' );
            } else if ( c == '_' ) {
                buf.append( '.' );
            } else if ( ! isWhitespace( c ) ) {
                buf.append( Character.toLowerCase( c ) );
            }

            lastChar = c;
        }
        if ( isWhitespace( lastChar ) ) {
            buf.append( "." );
        }


        return buf.toString();
    }

    /**
     * <p>Splits the given input sequence around matches of this pattern.<p/>
     * <p/>
     * <p> The array returned by this method contains each substring of the input sequence
     * that is terminated by another subsequence that matches this pattern or is terminated by
     * the end of the input sequence.
     * The substrings in the array are in the order in which they occur in the input.
     * If this pattern does not match any subsequence of the input then the resulting array
     * has just one element, namely the input sequence in string form.<p/>
     * <p/>
     * <pre>
     * splitPreserveAllTokens("boo:and:foo", ":") =  { "boo", ":", "and", ":", "foo"}
     * splitPreserveAllTokens("boo:and:foo", "o") =  { "b", "o", "o", ":and:f", "o", "o"}
     * </pre>
     *
     * @param input The character sequence to be split
     * @return The array of strings computed by splitting the input around matches of this pattern
     */
    public static String[] splitPreserveAllTokens ( String input , String regex ) {
        int                 index  = 0;
        Pattern             p      = Pattern.compile( regex );
        ArrayList< String > result = new ArrayList< String >();
        Matcher             m      = p.matcher( input );

        // Add segments before each match found
        int lastBeforeIdx = 0;
        while ( m.find() ) {
            if ( StringUtils.isNotEmpty( m.group() ) ) {
                String match = input.subSequence( index , m.start() ).toString();
                if ( StringUtils.isNotEmpty( match ) ) {
                    result.add( match );
                }
                result.add( input.subSequence( m.start() , m.end() ).toString() );
                index = m.end();
            }
        }

        // If no match was found, return this
        if ( index == 0 ) {
            return new String[]{ input };
        }


        final String remaining = input.subSequence( index , input.length() ).toString();
        if ( StringUtils.isNotEmpty( remaining ) ) {
            result.add( remaining );
        }

        // Construct result
        return result.toArray( new String[result.size()] );

    }


    public static String nonAsciiToUnicode ( String s ) {
        StringBuffer sb = new StringBuffer( s.length() );
        for ( Character c : s.toCharArray() ) {
            if ( ! CharUtils.isAscii( c ) ) {
                sb.append( CharUtils.unicodeEscaped( c ) );
            } else {
                sb.append( c );
            }
        }

        return sb.toString();
    }

    public static String wordsToHyphenCase ( String s ) {
        StringBuilder buf      = new StringBuilder();
        char          lastChar = 'a';
        for ( char c : s.toCharArray() ) {
            if ( isWhitespace( lastChar ) && ( ! isWhitespace( c ) && '-' != c ) && buf.length() > 0
                && buf.charAt( buf.length() - 1 ) != '-' ) {
                buf.append( "-" );
            }
            if ( '_' == c ) {
                buf.append( '-' );
            } else if ( '.' == c ) {
                buf.append( '-' );
            } else if ( ! isWhitespace( c ) ) {
                buf.append( toLowerCase( c ) );
            }
            lastChar = c;
        }
        if ( isWhitespace( lastChar ) ) {
            buf.append( "-" );
        }
        return buf.toString();
    }

    public static boolean containsLowerCase ( String s ) {
        for ( char c : s.toCharArray() ) {
            if ( isLowerCase( c ) ) {
                return true;
            }
        }
        return false;
    }

    public static int indexOfAnyButWhitespace ( String cs ) {
        if ( StringUtils.isEmpty( cs ) ) {
            return cs.length();
        }
        final int csLen = cs.length();
        for ( int i = 0 ; i < csLen ; i++ ) {
            final char ch = cs.charAt( i );
            if ( isWhitespace( ch ) ) {
                continue;
            }
            return i;
        }
        return cs.length();
    }


}
