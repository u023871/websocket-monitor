package de.phigroup.websocket.monitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author u023871
 *
 */
@Data
public class RestStatus {
    
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	
    private String status;
    private String message;

    public RestStatus() {
    	
    }
    
    public RestStatus(@JsonProperty String status) {
        this.status = status;
    }
}
