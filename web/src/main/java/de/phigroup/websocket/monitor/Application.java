package de.phigroup.websocket.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.phigroup.websocket.monitor.jpa.entity.PersistenceConfig;


// disable security: http://stackoverflow.com/questions/23894010/spring-boot-security-disable-security
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
@Configuration()
@ComponentScan({"de.phigroup"}) // http://www.baeldung.com/spring-nosuchbeandefinitionexception
@SpringBootApplication
//@Import({WebMvcContext.class, PersistenceContext.class})
@Import({PersistenceConfig.class})
public class Application extends SpringBootServletInitializer {
    
	/**
	 * Override this method of SpringBootServletInitializer or application won't startup as war deployment.
	 * See cpt. 81.1 in http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file.
	 */
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

    	return builder.sources(Application.class);
	}

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    
//    /**
//     * TODO: 20161215: access from non-localhost
//     * "XMLHttpRequest cannot load http://localhost:9090/monitor/hosts. No 
//     * 'Access-Control-Allow-Origin' header is present on the requested resource. 
//     * Origin 'http://wu17035962-lsy.ads.dlh.de:9090' is therefore not allowed access."
//     * only a try... https://spring.io/guides/gs/rest-service-cors/
//     * @return
//     */
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("*");
////                registry.addMapping("/hosts").allowedOrigins(
////                		"http://localhost:9090", 
////                		"http://wu17035962-lsy.ads.dlh.de:9090");
//            }
//        };
//    }
}
