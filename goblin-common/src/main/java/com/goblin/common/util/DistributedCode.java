package com.goblin.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.concurrent.atomic.LongAdder;

/**
 * 分布式code构建工具
 * <p>
 * (机器码 + 进程号(或者用PID)) + 随机数 + 时间 + 计数器
 *
 * @author pijingzhanji
 */
public final class DistributedCode {


	/** 机器码 加 进程号 会导致生成的序列号很长, 基于这两个值做一些截取 */
	private static final String MP;
	/** 截取长度: 从最后面开始截取 */
	private static final int               MP_LEN                = 6;
	/** 日期格式化,是线程安全的 */
	private static final DateTimeFormatter DATE_TIME_FORMATTER   = DateTimeFormatter.ofPattern( "yyyyMMddHHmmssSSS" );
	/** 原子类 */
	private static final LongAdder         LONG_ADDER            = new LongAdder();
	private static final Long              INITIAL_VALUE         = 100_00L;
	private static final Long              NEXT_NUMBER_MAX_LIMIT = 999_99L;


	static {
		try {
			LONG_ADDER.add( INITIAL_VALUE );
			// 机器码 --> 本机 mac 地址的 hashcode 值
			int machineIdentifier = createMachineIdentifier();
			// 进程号 --> 当前运行的 jvm 进程号的 hashcode 值
			int    processIdentifier = createProcessIdentifier();
			String mp                = Integer.toString( Math.abs( ( machineIdentifier + "" + processIdentifier ).hashCode() + 1 ) );
			MP = ( mp.length() > MP_LEN ) ? mp.substring( mp.length() - MP_LEN , mp.length() ) : mp;
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * 得到当前机器码
	 *
	 * @return 6位长度<code>String</code>
	 */
	public static String getMachineCode () {
		return MP;
	}


	/**
	 * 指定生成的ID长度
	 *
	 * @param length <b style="color:red">最大长度不能小于28</b>
	 * @return 全局唯一ID
	 */
	public static String globalUniqueId ( final int length ) {
		if ( length < 28 ) {
			throw new IllegalArgumentException( "length 不能小于28,否则无法保证唯一性" );
		}
		// 补位长度
		final int fill = length - 28;
		return globalUniqueId( StringUtils.EMPTY , RandomStringUtils.randomNumeric( fill ) );
	}

	/**
	 * @return 全局唯一ID(大于32位)
	 */
	public static String globalUniqueId () {
		return globalUniqueId( Thread.currentThread().getId() + StringUtils.EMPTY ,
							   RandomStringUtils.randomNumeric( 10 ) );
	}

	/**
	 * @return 固定长度28位
	 */
	public static String globalUniqueId28Length () {
		return globalUniqueId( StringUtils.EMPTY , StringUtils.EMPTY );
	}

	/**
	 * @return 固定长度32位
	 */
	public static String globalUniqueId32Length () {
		return globalUniqueId( 32 );
	}

	/**
	 * 前缀[自定长度] + 机器码[6位] + 时间[17位] + 循环增减数[5位] + 随机数[自定长度]
	 *
	 * @param prefix       前缀
	 * @param randomNumber 随机数
	 * @return 可确定长度28位
	 */
	private static String globalUniqueId ( final String prefix , final String randomNumber ) {
		// 时间
		final String now = DATE_TIME_FORMATTER.format( LocalDateTime.now() );
		// 下一个数
		final long nextNumber = nextNumber();
		return prefix + MP + now + nextNumber + randomNumber;
	}


	/**
	 * @return 5位
	 */
	private static long nextNumber () {
		final long next = LONG_ADDER.longValue();
		if ( next >= NEXT_NUMBER_MAX_LIMIT ) {
			LONG_ADDER.reset();
			LONG_ADDER.add( INITIAL_VALUE );
		}
		LONG_ADDER.increment();
		return next;
	}


	// 创建机器标识符
	private static int createMachineIdentifier () {
		// build a 2-byte machine piece based on NICs info
		int machinePiece;
		try {
			StringBuilder                   stringBuilder     = new StringBuilder();
			Enumeration< NetworkInterface > networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while ( networkInterfaces.hasMoreElements() ) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				stringBuilder.append( networkInterface.toString() );
				byte[] mac = networkInterface.getHardwareAddress();
				if ( mac != null ) {
					ByteBuffer byteBuffer = ByteBuffer.wrap( mac );
					try {
						stringBuilder.append( byteBuffer.getChar() );
						stringBuilder.append( byteBuffer.getChar() );
						stringBuilder.append( byteBuffer.getChar() );
					} catch ( BufferUnderflowException shortHardwareAddressException ) { //NOPMD
						// mac with less than 6 bytes. continue
					}
				}
			}
			machinePiece = stringBuilder.toString().hashCode();
		} catch ( Throwable t ) {
			// exception sometimes happens with IBM JVM, use random
			machinePiece = new SecureRandom().nextInt();
		}
		return machinePiece;
	}

	// Creates the process identifier. This does not have to be unique per class loader because
	// NEXT_COUNTER will provide the uniqueness.
	// 创建进程标识符。这并不是每个类装入器,因为必须是唯一的
	private static int createProcessIdentifier () {
		int processId;
		try {
			String processName = ManagementFactory.getRuntimeMXBean().getName();
			if ( processName.contains( "@" ) ) {
				processId = Integer.parseInt( processName.substring( 0 , processName.indexOf( '@' ) ) );
			} else {
				processId = processName.hashCode();
			}
		} catch ( Throwable t ) {
			processId = new SecureRandom().nextInt();
		}
		return processId;
	}


}












