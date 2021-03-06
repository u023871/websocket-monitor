package de.phigroup.websocket.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.concurrent.ListenableFuture;

import de.phigroup.websocket.client.SpringWebSocketStompMessageBroker;
import de.phigroup.websocket.client.SpringWebSocketStompMessageSubscriber;

@Configuration
@ComponentScan({"de.phigroup"}) // http://www.baeldung.com/spring-nosuchbeandefinitionexception
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private SpringWebSocketStompMessageBroker stompBroker;
	
	@Autowired
	private SpringWebSocketStompMessageSubscriber stompSubscriber;
	
	@Autowired
	private MonitoringController monitoringController;

	/**
	 * Override this method of SpringBootServletInitializer or application won't startup as war deployment.
	 * See cpt. 81.1 in http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file.
	 */
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

    	return builder.sources(Application.class);
	}
    
    public static void main(String[] args) {
		
		/*
		 * Test Sigar stuff with -Djava.library.path="D:\DEV\Workspaces\Spring\gs-messaging-stomp-websocket\stomp-websocket-client\src\main\resources\sigar\lib",
		 * see http://stackoverflow.com/questions/11612711/sigar-unsatisfiedlinkerror!
		 * 
		 * TODO: set to classpath or so to make it dynamic
		 */
		System.setProperty("java.library.path", "D:\\DEV\\Workspaces\\u023871\\websocket-monitor\\sender\\src\\main\\resources\\sigar\\lib");
		
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//
		// TODO: following is only an example implementation for subscription via Java... does not make sense so far for the monitoring webapp...
		//
		
		ListenableFuture<StompSession> session = stompBroker.connectStompSession("ws://127.0.0.1:9090/monitor/hello");
		
		@SuppressWarnings("unused")
		Subscription sGreetings = stompSubscriber.subscribe(session, "/topic/greetings", "127.0.0.1");
		
		@SuppressWarnings("unused")
		Subscription stateCpu = stompSubscriber.subscribe(session, "/status/system", "127.0.0.1");

		monitoringController.sendStatic("127.0.0.1");
		
	}
}
