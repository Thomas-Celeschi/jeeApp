package myapp.jpa.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

import myapp.jpa.model.Person;

@Configuration
@EntityScan(basePackageClasses = Person.class)
public class SpringDaoConfig {

}