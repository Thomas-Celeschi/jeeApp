package myapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Spring services
 */
@SpringBootTest
@ActiveProfiles("devel")
public class TestILoggerDevel {


    @Autowired
    @Qualifier("stderr")
    ILogger stderrLoggerByQualifier;

    @Autowired
    @Qualifier("fileLoggerWithConstructor") // pour choisir l'implantation
    ILogger fileLoggerWithConstructor;

    @Autowired
    @Qualifier("beanFileLogger")
    ILogger beanFileLogger;

    @Test
    public void testStderrLogger(){
        assertTrue(stderrLoggerByQualifier instanceof StderrLogger);
        stderrLoggerByQualifier.log("Ceci est un test pour le service de log par stderr.");
    }

    @Test
    public void testFileLoggerWithConstructor() {
        assertTrue(fileLoggerWithConstructor instanceof FileLogger);
        fileLoggerWithConstructor.log("Ceci est un test avec un logger par fichier.");
    }

    @Test
    public void testBeanFileLogger() {
        assertTrue(beanFileLogger instanceof BeanFileLogger);
        assertEquals(((BeanFileLogger) beanFileLogger).getFileName(), "myapp.log");
        beanFileLogger.log("Ceci est un test avec un bean logger");
    }

}
