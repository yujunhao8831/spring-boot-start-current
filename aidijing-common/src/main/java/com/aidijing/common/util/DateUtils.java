package com.aidijing.common.util;

import com.aidijing.common.regex.RegexType;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 披荆斩棘
 */
public abstract class DateUtils {

    public static final  String  DATE_BASIC_STYLE          = "yyyy-MM-dd HH:mm:ss";
    public static final  String  DATE_TIMESTAMP_STYLE      = "yyyyMMddHHmmss";
    public static final  String  DATE_TIMESTAMPS_STYLE     = "yyyyMMddHHmmssSSS";
    private static final Pattern DATE_BASIC_PATTERN        = Pattern.compile( RegexType.DATE_BASIC );
    private static final Pattern DATE_BASIC_FORMAT_PATTERN = Pattern.compile( "(\\d{4})-(\\d+)-(\\d+).*" );


    /**
     * 当前月份 + ( xx )
     *
     * @param number : 可以是负数,负数时则是减
     * @return
     */
    public static Date nowAddByMonth ( int number ) {
        Calendar instance = Calendar.getInstance();
        instance.add( Calendar.MONTH, number );
        return instance.getTime();
    }

    /**
     * 得到当月最后一天的'时间'
     *
     * @return
     */
    public static Date nowMonthLast () {
        Calendar now = Calendar.getInstance();
        now.set( Calendar.DATE, now.getActualMaximum( Calendar.DAY_OF_MONTH ) );
        return now.getTime();
    }

    /**
     * 得到当月最后一天
     *
     * @return min:1 , max:31
     */
    public static int nowMonthLastDay () {
        return Calendar.getInstance().getActualMaximum( Calendar.DAY_OF_MONTH );
    }

    /**
     * 根据时间(Date类型)得到指定格式的日期字符串
     *
     * @return
     */
    public static String formatDateByStyle ( final Date date, final String dateStyle ) {
        DateFormat format = new SimpleDateFormat( dateStyle );
        return format.format( date );
    }

    /**
     * 根据时间类型的字符串得到指定格式的日期 <br/>
     *
     * @param date      : 日期
     * @param dateStyle | yyyy : 年
     *                  | MM   : 月
     *                  | dd   : 日
     *                  | HH   : 时
     *                  | mm   : 分
     *                  | ss   : 秒
     * @return
     */
    public static Date formatStringByStyleToDate ( final String date, final String dateStyle ) {
        DateFormat format = new SimpleDateFormat( dateStyle );
        try {
            return format.parse( date );
        } catch ( ParseException e ) {
            return null;
        }
    }

    /**
     * 在指定时间内 + n 月
     *
     * @param date
     * @param number
     * @return
     */
    public static Date addMonth ( Date date, int number ) {
        Calendar instance = Calendar.getInstance();
        instance.setTime( date );
        instance.add( Calendar.MONTH, number );
        return instance.getTime();
    }

    /**
     * 在指定时间内 + n 秒
     *
     * @param date
     * @param number
     * @return
     */
    public static Date addSecond ( Date date, int number ) {
        Calendar instance = Calendar.getInstance();
        instance.setTime( date );
        instance.add( Calendar.SECOND, number );
        return instance.getTime();
    }


    /**
     * 得到两个日期之间的分钟差
     * <pre>
     * DateUtil.getMinuteInterval(  2017-5-4 15:00:08 ,2017-5-4 15:51:08  ) = 51
     * DateUtil.getMinuteInterval(  2017-5-4 15:00:34 ,2017-5-4 15:51:34  ) = 51
     * DateUtil.getMinuteInterval(  2016-5-4 15:56:30 ,2017-5-4 15:56:30  ) = 525600
     * </pre>
     *
     * @param a : Date 类型,不分前后顺序
     * @param b : Date 类型,不分前后顺序
     * @return 日期之间的分钟间隔
     */
    public static long getMinuteInterval ( Date a, Date b ) {
        return Math.abs( ( a.getTime() - b.getTime() ) / ( 1000 * 60 ) );
    }

    /**
     * 得天数间隔时间
     * <pre>
     * DateUtil.getMinuteInterval(  2016-5-4 15:56:30 ,2017-5-4 15:56:30  ) = 365
     * </pre>
     *
     * @param a : Date 类型,不分前后顺序
     * @param b : Date 类型,不分前后顺序
     * @return 日期之间的天数间隔
     */
    public static long getDayInterval ( Date a, Date b ) {
        return Math.abs( ( a.getTime() - b.getTime() ) / ( 1000 * 60 * 60 * 24 ) );
    }

    /**
     * 目前仅支持以下类型的判断
     * <p>
     * 2016-12-19 15:59:45
     * 2016-12-19
     * 2016/12/19 15:59:45
     * 2016/12/19
     * 20161219155945
     * 20161219
     *
     * @param inputTime
     * @return
     */
    public static boolean isDate ( final String inputTime ) {
        Matcher matcher = DATE_BASIC_PATTERN.matcher( inputTime );
        if ( ! matcher.matches() ) {
            return false;
        }
        matcher = DATE_BASIC_FORMAT_PATTERN.matcher( inputTime );
        if ( matcher.matches() ) {
            int year  = Integer.parseInt( matcher.group( 1 ) );
            int month = Integer.parseInt( matcher.group( 2 ) );
            int date  = Integer.parseInt( matcher.group( 3 ) );
            if ( date > 28 ) {
                Calendar instance = Calendar.getInstance();
                instance.set( year, month - 1, 1 );
                int lastDay = instance.getActualMaximum( Calendar.DAY_OF_MONTH );
                return ( lastDay >= date );
            }
        }
        return true;
    }

    /**
     * 不是一个Date类型
     *
     * @param inputTime
     * @return
     * @see DateUtils#isDate(String)
     */
    public static boolean isNotDate ( final String inputTime ) {
        return ! isDate( inputTime );
    }

    /**
     * 字符串转成Date类型
     *
     * @param inputTime
     * @param dateStyle
     * @return
     */
    public static Date formatStringByStyle ( final String inputTime, final String dateStyle ) {
        try {
            if ( StringUtils.isBlank( inputTime ) ) {
                return null;
            }
            if ( StringUtils.isBlank( dateStyle ) ) {
                return null;
            }
            DateFormat format = new SimpleDateFormat( dateStyle );
            return format.parse( inputTime );
        } catch ( ParseException e ) {
            return null;
        }
    }


}








