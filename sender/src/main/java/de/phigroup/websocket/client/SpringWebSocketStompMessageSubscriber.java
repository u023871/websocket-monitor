package de.phigroup.websocket.client;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 
 * @author eztup
 *
 */
public interface SpringWebSocketStompMessageSubscriber {

	
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
