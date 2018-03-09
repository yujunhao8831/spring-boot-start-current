package com.goblin.manage.controller;

import com.goblin.common.ResponseEntityPro;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 披荆斩棘
 * @date : 2017/8/26
 */
@RestController
public class WebSocketDemoController {

	@MessageMapping( "/hello" )   // 接收客户端
	@SendTo( "/topic/greetings" ) // 广播消息
	public ResponseEntity< String > greeting ( String message ) {
		return ResponseEntityPro.ok( "Hello, " + message + "!" );
	}


}
