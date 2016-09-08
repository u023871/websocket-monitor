package de.phigroup.websocket.client;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Receiptable;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import de.phigroup.websocket.MyStompSessionHandler;
import de.phigroup.websocket.monitor.dto.Greeting;
import de.phigroup.websocket.monitor.dto.SigarSystemStats;
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
public class SpringWebSocketStompClientBean implements SpringWebSocketStompClient {
	
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
		String url = StringUtils.isEmpty(wsUrl) ? "ws://127.0.0.1:9090/hello" : wsUrl;
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
	public void sendSystemStatus(String wsUrl, final String endpointUri) throws Exception {

		try {
			ListenableFuture<StompSession> session = connectStompSession(wsUrl);

			// convert object to json
			SigarSystemStats g = SigarSystemStats.getSigarSystemStatistics();
			Jackson2JsonObjectMapper map = new Jackson2JsonObjectMapper();
			String json = map.toJson(g);
			
			// send
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
	
	/*
	 * See https://github.com/rstoyanchev/spring-websocket-portfolio/blob/master/src/test/java/org/springframework/samples/portfolio/web/tomcat/IntegrationPortfolioTests.java
	 *  (non-Javadoc)
	 * @see com.lhsystems.websocket.client.SpringWebSocketStompClient#subscribe(java.lang.String, java.lang.String)
	 */
	@Override
	public Subscription subscribe(ListenableFuture<StompSession> session, final String sendToEndpointUri) throws Exception {
		
		Subscription s = null;
		try {
//			ListenableFuture<StompSession> session = connectStompSession(wsUrl);
			
//			sendToEndpointUri = StringUtils.isEmpty(sendToEndpointUri) ? "/topic/greetings" : sendToEndpointUri;

			final AtomicReference<Object> result = new AtomicReference<Object>();
			    
			// subscribe (only in case you want to subscribe a message stream)
			s = session.get().subscribe(sendToEndpointUri, new StompFrameHandler() {
							
			    @Override
			    public Type getPayloadType(StompHeaders headers) {

			    	log.debug("getpayloadType...");
			    	
			    	for (List<String> values : headers.values()) {
						
			    		log.debug("getPayloadType values: " + values);
					}
			    	
			    	return byte[].class; // json byte array
			    }

			    @Override
			    public void handleFrame(StompHeaders headers, Object payload) {

			    	log.debug("StompFrameHandler.handleFrame...");
			    	
			    	for (List<String> values : headers.values()) {
						
			    		log.debug("handleFrame values: " + values);
					}
			    	
			    	log.debug("handleFrame payload: " + payload);
			    	
			    	String json = new String((byte[]) payload);
			    	log.debug("json: " + json);
			    	log.debug("String.valueOf(payload): " + String.valueOf(payload));
			    	
			    	try {
			    		// http://www.baeldung.com/jackson-exception
						ObjectMapper mapper = new ObjectMapper();
						
						// switch type here
						switch(sendToEndpointUri) {
							
							case "/topic/greetings":
								Greeting greet = mapper.reader().forType(Greeting.class).readValue(json);
								log.debug("greet: " + greet);
								break;
							case "/status/system":
								SigarSystemStats stats = mapper.reader().forType(SigarSystemStats.class).readValue(json);
								log.debug("stats: " + stats);
								break;
							default:
								;
						}
						
						
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					} 
			    	
			    	
		    		result.set(payload);
				}
			});
			
			log.debug("result: " + result);
			
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		return s;
	}
}
