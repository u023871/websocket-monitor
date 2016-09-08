package de.phigroup.websocket.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.util.concurrent.ListenableFuture;

import de.phigroup.websocket.client.SpringWebSocketStompClient;

@Configuration
@ComponentScan({"de.phigroup"}) // http://www.baeldung.com/spring-nosuchbeandefinitionexception
@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private SpringWebSocketStompClient stompClient;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ListenableFuture<StompSession> session = stompClient.connectStompSession("ws://127.0.0.1:9090/hello");
		
		@SuppressWarnings("unused")
		Subscription sGreetings = stompClient.subscribe(session, "/topic/greetings");
		
		@SuppressWarnings("unused")
		Subscription stateCpu = stompClient.subscribe(session, "/status/system");

	
	}
}
