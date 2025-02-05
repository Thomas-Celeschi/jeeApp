package myapp.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

@SpringBootTest
public class TestSpringNameDao {

    @Autowired
    SpringNameDao dao;

    @Test
    public void testNames() {
        dao.deleteName(100);
        dao.deleteName(200);
        dao.deleteName(300);
        dao.addName(new Name(100, "Hello"));
        dao.addName(new Name(200, "Salut"));
        assertEquals("Hello", dao.findName(100).getName());
        assertEquals("Salut", dao.findName(200).getName());
        dao.findNames().forEach(System.out::println);
    }

    @Test
    public void testErrors() {
        dao.deleteName(100);
        dao.deleteName(200);
        dao.deleteName(300);
        assertThrows(DataAccessException.class, () -> {
            dao.addName(new Name(300, "Bye"));
            dao.addName(new Name(300, "Au revoir"));
        });

        assertEquals("Bye", dao.findName(300).getName());
    }

    @Test
    public void testWorks() throws Exception {
        long debut = System.currentTimeMillis();

        // exécution des threads
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 1; (i < 5); i++) {
            executor.execute(dao::longWork);
        }

        // attente de la fin des threads
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.HOURS);

        // calcul du temps de réponse
        long fin = System.currentTimeMillis();
        System.out.println("duree = " + (fin - debut) + "ms");
    }

    @Test
    public void testAddNameTwoTimes() {
        dao.deleteName(100);
        dao.deleteName(200);
        dao.deleteName(300);

        assertThrows(DataAccessException.class, () -> {
            dao.addNameTwoTimes(100, "Hello");
        });

        dao.findNames().forEach(System.out::println);
    }

    @Test
    public void testCountName() {
        dao.deleteName(100);
        dao.deleteName(200);
        dao.deleteName(300);
        dao.addName(new Name(100, "Coucou"));
        dao.addName(new Name(200, "Youpi"));
        dao.addName(new Name(300, "Allez"));

        assertEquals(dao.countNames("ou"), 2);
    }

}