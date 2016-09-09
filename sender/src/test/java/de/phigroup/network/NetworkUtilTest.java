package de.phigroup.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "classpath:applicationContext.xml" })
public class NetworkUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsReachable() {
		
		try {

			boolean success = NetworkUtil.isReachable("127.0.0.1");
			assertTrue(success);
			
			// localhost is usually pingable, just not on smartBase machines...
//			success = NetworkUtil.isReachable("localhost");
//			assertFalse(success);
			
			success = NetworkUtil.isReachable("WU17035962-LSY.ads.dlh.de");
			assertFalse(success);
			
		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}

	@Test
	public void testIsWebServiceOnline() {
		
		try {

			boolean success = NetworkUtil.isWebServiceOnline("/");
			assertTrue(success);
			
			success = NetworkUtil.isWebServiceOnline("/index.html");
			assertTrue(success);
			
			success = NetworkUtil.isWebServiceOnline("/fubar");
			assertFalse(success);
			
		} catch (Exception e) {

			log.error(e.getLocalizedMessage(), e);
			fail();
		}
	}
	
}
