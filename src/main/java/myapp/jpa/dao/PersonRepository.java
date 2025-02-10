package myapp.jpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import myapp.jpa.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PersonRepository extends JpaRepository<Person, Long> {

  List<Person> findByFirstName(String name);

  List<Person> findByFirstNameLike(String name);

  @Transactional
  @Query("SELECT p FROM Person p WHERE p.firstName LIKE %:pattern%")
  List<Person> deleteLikeFirstName(String pattern);
}
