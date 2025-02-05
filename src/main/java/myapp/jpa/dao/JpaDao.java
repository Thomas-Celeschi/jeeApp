package myapp.jpa.dao;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;

import myapp.jpa.model.FirstName;
import org.springframework.stereotype.Service;

import myapp.jpa.model.Person;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class JpaDao {

    @PersistenceContext
    EntityManager em;

    private EntityManagerFactory factory = null;

    @PostConstruct
    public void init() {
        factory = Persistence.createEntityManagerFactory("myBase");
    }

    @PreDestroy
    public void close() {
        if (factory != null) {
            factory.close();
        }
    }

    /*
     * Ajouter une personne
     */
    public Person addPerson(Person p) {
        em.persist(p);
        return p;
    }

    /*
     * Charger une personne
     */
    public Person findPerson(long id) {
        Person p = em.find(Person.class, id);
        return p;
    }

    /*
     * Fermeture d'un EM (avec rollback éventuellement)
     */


    public void updatePerson(Person p) {
        em.merge(p);
    }

    public void removePerson(long id) {
        Person p = em.find(Person.class, id);
        em.remove(p);
    }

    public List<Person> findAllPersons() {
        String query = "SELECT p FROM Person p";
        TypedQuery<Person> q = em.createQuery(query, Person.class);
        return q.getResultList();
    }

    public List<Person> findPersonsByFirstName(String pattern) {
        TypedQuery<Person> q = em.createNamedQuery("findPersonsByFirstName", Person.class);
        q.setParameter("pattern", pattern);
        return q.getResultList();
    }

    public List<FirstName> getAllFirstName() {
        String query = "SELECT new myapp.jpa.model.FirstName(p.id,p.firstName) FROM Person p";
        TypedQuery<FirstName> q = em.createQuery(query, FirstName.class);
        return q.getResultList();
    }

//    private void closeEntityManager(EntityManager em) {
//        if (em == null || !em.isOpen())
//            return;
//
//        var t = em.getTransaction();
//        if (t.isActive()) {
//            try {
//                t.rollback();
//            } catch (PersistenceException e) {
//                e.printStackTrace(System.err);
//            }
//        }
//        em.close();
//    }
}