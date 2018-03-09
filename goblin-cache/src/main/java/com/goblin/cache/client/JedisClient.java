package com.goblin.cache.client;


import redis.clients.jedis.exceptions.JedisDataException;

/**
 * redis基本操作,单机版
 *
 * @author pijingzhanji
 */
public interface JedisClient {

	/**
	 * <p>获取指定键的值.如果键不存在返回null.</p>
	 * <a href="http://www.redis.cn/commands/get.html">document</a>
	 *
	 * @param key
	 * @return <b style="color:red">如果key的value不是string，就返回错误，因为GET只处理string类型的values</b>
	 */
	String get ( String key );

	/**
	 * <p>将键key设定为指定的"字符串"值。</p>
	 * <a href="http://www.redis.cn/commands/set.html">document</a>
	 * <b style="color:red">
	 * 如果key已经保存了一个值，那么这个操作会直接覆盖原来的值，并且忽略原始类型。
	 * 当set命令执行成功之后，之前设置的过期时间都将失效
	 * </b>
	 *
	 * @param key
	 * @param value
	 * @return 如果SET命令正常执行那么回返回OK.
	 */
	String set ( String key , String value );

	/**
	 * <p>对存储在指定key的数值执行原子的加1操作。</p>
	 * <a href="http://www.redis.cn/commands/incr.html">document</a>
	 * <b style="color:red">
	 * 如果指定的key不存在，那么在执行incr操作之前，会先将它的值设定为0。
	 * 如果指定的key中存储的值不是字符串类型或者存储的字符串类型不能表示为一个整数
	 * 那么执行这个命令时服务器会抛异常 {@link JedisDataException}
	 * </b>
	 *
	 * @param key
	 * @return 执行递增操作后key对应的值。
	 */
	Long incr ( String key ) throws JedisDataException;

	/**
	 * 删除指定的一批keys，如果删除中的某些key不存在，则直接忽略。
	 * <a href="http://www.redis.cn/commands/del.html">document</a>
	 *
	 * @param keys
	 * @return 被删除的keys的数量
	 */
	Long del ( final String... keys );

	/**
	 * <p>设置key的过期时间,超过时间后,将会自动删除该key.</p>
	 * <b style="color:red">
	 * 对已经有过期时间的key执行EXPIRE操作，将会更新它的过期时间。有很多应用有这种业务场景，例如记录会话的session。
	 * </b>
	 * <a href="http://www.redis.cn/commands/expire.html">document</a>
	 *
	 * @param key
	 * @param second : 过期时间,单位 : 秒
	 * @return 1 如果成功设置过期时间,0 如果key不存在或者不能设置过期时间.
	 */
	Long expire ( String key , int second );


	/**
	 * 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期。<br/>
	 * 这个命令等效于执行下面的命令 :
	 * <pre>
	 *      1. {@link #get(String)}
	 *      2. {@link #expire(String , int)}
	 * </pre>
	 * <b style="color:red">
	 * SETEX是原子的，也可以通过把上面两个命令放到MULTI/EXEC块中执行的方式重现。
	 * 相比连续执行上面两个命令，它更快，因为当Redis当做缓存使用时，这个操作更加常用。
	 * </b>
	 *
	 * @param key
	 * @param value
	 * @param second : 过期时间,单位 : 秒
	 * @return 如果SET命令正常执行那么回返回OK.
	 */
	String setex ( String key , String value , int second );
}
