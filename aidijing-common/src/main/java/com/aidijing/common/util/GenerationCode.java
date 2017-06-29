/*
 * 文 件 名:  GenerationCode.java
 * 版    权:  HNA TELECOM Co.,LTD.
 * 描    述:  生成编码规则
 * 创 建 人:  wubangjie
 * 创建时间:  2016年6月27日
 */
package com.aidijing.common.util;

import org.apache.commons.lang3.RandomStringUtils;

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
 */
public final class GenerationCode {
    
    /** 机器码 加 进程号 会导致生成的序列号很长, 基于这两个值做一些截取 */
    private static final String MP;
    /** 截取长度: 从最后面开始截取 */
    private static final int               MP_LEN              = 6;
    /** 日期格式化,是线程安全的 */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern( "yyyyMMddHHmmssSSS" );
    /** 原子类 */
    private static final LongAdder         LONG_ADDER          = new LongAdder();


    static {
        try {
            int    machineIdentifier = createMachineIdentifier(); // 机器码 --> 本机 mac 地址的 hashcode 值
            int    processIdentifier = createProcessIdentifier(); // 进程号 --> 当前运行的 jvm 进程号的 hashcode 值
            String mp                = Integer.toString( Math.abs( ( machineIdentifier + "" + processIdentifier ).hashCode() ) );
            MP = ( mp.length() > MP_LEN ) ? mp.substring( mp.length() - MP_LEN, mp.length() ) : mp;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * (机器码 + 进程号) + 随机数 + 时间 + 下一个数
     *
     * @return 全局唯一ID
     */
    public static String globalUniqueId () {
        LONG_ADDER.increment();
        final String randomNumber = RandomStringUtils.randomNumeric( 10 ); // 随机数,该工具类是线程安全的
        final String now          = DATE_TIME_FORMATTER.format( LocalDateTime.now() ); // 时间
        final long   nextNumber   = LONG_ADDER.longValue(); // 下一个数
        return MP + Thread.currentThread().getId() + randomNumber + now + nextNumber;
    }

    /**
     * (机器码 + 进程号) + 随机数 + 时间 + 下一个数 + 用户ID
     *
     * @param userId : 用户ID
     * @return 全局唯一ID
     */
    public static String globalUniqueId ( final String userId ) {
        return globalUniqueId() + userId;
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
                processId = Integer.parseInt( processName.substring( 0, processName.indexOf( '@' ) ) );
            } else {
                processId = processName.hashCode();
            }
        } catch ( Throwable t ) {
            processId = new SecureRandom().nextInt();
        }
        return processId;
    }


}












