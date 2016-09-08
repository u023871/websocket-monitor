package de.phigroup.websocket.monitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * http://www.baeldung.com/jackson-exception
 * @author u023871
 *
 */
@Data
public class Greeting {
    
    private String content;

    public Greeting() {
    	
    }
    
    public Greeting(@JsonProperty String content) {
        this.content = content;
    }
}
