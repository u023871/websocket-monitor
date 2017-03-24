package de.phigroup.websocket.monitor.jpa.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
 
/**
 * see https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-one/
 * 
 * http://www.baeldung.com/database-auditing-jpa
 * 
 * @author U023871
 *
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
        "de.phigroup.websocket.monitor.jpa.repository"
})
@EnableTransactionManagement
public class PersistenceConfig {
    
    @Bean
    public AuditorAware<String> auditorProvider() {
      return new AuditorAwareImpl();
    }
       
}