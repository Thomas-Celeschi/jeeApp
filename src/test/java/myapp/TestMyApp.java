package myapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Test Spring services
 */
@SpringBootTest
public class TestMyApp {

	@Autowired
	ApplicationContext context;

	@Autowired
	IHello helloByService;

	@Resource(name = "helloService")
	IHello helloByName;
	
	@Autowired
	String bye;

	@Test
	public void testHelloService() {
		assertTrue(helloByService instanceof HelloService);
		helloByService.hello();
	}

	@Test
	public void testHelloByName() {
		assertEquals(helloByService, helloByName);
	}

	@Test
	public void testHelloByContext() {
		assertEquals(helloByName, context.getBean(IHello.class));
		assertEquals(helloByName, context.getBean("helloService"));
	}

	@Test
	public void testBye() {
		assertEquals(bye, "Bye.");
	}

}

