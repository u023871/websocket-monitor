package de.phigroup.websocket.monitor.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
 
/**
 * Note this is a very simple CORS filter that is wide open.
 * This would need to be locked down.
 * Source: http://stackoverflow.com/questions/39565438/no-access-control-allow-origin-error-with-spring-restful-hosted-in-pivotal-web
 * See http://stackoverflow.com/questions/31724994/spring-data-rest-and-cors.
 * See https://jira.spring.io/browse/DATAREST-573.
 * LI 20161215: Implementation currently NOT available in latest Spring Boot Starter 4.2! 
 */
@Component
public class CORSFilter implements Filter {
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        chain.doFilter(req, res);
    }
 
    public void init(FilterConfig filterConfig) {}
 
    public void destroy() {}
 
}