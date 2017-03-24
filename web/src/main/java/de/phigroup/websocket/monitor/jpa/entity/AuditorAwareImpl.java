package de.phigroup.websocket.monitor.jpa.entity;

import org.springframework.data.domain.AuditorAware;

/**
 * http://www.baeldung.com/database-auditing-jpa
 * 
 * @author U023871
 *
 */
public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Override
    public String getCurrentAuditor() {

        return "U023871";
    }
 
}