package com.lhsystems.security.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * https://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/
 * 
 * http://stackoverflow.com/questions/31570021/failed-to-create-websocket-connection-when-spring-security-is-on
 * 
 * @author u023871
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
        // @formatter:off
        auth
            .eraseCredentials(true);
        // @formatter:on
//        super.configure(auth);
		
		
//		  auth.inMemoryAuthentication().withUser("u023871").password("123456").roles("USER");
//	  auth.inMemoryAuthentication().withUser("u023871").password("123456");
//	  auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
//	  auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

//	  http.authorizeRequests()
//		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//		.antMatchers("/dba/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
//		.and().formLogin();
	  
		// http://stackoverflow.com/questions/31570021/failed-to-create-websocket-connection-when-spring-security-is-on
		// http://stackoverflow.com/questions/25639188/disable-basic-authentication-while-using-spring-security-java-configuration
		
//		http
//		.authorizeRequests()
//		.anyRequest().permitAll();
////		.csrf().disable()
////		.and().formLogin()
		;
		
//		http
//	    .authorizeRequests()
//	        .anyRequest().authenticated()
//	        .and()
//	    .formLogin()
//	        .and()
//	    .httpBasic().disable()
//	    .csrf().disable();
		
		http
		.csrf().disable();

	}
}