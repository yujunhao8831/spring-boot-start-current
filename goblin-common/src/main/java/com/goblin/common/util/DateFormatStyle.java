package com.goblin.common.util;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : 披荆斩棘
 * @date : 2017/7/17
 */
@Getter
public enum DateFormatStyle {

	CN_DATE_BASIC_STYLE( "yyyy-MM-dd HH:mm:ss" ),
	CN_DATE_BASIC_STYLE1( "yyyy-MM-dd" ),
	CN_DATE_BASIC_STYLE2( "yyyy-MM" ),
	CN_DATE_BASIC_STYLE3( "yyyy/MM/dd HH:mm:ss" ),
	CN_DATE_BASIC_STYLE4( "yyyy/MM/dd" ),
	CN_DATE_BASIC_STYLE5( "yyyyMMdd" ),
	CN_DATE_BASIC_STYLE6( "yyyyMM" ),
	DATE_TIMESTAMP_STYLE( "yyyyMMddHHmmssSSS" ),
	DATE_TIMESTAMP_STYLE2( "yyyyMMddHHmmss" ),
	ISO_DATETIME_TIME_ZONE_FORMAT( "yyyy-MM-dd'T'HH:mm:ssZZ" ),
	ISO_DATETIME_FORMAT( "yyyy-MM-dd'T'HH:mm:ss" ),
	ISO_DATE_TIME_ZONE_FORMAT( "yyyy-MM-ddZZ" ),
	ISO_TIME_TIME_ZONE_FORMAT( "'T'HH:mm:ssZZ" ),
	ISO_TIME_FORMAT( "'T'HH:mm:ss" ),
	ISO_TIME_NO_T_TIME_ZONE_FORMAT( "HH:mm:ssZZ" ),
	ISO_TIME_NO_T_FORMAT( "HH:mm:ss" );

	private String dateStyle;

	DateFormatStyle ( String dateStyle ) {
		this.dateStyle = dateStyle;
	}

	/**
	 * @return 按照默认声明顺序
	 */
	public static List<String> getDateFormatStyles() {
		return Arrays.stream(DateFormatStyle.values())
					 .map(DateFormatStyle::getDateStyle)
			   	     .collect(Collectors.toList());
	}


}
