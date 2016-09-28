package de.phigroup.websocket.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;

import de.phigroup.websocket.client.SpringWebSocketStompMessageBroker;
import de.phigroup.websocket.client.SpringWebSocketStompMessageSubscriber;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan({"de.phigroup"}) // http://www.baeldung.com/spring-nosuchbeandefinitionexception
@SpringBootApplication
@EnableAsync
@EnableScheduling
@Slf4j
public class Application implements CommandLineRunner {

	@Autowired
	private SpringWebSocketStompMessageBroker stompBroker;
	
	@Autowired
	private SpringWebSocketStompMessageSubscriber stompSubscriber;

	public static void main(String[] args) {
		
		/*
		 * Test Sigar stuff with -Djava.library.path="D:\DEV\Workspaces\Spring\gs-messaging-stomp-websocket\stomp-websocket-client\src\main\resources\sigar\lib",
		 * see http://stackoverflow.com/questions/11612711/sigar-unsatisfiedlinkerror!
		 * 
		 * TODO: set to classpath or so to make it dynamic
		 */
		System.setProperty("java.library.path", "C:\\development\\Workspaces\\Spring\\websocket-monitor\\sender\\src\\main\\resources\\sigar\\lib");
		
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ListenableFuture<StompSession> session = stompBroker.connectStompSession("ws://127.0.0.1:9090/monitor/hello");
		
		@SuppressWarnings("unused")
		Subscription sGreetings = stompSubscriber.subscribe(session, "/topic/greetings", "127.0.0.1");
		
		@SuppressWarnings("unused")
		Subscription stateCpu = stompSubscriber.subscribe(session, "/status/system", "127.0.0.1");

		sendStatic();
		
	}
	
	/**
	 * send out data to subscribers every 10s
	 * 
     * TODO: let iteration be set by UI in next step
     * TODO: read ws endpoint and source host from some configuration
	 */
	@Scheduled(cron="*/10 * * * * *")
	public void sendDynamic() {

		try {
			stompBroker.sendDynamicSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/dynamic", "127.0.0.1");

			stompBroker.sendStaticSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/static", "127.0.0.1");

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * send out static data to subscribers
	 * 
     * TODO: read ws endpoint and source host from some configuration
	 */
	public void sendStatic() {

		try {
			stompBroker.sendStaticSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/static", "127.0.0.1");

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
		}
	}
	
}
