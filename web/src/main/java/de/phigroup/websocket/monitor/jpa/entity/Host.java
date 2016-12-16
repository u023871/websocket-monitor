package de.phigroup.websocket.monitor.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * Fill like HTTP POST to http://localhost:9090/monitor/hosts with payload {"ip":"127.0.0.1","name":"ROCKIT","url":"http://localhost:7070"}
 * 
 * See https://spring.io/guides/gs/accessing-data-rest/.
 * 
 * See https://spring.io/guides/gs/consuming-rest-jquery/.
 * 
 * @author eztup
 *
 */
@Entity
@Data
public class Host {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	String name;
	String ip;
	String url;
}
