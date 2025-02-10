package myapp.jpa.dao;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import myapp.jpa.model.FirstName;

import myapp.jpa.model.MasterUE;
import myapp.jpa.model.Person;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public class JpaDao {

  @PersistenceContext EntityManager em;

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

  public <T> T add(T entity) {
    em.persist(entity);
    return entity;
  }

  public <T> void remove(Class<T> clazz, Object pk) {
    T entity = em.find(clazz, pk);
    if (entity != null) {
      em.remove(entity);
    }
  }

  public <T> T update(T entity) {
    return em.merge(entity);
  }

  public <T> T find(Class<T> clazz, Object id) {
    return em.find(clazz, id);
  }

  public <T> Collection<T> findAll(Class<T> clazz) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(clazz);
    Root<T> root = cq.from(clazz);
    cq.select(root);
    TypedQuery<T> q = em.createQuery(cq);
    return q.getResultList();
  }

  public Collection<MasterUE> findAll6CreditsMasterUE() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<MasterUE> cq = cb.createQuery(MasterUE.class);
    Root<MasterUE> root = cq.from(MasterUE.class);
    cq.select(root);
    cq.where(cb.equal(root.get("ects"), 6));
    TypedQuery<MasterUE> q = em.createQuery(cq);
    return q.getResultList();
  }

  public void changeFirstName(long idPerson, String firstName) {
    Person p = em.find(Person.class, idPerson, LockModeType.PESSIMISTIC_WRITE);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    p.setFirstName(firstName);
  }

  /*
   * Ajouter une personne
   */
  public Person addPerson(Person p) {
    em.persist(p);
    return (p);
  }

  /*
   * Charger une personne
   */
  public Person findPerson(long id) {
    Person p = em.find(Person.class, id);
    p.getCars().size();
    p.getMovies().size();
    return p;
  }

  /*
   * Supprimer une personne
   */
  public Person removePerson(long id) {
    Person p = em.find(Person.class, id);
    em.remove(p);
    return p;
  }

  /*
   * Modifier une personne
   */
  public Person updatePerson(Person p) {
    return em.merge(p);
  }

  /*
   * Trouver toutes les personnes
   */
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
    String query = "SELECT new myapp.jpa.model.FirstName(p.id, p.firstName) FROM Person p";
    TypedQuery<FirstName> q = em.createQuery(query, FirstName.class);
    return q.getResultList();
  }

  public List<Person> findPersonsByCarModel(String model) {
    TypedQuery<Person> q = em.createNamedQuery("findPersonsByCarModel", Person.class);
    q.setParameter("model", model);
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
