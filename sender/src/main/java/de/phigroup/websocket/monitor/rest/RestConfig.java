package de.phigroup.websocket.monitor.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * global cross origin conf. (see https://spring.io/guides/gs/rest-service-cors/), con be overridden by fine grained @@CrossOrigin in REST class
 * 
 * http://stackoverflow.com/questions/35091524/spring-cors-no-access-control-allow-origin-header-is-present
 * 
 * @author eztup
 *
 */
@Configuration
@EnableWebMvc
public class RestConfig extends WebMvcConfigurerAdapter {

	/**
	 * Allow CORS to everything from everywhere.
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**").allowedOrigins("*");
    	registry.addMapping("/monitoring/sendStatus").allowedOrigins("*");
    }
}
