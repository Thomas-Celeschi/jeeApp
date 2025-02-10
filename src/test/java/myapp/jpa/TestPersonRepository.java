package myapp.jpa.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import myapp.jpa.dao.PersonRepository;
import myapp.jpa.model.Person;

@SpringBootTest
public class TestPersonRepository {

    @Autowired
    PersonRepository dao;

    @Test
    public void testRepository() {
        // détruire les instances
        dao.deleteAll();
        assertFalse(dao.findAll().iterator().hasNext());
        // créer une instance
        var p = new Person("AAA", new Date());
        dao.save(p);
        // tester une instance
        var op = dao.findById(p.getId());
        assertTrue(op.isPresent());
        p = op.get();
        assertEquals("AAA", p.getFirstName());
    }

    @Test
    public void testFindByFirstName() {
        // détruire les instances
        dao.deleteAll();
        assertFalse(dao.findAll().iterator().hasNext());
        // créer une instance
        dao.save(new Person("Jhon", new Date()));
        dao.save(new Person("Jhonny", new Date()));
        dao.save(new Person("Timéo", new Date()));
        dao.save(new Person("TimJhon", new Date()));
        // teste de la méthode findByFirstName
        var list1 = dao.findByFirstName("Jhon");
        assertEquals(1, list1.size());
        assertEquals("Jhon", list1.get(0).getFirstName());
        // teste de la méthode findByFirstNameLike
        var list2 = dao.findByFirstNameLike("%Jhon%");
        assertEquals(3, list2.size());
        assertEquals("Jhon", list2.get(0).getFirstName());
        assertEquals("Jhonny", list2.get(1).getFirstName());
        assertEquals("TimJhon", list2.get(2).getFirstName());
    }

    @Test
    public void testDeleteLikeFirstName() {
        // détruire les instances
        dao.deleteAll();
        assertFalse(dao.findAll().iterator().hasNext());
        // créer une instance
        dao.save(new Person("Jhon", new Date()));
        dao.save(new Person("Jhonny", new Date()));
        dao.save(new Person("Timéo", new Date()));
        dao.save(new Person("TimJhon", new Date()));
        // teste de la méthode deleteByFirstName
        var list = dao.deleteLikeFirstName("Jhon");
        assertEquals(3, list.size());
        assertEquals("Jhon", list.get(0).getFirstName());
        assertEquals("Jhonny", list.get(1).getFirstName());
        assertEquals("TimJhon", list.get(2).getFirstName());
    }

}