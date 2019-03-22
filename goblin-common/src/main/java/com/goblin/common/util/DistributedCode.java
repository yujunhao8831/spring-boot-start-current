package com.goblin.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 分布式code构建工具
 * <p>
 * 机器码 + 时间 + 计数器
 *
 * @author pijingzhanji
 */
public final class DistributedCode {


    /** 机器码 加 进程号 会导致生成的序列号很长, 基于这两个值做一些截取 */
    private static final String                  MP;
    /** 截取长度: 从最后面开始截取 */
    private static final int                     MP_LEN              = 6;
    /** 日期格式化,是线程安全的 */
    private static final DateTimeFormatter       DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyyMMddHHmmssSSS" );
    private static final Map< Integer, Counter > ATOMIC_LONG_MAP;
    private static final Counter                 COUNTER;
    private static final Integer                 MAX_COUNTER_LENGTH  = 18;
    private static final Integer                 MIN_COUNTER_LENGTH  = 5;


    private static String buildDuplicateString ( String str , int count ) {
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0 ; i < count ; i++ ) {
            stringBuilder.append( str );
        }
        return stringBuilder.toString();
    }

    static {
        COUNTER = new Counter( 0L , 999999999L , new AtomicLong( 0L ) , 9 );
        Map< Integer, Counter > atomicLongPros = new ConcurrentHashMap<>();
        for ( int i = MIN_COUNTER_LENGTH ; i <= MAX_COUNTER_LENGTH ; i++ ) {
            atomicLongPros.put( i , new Counter( 0L ,
                                                 Long.parseLong( buildDuplicateString( "9" , i ) ) ,
                                                 new AtomicLong( 0L ) ,
                                                 i )
            );
        }
        ATOMIC_LONG_MAP = atomicLongPros;
        try {
            // 机器码 --> 本机 mac 地址的 hashcode 值
            int machineIdentifier = createMachineIdentifier();
            // 进程号 --> 当前运行的 jvm 进程号的 hashcode 值
            int    processIdentifier = createProcessIdentifier();
            String mp                = Integer.toString( Math.abs( ( machineIdentifier + "" + processIdentifier ).hashCode() + 1 ) );
            MP = ( mp.length() > MP_LEN ) ? mp.substring( mp.length() - MP_LEN ) : mp;
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
     * 机器码(6位) + 时间 (17位) + 计数器(27位)
     *
     * @return 全局唯一ID(50位)
     */
    public static String globalUniqueId () {
        // 时间
        final String now = DATE_TIME_FORMATTER.format( LocalDateTime.now() );
        // 下一个数
        final String nextNumber = ATOMIC_LONG_MAP.get( MAX_COUNTER_LENGTH ).nextNumber();
        return MP + now + COUNTER.nextNumber() + nextNumber;
    }


    /**
     * 创建机器标识符
     *
     * @return /
     */
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

    /**
     * 创建进程标识符。这并不是每个类装入器,因为必须是唯一的
     *
     * @return /
     */
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


    /**
     * 计数器
     */
    private static class Counter {
        /**
         * 初始化值
         */
        private final Long                   initialValue;
        /**
         * 最大上限
         */
        private final Long                   nextNumberMaxLimit;
        /**
         * 原子操作
         */
        private final AtomicLong             atomicLong;
        /**
         * 长度
         */
        private final Integer                length;
        /**
         * 补位字符
         */
        private final Map< Integer, String > buffMap;

        Counter ( Long initialValue ,
                  Long nextNumberMaxLimit ,
                  AtomicLong atomicLong ,
                  Integer length ) {
            this.initialValue = initialValue;
            this.nextNumberMaxLimit = nextNumberMaxLimit;
            this.atomicLong = atomicLong;
            this.length = length;
            this.buffMap = new ConcurrentHashMap<>( this.length );
            // 0 - 99999
            // 1 需要补 0000
            // 11 需要补 000
            // 111 需要补 00
            // 1111 需要补 0
            // 11111 不需要补
            for ( int i = 1 ; i < this.length ; i++ ) {
                StringBuilder buff = new StringBuilder();
                for ( int j = 0 ; j < this.length - i ; j++ ) {
                    buff.append( "0" );
                }
                buffMap.put( i , buff.toString() );
            }
            buffMap.put( this.length , StringUtils.EMPTY );
        }


        /**
         * 循环取数,达到上限则回到初始值重新取数
         * <pre>
         *     new Counter( 0L ,Long.parseLong( buildDuplicateString( "9" , 3 ) ) ,new AtomicLong( 0L ) ,3 ).nextNumber() = 0000 - 9999
         * </pre>
         *
         * @return 固定长度数
         */
        private String nextNumber () {

            // 下一个数
            final long nextNumber = this.atomicLong.getAndIncrement();
            if ( nextNumber >= this.nextNumberMaxLimit ) {
                this.atomicLong.set( this.initialValue );
            }
            final String nextNumberString = nextNumber + StringUtils.EMPTY;
            // 长度不够自动补位
            return this.buffMap.get( nextNumberString.length() ) + nextNumberString;
        }
    }

    public static void main ( String[] args ) throws InterruptedException {
        final long            max            = 100000;
        final int             threads        = 200;
        Map< String, Object > map            = new ConcurrentHashMap<>();
        CountDownLatch        countDownLatch = new CountDownLatch( threads );
        final ExecutorService service        = Executors.newFixedThreadPool( threads );

        for ( int j = 0 ; j < threads ; j++ ) {
            service.submit( () -> {
                System.err.println( "线程:[" + Thread.currentThread().getName() + "]开始执行,id = " + Thread.currentThread()
                                                                                                      .getId() );
                for ( int i = 0 ; i < max ; i++ ) {
                    final String id = globalUniqueId();
                    map.put( id , id );
                }
                // 完成
                countDownLatch.countDown();
                System.err.println( "线程:[" + Thread.currentThread().getName() + "]结束执行,id = " + Thread.currentThread()
                                                                                                      .getId() );
            } );
        }
        countDownLatch.await();

        System.err.println( "计划生成[" + max * threads + "]个序列号,实际生成[" + map.size() + "]个序列号" );
        System.err.println( String.valueOf( max * threads ).equals( String.valueOf( map.size() ) ) );
        System.err.println( globalUniqueId().length() );

        service.shutdown();


    }
}












