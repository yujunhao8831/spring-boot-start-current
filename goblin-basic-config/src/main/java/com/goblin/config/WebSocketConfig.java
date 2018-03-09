package com.goblin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Objects;

/**
 * Spring WebSocket 配置,默认开启
 * <a href="https://spring.io/guides/gs/messaging-stomp-websocket/">example</a>
 * 如何配置? yml中示例如下
 * <pre>
 *      goblin:
 *        web-socket:
 *          enabled: true
 *          client-broker-destination-prefixes: /topic
 *          server-application-destination-prefixes: /app
 *          stomp-endpoints-paths: /goblin
 *
 * </pre>
 * <img src="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/images/message-flow-simple-broker.png"/>
 * <p>
 * <img src="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/images/message-flow-broker-relay.png"/>
 * <p>
 * 拦截器配置,如果要想对 WebSocket 通信进行拦截,比如做些认证处理,那么实现{@link org.springframework.messaging.support.ChannelInterceptor},并注入到Spring上下文中即可
 * 示例 :
 * <pre>
 *      <code>@Configuration</code>
 *      public class SpringWebSocketSecurityConfig {
 *          <code>@Bean</code>
 *          public ChannelInterceptor channelInterceptor () {
 *              return new JwtWebSocketInterceptorAdapter();
 *          }
 *      }
 * </pre>
 *
 * @author pijingzhanji
 */
@Configuration
@EnableWebSocketMessageBroker
@Getter
@Setter
@ConfigurationProperties( prefix = "goblin.web-socket" )
@ConditionalOnExpression( "${goblin.web-socket.enabled:true}" )
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private String[] clientBrokerDestinationPrefixes      = { "/topic" };
	private String[] serverApplicationDestinationPrefixes = { "/app" };
	private String[] stompEndpointsPaths                  = { "/goblin" };
	/**
	 * WebSocket拦截器
	 */
	@Autowired( required = false )
	private ChannelInterceptor channelInterceptor;


	/**
	 * 通道
	 *
	 * @param registration
	 */
	@Override
	public void configureClientInboundChannel ( ChannelRegistration registration ) {
		if ( Objects.nonNull( channelInterceptor ) ) {
			registration.interceptors( channelInterceptor );
		}

	}

	/**
	 * 定义消息代理,设置消息连接请求的各种规范信息.
	 *
	 * @param config 消息代理注册
	 */
	@Override
	public void configureMessageBroker ( MessageBrokerRegistry config ) {
		// Server前缀,指服务端接收地址的前缀,意思就是说客户端给服务端发消息的地址的前缀
		config.setApplicationDestinationPrefixes( serverApplicationDestinationPrefixes );
		// Client前缀,表示客户端订阅地址的前缀信息,也就是客户端接收服务端消息的地址的前缀信息
		config.enableSimpleBroker( clientBrokerDestinationPrefixes );
	}

	/**
	 *
	 * <pre>
	 *    var stompClient = null;
	 *    var brokerDestinationPrefixes = "服务器端配置的订阅前缀";
	 *    var serverApplicationDestinationPrefixes = "服务器端配置的接收前缀";
	 *    var serverEndpoint = "/goblin"; // 服务端配置的端点
	 *
	 *    // 连接
	 *    function connect() {
	 *        var socket = new SockJS(serverEndpoint);
	 *        stompClient = Stomp.over(socket);
	 *        stompClient.connect({}, function (frame) {
	 *            setConnected(true);
	 *            // 订阅的时候,加上前缀 {@link #CLIENT_SUBSCRIBE_PREFIXES}
	 *            stompClient.subscribe( brokerDestinationPrefixes + '/greetings', function (greeting) {
	 *                showGreeting(JSON.parse(greeting.body).content);
	 *            });
	 *        });
	 *    }
	 *    // 发送消息
	 *    function sendName() {
	 *        stompClient.send( serverApplicationDestinationPrefixes + "/hello", {}, JSON.stringify({'name': '披荆斩棘'}));
	 *    }
	 *
	 * </pre>
	 */

	/**
	 * 配置端点注册中心,接收客户端的连接
	 *
	 * @param registry 端点注册中心
	 */
	@Override
	public void registerStompEndpoints ( StompEndpointRegistry registry ) {
		/* 在指定的映射路径上为Web套接字端点注册一个STOMP. */
		// 表示添加了一个 /goblin 端点,客户端就可以通过这个端点来进行连接.
		// goblin
		registry.addEndpoint( stompEndpointsPaths )
				.withSockJS(); // 开启SockJS支持
	}


}

/**
 * 大概处理流程
 * <p>
 * {@link org.springframework.messaging.handler.invocation.AbstractMethodMessageHandler}
 * {@link org.springframework.messaging.handler.invocation.AbstractMethodMessageHandler#handleMessage}
 * ... ... ...
 * 处理返回值,然后发送消息
 * {@link org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite#handleReturnValue(Object , org.springframework.core.MethodParameter , org.springframework.web.method.support.ModelAndViewContainer , org.springframework.web.context.request.NativeWebRequest)}
 * {@link org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler#handleReturnValue}
 * <p>
 * 注解简介 : org.springframework.messaging.handler.annotation 包下
 * <p>
 * {@link org.springframework.messaging.handler.annotation.Payload} : 消息体内容
 * {@link org.springframework.messaging.handler.annotation.Header}  : 得到某个header内容
 * {@link org.springframework.messaging.handler.annotation.DestinationVariable} 类似于 @{@link org.springframework.web.bind.annotation.PathVariable}
 * <a href="https://stackoverflow.com/questions/27047310/path-variables-in-spring-websockets-sendto-mapping">example</a>
 * <a href="https://stackoverflow.com/questions/30464230/spring-websocket-reply-to-user-message-flow">example</a>
 * <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#websocket-stomp-destination-separator">document</a>
 */
