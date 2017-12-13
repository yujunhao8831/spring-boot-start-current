package com.aidijing.common.util;

import lombok.Getter;

/**
 * @author : 披荆斩棘
 * @date : 2017/7/17
 */
@Getter
public enum DateFormatStyle {

	CN_DATE_BASIC_STYLE( "yyyy-MM-dd HH:mm:ss" ),
	CN_DATE_BASIC_STYLE2( "yyyy/MM/dd HH:mm:ss" ),
	CN_DATE_BASIC_STYLE3( "yyyy/MM/dd" ),
	CN_DATE_BASIC_STYLE4( "yyyy-MM-dd" ),
	CN_DATE_BASIC_STYLE5( "yyyyMMdd" ),
	CN_DATE_BASIC_STYLE6( "yyyy-MM" ),
	CN_DATE_BASIC_STYLE7( "yyyyMM" ),
	DATE_TIMESTAMP_STYLE( "yyyyMMddHHmmss" ),
	DATE_TIMESTAMPS_STYLE( "yyyyMMddHHmmssSSS" ),
	ISO_DATETIME_FORMAT( "yyyy-MM-dd'T'HH:mm:ss" ),
	ISO_DATETIME_TIME_ZONE_FORMAT( "yyyy-MM-dd'T'HH:mm:ssZZ" ),
	ISO_DATE_TIME_ZONE_FORMAT( "yyyy-MM-ddZZ" ),
	ISO_TIME_FORMAT( "'T'HH:mm:ss" ),
	ISO_TIME_TIME_ZONE_FORMAT( "'T'HH:mm:ssZZ" ),
	ISO_TIME_NO_T_FORMAT( "HH:mm:ss" ),
	ISO_TIME_NO_T_TIME_ZONE_FORMAT( "HH:mm:ssZZ" );

	private String dateStyle;

	DateFormatStyle ( String dateStyle ) {
		this.dateStyle = dateStyle;
	}


}
