package spring.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.blog.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
