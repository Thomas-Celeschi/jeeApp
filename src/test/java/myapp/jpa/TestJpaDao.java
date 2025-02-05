package myapp.jpa;

import jakarta.persistence.RollbackException;
import myapp.jpa.model.FirstName;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import myapp.jpa.dao.JpaDao;
import myapp.jpa.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestJpaDao {

    @Autowired
    JpaDao dao;

    @Test
    public void addAndFindPerson() {
        // Création
        var p1 = new Person("Jean", null);
        p1 = dao.addPerson(p1);
        assertTrue(p1.getId() > 0);
        // relecture
        var p2 = dao.findPerson(p1.getId());
        assertEquals("Jean", p2.getFirstName());
        assertEquals(p1.getId(), p2.getId());
    }

    @Test
    public void addPersonNullName() {
        var p1 = new Person(null, null);
        assertThrows(PropertyValueException.class, () -> dao.addPerson(p1));
    }

    @Test
    public void addUpdateAndRemovePerson() {
        // Création
        var p1 = dao.addPerson(new Person("Jean", null));
        assertTrue(p1.getId() > 0);
        // Relecture
        var p2 = dao.findPerson(p1.getId());
        assertEquals("Jean", p2.getFirstName());
        assertEquals(p1.getId(), p2.getId());
        // Modification
        p1.setFirstName("Dimitri");
        assertNotEquals(p1.getFirstName(), dao.findPerson(p1.getId()).getFirstName());
        dao.updatePerson(p1);
        assertEquals(p1.getFirstName(), dao.findPerson(p1.getId()).getFirstName());
        // Suppression
        dao.removePerson(p2.getId());
        assertNull(dao.findPerson(p2.getId()));
    }

    @Test
    public void testUniqueConstraintTable() {
        Date date  = new Date(System.currentTimeMillis());
        dao.addPerson(new Person("Jean", date));

        assertThrows(RollbackException.class, () -> dao.addPerson(new Person("Jean", date)));

    }

    @Test
    public void testUniqueSecondName() {
        dao.addPerson(new Person("Jean", "Paul", null));
        assertThrows(RollbackException.class, () -> dao.addPerson(new Person("Jean", "Paul", null)));
    }

    @Test
    public void testThread() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var p1 = new Person("Jean", null);
        Future<Boolean> f1 = executor.submit(() -> {
            try {
                dao.addPerson(p1);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        Future<Boolean> f2 = executor.submit(() -> {
            try {
                dao.addPerson(p1);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        executor.shutdown();
        while(!executor.awaitTermination(1, TimeUnit.SECONDS)) {
        }

        assertNotSame(f1.get(), f2.get());
    }

    @Test
    public void testFindAllPersons() {
        var p1 = new Person("Jean", "", null);
        var p2 = new Person("Yvan", "Haspekian", null);
        var p3 = new Person("Théau", "Baton", null);
        p1 = dao.addPerson(p1);
        p2 = dao.addPerson(p2);
        p3 = dao.addPerson(p3);

        List<Person> personList = dao.findAllPersons();
        assertEquals(personList.size(), 3);
        assertTrue(personList.containsAll(Arrays.asList(p1, p2, p3)));
    }

    @Test
    public void testFindPersonneByFirstName() {
        var p1 = new Person("Jean", "", null);
        p1 = dao.addPerson(new Person("Jean", "", null));

        var result = dao.findPersonsByFirstName("Jean");
        assertTrue(result.contains(p1));
    }

    @Test
    public void testGetAllFirstNames() {
        var p1 = new Person("Jean", null);
        var p2 = new Person("Paul", null);
        var p3 = new Person("Gautier", null);

        dao.addPerson(p1);
        dao.addPerson(p2);
        dao.addPerson(p3);

        List<FirstName> firstNames = dao.getAllFirstName();

        List<String> names = new ArrayList<>();
        names.add(p1.getFirstName());
        names.add(p2.getFirstName());
        names.add(p3.getFirstName());

        assertEquals(firstNames.stream().filter(firstName -> names.contains(firstName.getFirstName())).toList().size(), 3);
    }


}