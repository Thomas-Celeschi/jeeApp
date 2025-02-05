package myapp.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * Tester une configuration basée sur un fichier XML.
 */
@SpringBootTest
@ContextConfiguration(locations = "classpath:/myapp/xml/message-config.xml")
public class TestXmlConfigMessage {

    @Autowired
    MessageFactory messageFactory;

    @Autowired
    @Qualifier("bye")
    MessageFactory byeMessageFactory;

    @Autowired
    ApplicationContext context;

    @Test
    public void testMessageFactory() {
        var m1 = messageFactory.newMessage();
        var m2 = messageFactory.newMessage();
        assertEquals("1", m1);
        assertEquals("2", m2);
    }

    @Test
    public void testByeMessageFactory() {
        var m = byeMessageFactory.newMessage();
        assertTrue(m.startsWith("Bye "));
    }

    @Test
    public void testHelloMessageFactory() {
        var factory = context.getBean("helloFactory", MessageFactory.class);
        var m1 = factory.newMessage();
        var m2 = factory.newMessage();
        assertEquals("Hello 1", m1);
        assertEquals("Hello 2", m2);
    }

    @Test
    public void testHandMakeXmlContext() {
        String conf = "classpath:/myapp/xml/message-config.xml";
        try (var ctx = new ClassPathXmlApplicationContext(conf);) {
            var factory = ctx.getBean(MessageFactory.class);
            var msg = factory.newMessage();
            assertEquals("1", msg);
            var counter = ctx.getBean(Counter.class);
            assertEquals(2, counter.nextValue());
        }
    }

}
