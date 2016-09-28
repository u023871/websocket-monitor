package de.phigroup.websocket.client;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.util.concurrent.ListenableFuture;

public interface SpringWebSocketStompClient {

	ListenableFuture<StompSession> connectStompSession(String wsUrl) throws Exception;

	/**
	 * send greeting.
	 * 
	 * See http://databasefaq.com/index.php/answer/69808/java-spring-spring-websocket-websocketstompclient-wont-connect-to-sockjs-endpoint
	 * Okay this works and sends a Greeting right to the web socket listening browser page :-)
	 * 
	 * @param wsUrl e.g. ws://127.0.0.1:9090/hello
	 * @param greeting some greeting
	 * @throws Exception
	 */
	public void sendGreeting(String wsUrl, String endpointUri, String greeting) throws Exception;

	public void sendStaticSystemStatus(String wsUrl, final String endpointUri, final String sourceHost) throws Exception;
	
	public void sendDynamicSystemStatus(String wsUrl, final String endpointUri, final String sourceHost) throws Exception;
	
	/**
	 * subscribe to a message channel
	 * @param session
	 * @param sendToEndpointUri
	 * @param sourceHost we want to listen to 1 host only
	 * @return
	 * @throws Exception
	 */
	Subscription subscribe(ListenableFuture<StompSession> session, String sendToEndpointUri, final String sourceHost) throws Exception;
	
}