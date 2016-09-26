package de.phigroup.websocket.monitor;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author u023871
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * register new message broker contexts here
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/status");
		config.setApplicationDestinationPrefixes("/app");
	}

	/**
	 * LI: if /hello is not registered here, Application will not be able to receive our messages from JUnit test Stomp client
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		// you can register hosts allowed to connect to our web sockets with setAllowedOrigins
		// TODO: encapsulate into properties or DB
		registry.addEndpoint("/hello").setAllowedOrigins("*").withSockJS();
	}

}