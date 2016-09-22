package de.phigroup.websocket.monitor.jpa.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import de.phigroup.websocket.monitor.jpa.entity.Host;

/**
 * http://tommyziegler.com/how-to-expose-the-resourceid-with-spring-data-rest/
 * 
 * @author eztup
 *
 */
@Configuration
public class MyCoolConfiguration extends RepositoryRestMvcConfiguration {
 
    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Host.class);
    }
}
