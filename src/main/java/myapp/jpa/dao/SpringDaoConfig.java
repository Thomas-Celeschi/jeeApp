package myapp.jpa.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import myapp.jpa.model.Person;

@Configuration
@EntityScan(basePackageClasses = Person.class)
@EnableJpaRepositories(basePackageClasses = SpringDaoConfig.class)
public class SpringDaoConfig {}
