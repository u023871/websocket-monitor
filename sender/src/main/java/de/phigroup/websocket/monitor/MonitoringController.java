package de.phigroup.websocket.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.phigroup.websocket.client.SpringWebSocketStompMessageBroker;
import de.phigroup.websocket.monitor.dto.RestStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * REST service to trigger sending system status of a specific host
 * 
 * also runs as CRON task for dynamic content (TODO: make more dynamic, web app url and host currently hardcoded)
 * 
 * @author eztup
 *
 */
@RestController
@RequestMapping("/monitoring")
@Slf4j
public class MonitoringController {

	@Autowired
	private SpringWebSocketStompMessageBroker stompBroker;
	
	/**
	 * trigger sending system status from the source host which this app instance is running on
	 * 
	 * check out that fix here regarding truncting of path/request variables with a dot inside: 
	 * http://stackoverflow.com/questions/16332092/spring-mvc-pathvariable-with-dot-is-getting-truncated
	 * 
	 * @throws Exception
	 */
    @RequestMapping(method = RequestMethod.POST, path = "/sendStatus/{sourceHost:.+}")
    public RestStatus sendSystemStatus(@PathVariable String sourceHost) {

    	log.debug("Triggering send system status for source host with id '" + sourceHost + "'");
    	
    	sendStatic(sourceHost);
    	
    	sendDynamic(sourceHost);
    	
    	RestStatus status = new RestStatus(RestStatus.SUCCESS);
    	status.setMessage("Everything sent...");
    	
    	return status;
    }
	
	/**
	 * send out data to subscribers every 10s
	 * 
     * TODO: let iteration be set by UI in next step
     * TODO: read ws endpoint and source host from some configuration
	 */
	public void sendDynamic(String sourceHost) {

		try {
			stompBroker.sendDynamicSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/dynamic", sourceHost);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
		}
	}
	
//	/**
//	 * send out data to subscribers every 10s
//	 * 
//     * TODO: let iteration be set by UI in next step
//     * TODO: read ws endpoint and source host from some configuration
//	 */
//	@Scheduled(cron="*/10 * * * * *")
//	public void sendDynamic() {
//
//		try {
////			stompBroker.sendDynamicSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/dynamic", "127.0.0.1");
//
//			// TODO: read active subscribers from map or so and trigger sending to all of them
//			stompBroker.sendDynamicSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/dynamic", "127.0.0.1");
//
//		} catch (Exception e) {
//
//			log.error(e.getLocalizedMessage(), e);
//		}
//	}
	
	/**
	 * send out static data to subscribers
	 * 
     * TODO: read ws endpoint and source host from some configuration
	 */
	public void sendStatic(String sourceHost) {

		try {
			stompBroker.sendStaticSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/static", sourceHost);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	
//	/**
//	 * send out static data to subscribers
//	 * 
//     * TODO: read ws endpoint and source host from some configuration
//	 */
//	@Scheduled(cron="*/10 * * * * *")
//	public void sendStatic() {
//
//		try {
//			// TODO: read active subscribers from map or so and trigger sending to all of them
//			
//			stompBroker.sendStaticSystemStatus("ws://127.0.0.1:9090/monitor/hello", "/status/system/static", "127.0.0.1");
//
//		} catch (Exception e) {
//
//			log.error(e.getLocalizedMessage(), e);
//		}
//	}
}
