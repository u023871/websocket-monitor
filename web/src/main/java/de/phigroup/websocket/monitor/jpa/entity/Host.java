package de.phigroup.websocket.monitor.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Fill like HTTP POST to http://localhost:9090/monitor/hosts with payload
 * {"ip":"127.0.0.1","name":"ROCKIT","url":"http://localhost:7070"}
 * 
 * See https://spring.io/guides/gs/accessing-data-rest/.
 * 
 * See https://spring.io/guides/gs/consuming-rest-jquery/.
 * 
 * See https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-one/.
 * 
 * @author eztup
 *
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class Host extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /** name of monitored host */
    String name;

    /** IP suffix of a subscribed message channel */
    String ip;

    /**
     * the REST endpoint of sender (!) application prefix for triggering sending
     * data to all subscribers of a channel
     */
    String url;
}
