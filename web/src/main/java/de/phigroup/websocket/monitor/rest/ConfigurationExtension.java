package de.phigroup.websocket.monitor.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import de.phigroup.websocket.monitor.jpa.entity.Host;

/**
 * http://tommyziegler.com/how-to-expose-the-resourceid-with-spring-data-rest/
 * 
 * Update to newer Spring Boot Starter version:
 * http://stackoverflow.com/questions/42585504/spring-data-rest-ids-or-link-href
 * 
 * @author eztup
 *
 */
@Configuration
public class ConfigurationExtension extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Host.class);
    }
}
