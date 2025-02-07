package myapp.jpa;

import jakarta.persistence.RollbackException;
import myapp.jpa.model.Address;
import myapp.jpa.model.Car;
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

  @Autowired JpaDao dao;

  @Test
  public void addAndFindPerson() {
    // Création
    var p1 = new Person("Jean", null);
    p1 = dao.add(p1);
    assertTrue(p1.getId() > 0);
    // relecture
    var p2 = dao.find(Person.class, p1.getId());
    assertEquals("Jean", p2.getFirstName());
    assertEquals(p1.getId(), p2.getId());
  }

  @Test
  public void addPersonNullName() {
    var p1 = new Person(null, null);
    assertThrows(PropertyValueException.class, () -> dao.add(p1));
  }

  @Test
  public void addUpdateAndRemovePerson() {
    // Création
    var p1 = dao.add(new Person("Jean", null));
    assertTrue(p1.getId() > 0);
    // Relecture
    var p2 = dao.find(Person.class, p1.getId());
    assertEquals("Jean", p2.getFirstName());
    assertEquals(p1.getId(), p2.getId());
    // Modification
    p1.setFirstName("Dimitri");
    assertNotEquals(p1.getFirstName(), dao.find(Person.class, p1.getId()).getFirstName());
    var p3 = dao.update(p1);
    assertEquals(p1.getFirstName(), p3.getFirstName());
    // Suppression
    dao.remove(Person.class, p2.getId());
    assertNull(dao.find(Person.class, p2.getId()));
  }

  @Test
  public void testUniqueConstraintTable() {
    Date date = new Date(System.currentTimeMillis());
    dao.add(new Person("Jean", date));

    assertThrows(RollbackException.class, () -> dao.add(new Person("Jean", date)));
  }

  @Test
  public void testUniqueSecondName() {
    dao.add(new Person("Jean", "Paul", null));
    assertThrows(RollbackException.class, () -> dao.add(new Person("Jean", "Paul", null)));
  }

  @Test
  public void testThread() throws InterruptedException, ExecutionException {
    ExecutorService executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    var p1 = new Person("Jean", null);
    Future<Boolean> f1 =
        executor.submit(
            () -> {
              try {
                dao.add(p1);
                return true;
              } catch (Exception e) {
                return false;
              }
            });
    Future<Boolean> f2 =
        executor.submit(
            () -> {
              try {
                dao.add(p1);
                return true;
              } catch (Exception e) {
                return false;
              }
            });
    executor.shutdown();
    while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {}

    assertNotSame(f1.get(), f2.get());
  }

  @Test
  public void testFindAllPersons() {
    var p1 = new Person("Jean", "", null);
    var p2 = new Person("Yvan", "Haspekian", null);
    var p3 = new Person("Théau", "Baton", null);
    p1 = dao.add(p1);
    p2 = dao.add(p2);
    p3 = dao.add(p3);

    List<Person> personList = dao.findAll("SELECT p FROM Person p", Person.class).stream().toList();
    assertEquals(personList.size(), 3);
    assertEquals(personList.get(0).getFirstName(), "Jean");
    assertEquals(personList.get(1).getFirstName(), "Yvan");
    assertEquals(personList.get(2).getFirstName(), "Théau");
  }

  @Test
  public void testFindPersonneByFirstName() {
    var p1 = new Person("Jean", "", null);
    p1 = dao.add(new Person("Jean", "", null));

    var result = dao.findPersonsByFirstName("Jean");
    assertTrue(result.contains(p1));
  }

  @Test
  public void testGetAllFirstNames() {
    var p1 = new Person("Jean", null);
    var p2 = new Person("Paul", null);
    var p3 = new Person("Gautier", null);

    dao.add(p1);
    dao.add(p2);
    dao.add(p3);

    List<FirstName> firstNames = dao.getAllFirstName();

    List<String> names = new ArrayList<>();
    names.add(p1.getFirstName());
    names.add(p2.getFirstName());
    names.add(p3.getFirstName());

    assertEquals(
        firstNames.stream()
            .filter(firstName -> names.contains(firstName.getFirstName()))
            .toList()
            .size(),
        3);
  }

  @Test
  public void tesAddress() {
    var p1 = new Person("Jean", "", null);
    var ad1 = new Address("163 Avenue de Luminy", "Marseille", "France");
    var ad2 = new Address("6942 Boulevard de la liberté", "Marseille", "France");
    p1.setAddress(ad1);
    p1.setAddress2(ad2);
    p1 = dao.addPerson(p1);

    var p2 = dao.findPerson(p1.getId());
    assertSame(p2.getAddress().getStreet(), ad1.getStreet());
    assertSame(p2.getAddress2().getStreet(), ad2.getStreet());
  }

  @Test
  public void testSetOwner() {
    var p1 = new Person("Jean", null);
    p1 = dao.add(p1);

    var c1 = new Car("EG-480-VX", "Dacia");
    c1.setOwner(p1);
    c1 = dao.add(c1);

    dao.update(c1);
    assertEquals(p1.getId(), dao.find(Car.class, c1.getImmatriculation()).getOwner().getId());
  }

  @Test
  public void testParentToFils() {
    var p1 = new Person("Jean", "", null);
    p1.addCar(new Car("EG-480-VX", "Dacia"));
    p1 = dao.add(p1);

    var newP1 = dao.findPerson(p1.getId());
    assertEquals(newP1.getCars().size(), 1);
    assertEquals(newP1.getCars().stream().toList().get(0).getImmatriculation(), "EG-480-VX");

    var car = new Car("YV-069-AN", "Renault Zoe");
    car = dao.add(car);

    p1.addCar(car);
    dao.update(p1);

    var newP2 = dao.findPerson(p1.getId());
    assertEquals(newP2.getCars().size(), 2);
    assertEquals(newP2.getCars().stream().toList().get(1).getImmatriculation(), "YV-069-AN");
  }

  @Test
  public void testFindPersonByCarModel() {
    var p1 = new Person("Jean", "", null);
    p1 = dao.add(p1);
    var c1 = new Car("EG-480-VX", "Dacia");
    c1 = dao.add(c1);
    p1.addCar(c1);
    p1 = dao.update(p1);

    List<Person> personList = dao.findPersonsByCarModel("Dacia");
    assertEquals(personList.get(0).getId(), p1.getId());
  }

  @Test
  public void testModelOrder() {
    var p1 = new Person("Jean", "", null);
    p1 = dao.add(p1);

    var c1 = new Car("EG-480-VX", "Dacia");
    c1 = dao.add(c1);
    p1.addCar(c1);

    var c2 = new Car("AZ-369-BY", "Alpha Romeo");
    c2 = dao.add(c2);
    p1.addCar(c2);

    p1 = dao.update(p1);

    var c3 = p1.getCars().stream().toList().get(0);
    assertEquals(c3.getModel(), "Alpha Romeo");
  }


}
