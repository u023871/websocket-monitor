package de.phigroup.websocket.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Random;

import org.hyperic.sigar.SigarException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;

import de.phigroup.http.client.SpringHttpClient;
import de.phigroup.websocket.monitor.dto.SigarDynamicSystemStats;
import de.phigroup.websocket.monitor.dto.SigarStaticSystemStats;
import lombok.extern.slf4j.Slf4j;

/**
 * see
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#websocket-stomp-client
 * 
 * @author u023871
 *
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class SpringWebSocketStompClientTest {
	
	@Autowired
	private SpringHttpClient client;

	@Autowired
	private SpringWebSocketStompClient stompClient;

	ListenableFuture<StompSession> session;

	@Before
	public void setUp() {
		System.out.println("setUp: " + client.getServer());

		/*
		 * Test Sigar stuff with -Djava.library.path="D:\DEV\Workspaces\Spring\gs-messaging-stomp-websocket\stomp-websocket-client\src\main\resources\sigar\lib",
		 * see http://stackoverflow.com/questions/11612711/sigar-unsatisfiedlinkerror!
		 */
		System.setProperty("java.library.path", "C:\\development\\Workspaces\\Spring\\websocket-monitor\\sender\\src\\main\\resources\\sigar\\lib");
	}
	
	/**
	 * See
	 * http://databasefaq.com/index.php/answer/69808/java-spring-spring-websocket-websocketstompclient-wont-connect-to-sockjs-endpoint
	 * 
	 * okay this works and sends a Greeting right to the web socket listening
	 * browser page :-)
	 * 
	 */
//	@Test
	public void testSendGreeting() {

		try {

			assertNotNull(stompClient);
			stompClient.sendGreeting("ws://127.0.0.1:9090/monitor/hello", "/topic/greetings",
					"Hi Lars " + (new Random().nextGaussian()));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}

	@Test
	public void testSigarDynamicSystemStats() {
		
		try {
			SigarDynamicSystemStats stats = SigarDynamicSystemStats.getNewFilledInstance();
			
			log.debug("stats: " + stats);
			
		} catch (SigarException e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}

	@Test
	public void testSigarStaticSystemStats() {
		
		try {
			SigarStaticSystemStats stats = SigarStaticSystemStats.getFilledInstance();
			
			log.debug("stats: " + stats);
			
		} catch (SigarException e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}

	/**
	 * See
	 * http://databasefaq.com/index.php/answer/69808/java-spring-spring-websocket-websocketstompclient-wont-connect-to-sockjs-endpoint
	 * 
	 * okay this works and sends a Greeting right to the web socket listening
	 * browser page :-)
	 * 
	 */
	// @Test
	public void testSendGreetingLoop() {

		try {
			assertNotNull(stompClient);

			for (int i = 0; i < 5; i++) {

				stompClient.sendGreeting("ws://127.0.0.1:9090/monitor/hello", "/topic/greetings",
						"Hi Leute " + (new Random().nextGaussian()));

				Thread.sleep(1000);
			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}
	
	private void sendDynamicSystemStatus() throws Exception {

//		stompClient.sendDynamicSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/dynamic", "127.0.0.1");
		stompClient.sendDynamicSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/dynamic", "192.168.1.43");
	}
	
	@Test
	public void testSendStaticSystemStatus() {

		try {

			assertNotNull(stompClient);
//			stompClient.sendStaticSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/static", "127.0.0.1");
			stompClient.sendStaticSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/static", "192.168.1.43");

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testSendDynamicSystemStatus() {

		try {

			assertNotNull(stompClient);
			sendDynamicSystemStatus();

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}
	
//	@Test
	public void testSendDynamicSystemStatusLoop() {

		try {
			assertNotNull(stompClient);

			for (int i = 0; i < 5; i++) {

				sendDynamicSystemStatus();

				Thread.sleep(1000);
			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}
}
