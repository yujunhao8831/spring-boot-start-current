package com.goblin.common.cron;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/9
 */
@Getter
@Setter
@ToString
@Accessors( chain = true )
public class CustomizeCron implements Serializable {

	// seconds(60) minutes(60) hours(24) daysOfMonth(31) months(12) daysOfWeek(7)
	protected static final String ALWAYS      = "*";
	// 秒 最大值 : 60
	private                String seconds;
	// 分 最大值 : 60
	private                String minutes;
	// 小时 最大值 : 24
	private                String hours       = ALWAYS;
	// 月内天数 最大值 : 31
	private                String daysOfMonth = ALWAYS;
	// 月份 最大值 : 12     -> FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC
	private                String months      = ALWAYS;
	// 周内天 最大值 : 7    -> SUN,MON,TUE,WED,THU,FRI,SAT
	private                String daysOfWeek  = ALWAYS;


	// Example patterns:
	// "0 0 * * * *" = the top of every hour of every day.
	// "*/10 * * * * *" = every ten seconds.
	// "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	// "0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
	// "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
	// "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	// "0 0 0 25 12 ?" = every Christmas Day at midnight
	// * * * * *
	// seconds(60) minutes(60) hours(24) daysOfMonth(31) months(12) daysOfWeek(7)

	public CustomizeCron () {
	}

	public CustomizeCron ( String expression ) {
		final String[] fields = split( expression , " " );
		if ( fields.length > 6 ) {
			throw new RuntimeException( String.format( "cron表达式不正确,expression : %s " , expression ) );
		}
		this.seconds = fields[0];
		this.minutes = fields[1];
		this.hours = fields[2];
		this.daysOfMonth = fields[3];
		this.months = fields[4];
		this.daysOfWeek = fields[5];
	}


	/**
	 * 得到 {@link CronSequenceGenerator}
	 */
	public CronSequenceGenerator toCron () {
		return new CronSequenceGenerator( this.toCronString() );
	}

	/**
	 * 得到表达式
	 */
	public String toCronString () {
		return this.seconds + SPACE +
			this.minutes + SPACE +
			this.hours + SPACE +
			this.daysOfMonth + SPACE +
			this.months + SPACE +
			this.daysOfWeek;
	}


	protected void init () {
		this.seconds = "0";
		this.minutes = "0";
	}

}
