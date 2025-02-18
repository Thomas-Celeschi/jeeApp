package myapp.jpa.dao;

import myapp.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReposity extends JpaRepository<User, Long> {}
