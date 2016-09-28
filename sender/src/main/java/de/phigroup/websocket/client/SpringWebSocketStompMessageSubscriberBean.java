package de.phigroup.websocket.client;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.phigroup.websocket.monitor.dto.Greeting;
import de.phigroup.websocket.monitor.dto.SigarDynamicSystemStats;
import de.phigroup.websocket.monitor.dto.SigarStaticSystemStats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author eztup
 *
 */
@Service
@Transactional
@Data
@Slf4j
public class SpringWebSocketStompMessageSubscriberBean implements SpringWebSocketStompMessageSubscriber {

	
	/*
	 * See https://github.com/rstoyanchev/spring-websocket-portfolio/blob/master/src/test/java/org/springframework/samples/portfolio/web/tomcat/IntegrationPortfolioTests.java
	 *  (non-Javadoc)
	 * @see com.lhsystems.websocket.client.SpringWebSocketStompMessageSubscriber#subscribe(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Subscription subscribe(ListenableFuture<StompSession> session, final String sendToEndpointUri, final String sourceHost) throws Exception {
		
		Subscription s = null;
		try {
//			ListenableFuture<StompSession> session = connectStompSession(wsUrl);
			
//			sendToEndpointUri = StringUtils.isEmpty(sendToEndpointUri) ? "/topic/greetings" : sendToEndpointUri;

			final AtomicReference<Object> result = new AtomicReference<Object>();
			    
			// subscribe (only in case you want to subscribe a message stream)
			s = session.get().subscribe(sendToEndpointUri + "/" + sourceHost, new StompFrameHandler() {
							
			    @Override
			    public Type getPayloadType(StompHeaders headers) {

			    	log.debug("getPayloadType...");
			    	
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
						switch(sendToEndpointUri + "/" + sourceHost) {
							
							case "/topic/greetings":
								Greeting greet = mapper.reader().forType(Greeting.class).readValue(json);
								log.debug("greet: " + greet);
								break;
							case "/status/system/dynamic":
								SigarDynamicSystemStats statsDynamic = mapper.reader().forType(SigarDynamicSystemStats.class).readValue(json);
								log.debug("stats: " + statsDynamic);
								break;
							case "/status/system/static":
								SigarStaticSystemStats statsStatic = mapper.reader().forType(SigarStaticSystemStats.class).readValue(json);
								log.debug("stats: " + statsStatic);
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
