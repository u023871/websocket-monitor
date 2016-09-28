package de.phigroup.websocket.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Receiptable;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import de.phigroup.websocket.MyStompSessionHandler;
import de.phigroup.websocket.monitor.dto.Greeting;
import de.phigroup.websocket.monitor.dto.SigarDynamicSystemStats;
import de.phigroup.websocket.monitor.dto.SigarStaticSystemStats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author u023871
 *
 */
@Service
@Transactional
@Data
@Slf4j
public class SpringWebSocketStompMessageBrokerBean implements SpringWebSocketStompMessageBroker {
	
	/* (non-Javadoc)
	 * @see com.lhsystems.websocket.client.SpringWebSocketStompClient#connectStompSession(java.lang.String)
	 */
	@Override
	public ListenableFuture<StompSession> connectStompSession(String wsUrl) throws Exception {
		
		ListenableFuture<StompSession> session = null;

		// create web socket
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
		WebSocketClient transport = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(transport);
		
		// create session
		String url = StringUtils.isEmpty(wsUrl) ? "ws://127.0.0.1:9090/monitor/hello" : wsUrl;
		StompSessionHandler sessionHandler = new MyStompSessionHandler();
		session = stompClient.connect(url, sessionHandler);
		
		return session;
	}
	
	/* (non-Javadoc)
	 * @see com.lhsystems.websocket.client.SpringWebSocketStompClient#sendGreeting(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendGreeting(String wsUrl, String endpointUri, String greeting) throws Exception {
		
		try {
			ListenableFuture<StompSession> session = connectStompSession(wsUrl);

			// convert greeting object to json
			Greeting g = new Greeting(greeting);
//			Greeting g = new Greeting();
//			g.setContent(greeting);
			Jackson2JsonObjectMapper map = new Jackson2JsonObjectMapper();
			String json = map.toJson(g);
			
			// send
			endpointUri = StringUtils.isEmpty(endpointUri) ? "/topic/greetings" : endpointUri; 
			Receiptable r = session.get().send(endpointUri, json.getBytes());

			// check response
			String response = r.toString();
			String rid = r.getReceiptId();
			
			log.debug("rid: " + rid);
			log.debug("Response: " + response);
			
		} catch (Exception e) {
			
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void sendStaticSystemStatus(String wsUrl, final String endpointUri, final String sourceHost) throws Exception {

		try {
			ListenableFuture<StompSession> session = connectStompSession(wsUrl);

			// convert object to json
			SigarStaticSystemStats g = SigarStaticSystemStats.getFilledInstance();
			Jackson2JsonObjectMapper map = new Jackson2JsonObjectMapper();
			String json = map.toJson(g);
			
			// send
			Receiptable r = session.get().send(endpointUri + "/" + sourceHost, json.getBytes());

			// check response
			String response = r.toString();
			String rid = r.getReceiptId();
			
			log.debug("rid: " + rid);
			log.debug("Response: " + response);
			
		} catch (Exception e) {
			
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@Override
	public void sendDynamicSystemStatus(String wsUrl, final String endpointUri, final String sourceHost) throws Exception {

		try {
			ListenableFuture<StompSession> session = connectStompSession(wsUrl);

			// convert object to json
			SigarDynamicSystemStats g = SigarDynamicSystemStats.getNewFilledInstance();
			Jackson2JsonObjectMapper map = new Jackson2JsonObjectMapper();
			String json = map.toJson(g);
			
			// send
			Receiptable r = session.get().send(endpointUri + "/" + sourceHost, json.getBytes());

			// check response
			String response = r.toString();
			String rid = r.getReceiptId();
			
			log.debug("rid: " + rid);
			log.debug("Response: " + response);
			
		} catch (Exception e) {
			
			log.error(e.getMessage(), e);
			throw e;
		}
	}
}
