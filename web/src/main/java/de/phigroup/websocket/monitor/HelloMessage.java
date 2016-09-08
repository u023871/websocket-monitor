package de.phigroup.websocket.monitor;

import lombok.Data;

@Data
public class HelloMessage {

    private String name;
    
    public String getName() {
        return name;
    }

}
