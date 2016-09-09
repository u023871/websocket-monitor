package de.phigroup.websocket.client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * this damned web integration testing if so f*cking hard...
 * 
 * @author u023871
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:applicationContext.xml" })
@org.springframework.boot.test.context.SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
//@Configuration
//@ComponentScan({"de.phigroup"}) // http://www.baeldung.com/spring-nosuchbeandefinitionexception
public class SpringBootTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Before
	public void setUp() {
	}

    @Test
    public void exampleTest() {
        String body = this.restTemplate.getForObject("/", String.class);
        assertTrue("Hello World".equals(body));
    }
}